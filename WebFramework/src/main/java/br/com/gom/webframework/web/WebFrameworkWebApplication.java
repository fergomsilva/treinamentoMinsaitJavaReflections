package br.com.gom.webframework.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkRepository;
import br.com.gom.webframework.annotations.WebFrameworkService;
import br.com.gom.webframework.datastructures.InterfaceImplementationMap;
import br.com.gom.webframework.datastructures.configs.WebFrameworkConfigMap;
import br.com.gom.webframework.datastructures.controllers.ControllerMap;
import br.com.gom.webframework.datastructures.controllers.ParameterMethodControllerData;
import br.com.gom.webframework.datastructures.controllers.RequestControllerData;
import br.com.gom.webframework.datastructures.controllers.SplitUrlControllerData;
import br.com.gom.webframework.enumerations.HTTP_METHOD_ENUM;
import br.com.gom.webframework.explorer.ClassExplorer;
import br.com.gom.webframework.util.WebFrameworkLogger;
import br.com.gom.webframework.util.WebFrameworkUtil;


public class WebFrameworkWebApplication{
    private static final String LOG_MODULO = "Embeded Web Container";
    private static final String LOG_MODULO_CONFIG = "Configs Explorer";
    private static final String LOG_MODULO_METADATA = "Metadata Explorer";

    private WebFrameworkWebApplication(){
        super();
    }
    
    public static void run(final Class<?> sourceClass){
        final long ini = System.currentTimeMillis();
        // desligar todos os logs do apache tomcat
        java.util.logging.Logger.getLogger( "org.apache" )
            .setLevel( java.util.logging.Level.OFF );

        WebFrameworkLogger.showBanner();
        WebFrameworkLogger.log( LOG_MODULO, 
            "Iniciando WebFrameworkWebApplication!" );
        try{
            // class explorer
            // extracao de metadados
            extractMetadata( sourceClass );

            // encontrar configuracoes
            findConfig( sourceClass );

            final Tomcat tomcat = new Tomcat();
            final Connector connector = new Connector();
            final int port = WebFrameworkConfigMap.port(); //pega a porta configurada ou padrão
            connector.setPort( port );
            tomcat.setConnector( connector );
            WebFrameworkLogger.log( LOG_MODULO, "Iniciando na porta %d.", port );
            WebFrameworkLogger.log( LOG_MODULO, 
                "Acesso disponivel a partir da URL: http://localhost:%s.", port );
            
            // contexto olhando a raiz da aplicacao
            // procurando classes na raiz da app
            final Context context = tomcat.addContext( "", 
                new File( "." ).getAbsolutePath() );
            Tomcat.addServlet( context, "WebFrameworkDispacherServlet", 
                new WebFrameworkDispatcherServlet() );

            // tudo que digitar na URL vai cair neste ponto
            context.addServletMappingDecoded( "/*", 
                "WebFrameworkDispacherServlet" );

            final long fim = System.currentTimeMillis();
            WebFrameworkLogger.log( LOG_MODULO, "Tomcat iniciado em %dms\n", (fim - ini) );

            // start
            tomcat.start();
            tomcat.getServer().await();
        }catch( Exception e ){
            WebFrameworkLogger.error( LOG_MODULO, e, e.getLocalizedMessage() );
        }
    }

    private static void  extractMetadata(final Class<?> sourceClass){
        final List<String> allClasses = ClassExplorer.retrieveAllClasses( sourceClass );
        try{
            for( String classe : allClasses ){
                // recupera as anotacoes da classe
                final Annotation[] annotations = Class.forName( classe ).getAnnotations();
                for( Annotation classAnnotation : annotations ){
                    if( classAnnotation.annotationType().isAssignableFrom( WebFrameworkController.class ) ){
                        WebFrameworkLogger.log( LOG_MODULO_METADATA, "Found a controller %s", classe );
                        extractMethodsFromController( classe );
                    }else if( classAnnotation.annotationType().isAssignableFrom( WebFrameworkService.class ) ){
                        WebFrameworkLogger.log( LOG_MODULO_METADATA, "Found a service Implementation %s", classe );
                        for( Class<?> interfaceWeb : Class.forName( classe ).getInterfaces() ){
                            WebFrameworkLogger.log( LOG_MODULO_METADATA, "Class implements %s", interfaceWeb.getName() );
                            InterfaceImplementationMap.put( interfaceWeb.getName(), classe );
                        }
                    }else if( classAnnotation.annotationType().isAssignableFrom( WebFrameworkRepository.class ) ){
                        WebFrameworkLogger.log( LOG_MODULO_METADATA, "Found a repository Implementation %s", classe );
                        for( Class<?> interfaceRepo : Class.forName( classe ).getInterfaces() ){
                            WebFrameworkLogger.log( LOG_MODULO_METADATA, "Class implements %s", interfaceRepo.getName() );
                            InterfaceImplementationMap.put( interfaceRepo.getName(), classe );
                        }
                    }
                }
            }
            ControllerMap.listValues()
                .forEach( item -> WebFrameworkLogger.log( LOG_MODULO_METADATA, item.toString() ) );
        }catch( Exception e ){
            WebFrameworkLogger.error( LOG_MODULO_METADATA, e, e.getLocalizedMessage() );
        }
    }

    /**
     * Verifica a existencia de configurações passadas para o APP, e se existir memoriza em memória.<br>
     * <p>
     * A prioridade é a properties 'webframework-config.properties' e depois as variaveis de ambiente 
     * começadas com 'WEBFRAMEWORK_'.
     * </p>
     * @param sourceClass classe principal do APP
     * @throws IOException erro na leitura do properties
     */
    private static void findConfig(final Class<?> sourceClass) throws IOException{
        WebFrameworkLogger.log( LOG_MODULO_CONFIG, "Procurando pelo properties 'webframework-config'." );
        // tenta encontrar a properties 'webframework-config.properties'
        try( final InputStream streamProperties = sourceClass.getClassLoader().getResourceAsStream( "webframework-config.properties" ) ){
            if( streamProperties != null ){
                final Properties prop = new Properties();
                prop.load( streamProperties );
                if( !prop.isEmpty() ){
                    WebFrameworkLogger.log( LOG_MODULO_CONFIG, "Carregando as propriedades do properties." );
                    final AtomicInteger count = new AtomicInteger( 0 );
                    // carrega todas as propriedades do arquivo em memória
                    prop.entrySet().stream().forEach( config -> {
                        count.incrementAndGet();
                        WebFrameworkConfigMap.put( config.getKey().toString(), config.getValue().toString() );
                    } );
                    WebFrameworkLogger.log( LOG_MODULO_CONFIG, "\tCarreganda(s) %s propriedade(s) do properties.", 
                        count.get() );
                }
            }
        }
        // tenta encontrar variaveis de ambiente iniciadas com 'WEBFRAMEWORK_'
        WebFrameworkLogger.log( LOG_MODULO_CONFIG, "Procurando por variveis de ambiente." );
        final AtomicInteger count = new AtomicInteger( 0 );
        System.getenv().entrySet().stream()
            .filter( env -> env.getKey().startsWith( "WEBFRAMEWORK_" ) 
                && !WebFrameworkConfigMap.hasKey( env.getKey() ) )
                // filtra todas as variaveis de ambiente para carregar apenas as que não existem na properties
            .forEach( env -> {
                count.incrementAndGet();
                WebFrameworkConfigMap.put( env.getKey(), env.getValue() );
             } );
        if( count.get() > 0 )
            WebFrameworkLogger.log( LOG_MODULO_CONFIG, "\tCarreganda(s) %s variavel(is) de ambiente.", 
                count.get() );
    }

    private static void extractMethodsFromController(final String classNameController) throws SecurityException, ClassNotFoundException{
        // recuperar todos os métodos da classe
        for( Method method : Class.forName( classNameController ).getDeclaredMethods() ){
            if( Modifier.PUBLIC == method.getModifiers() ){
                //LOG_MODULO_METADATA
                for( Annotation annotation : method.getAnnotations() ){
                    final RequestControllerData data = new RequestControllerData();
                    data.setHttpMethod( HTTP_METHOD_ENUM.valueOfByAnnotation( annotation ) );
                    data.setUrl( WebFrameworkUtil.getUrlValueFromAnnotation( annotation ) );
                    data.setControllerClass( classNameController );
                    data.setControllerMethod( method.getName() );
                    
                    // extrai os parametros do método da controller
                    data.setMethodParameters( WebFrameworkUtil.extractParametersFromMethod( method ) );
                    // marca se existe algum parametro com a primeira anotação de body
                    data.setHasBodyParameterAnnotation( data.getMethodParameters().stream()
                        .anyMatch( ParameterMethodControllerData::isBodyParameterAnnotation ) );
                    // marca se existe algum parametro com a primeira anotação de query param
                    data.setHasRequestParamAnnotation( data.getMethodParameters().stream()
                        .anyMatch( ParameterMethodControllerData::isRequestParameterAnnotation ) );
                    
                    // extrai as 'partes' da URI configurada no método
                    data.setUrlSplits( WebFrameworkUtil.extractUrlSplits( data.getUrl(), data.getMethodParameters() ) );
                    // marca se a URI não possui variaveis
                    data.setStaticUrl( data.getUrlSplits().stream().noneMatch( SplitUrlControllerData::isParameter ) );
                    if( !data.isStaticUrl() ) // se a URI tem variável altera a URI para regex
                        data.setUrlRegex( WebFrameworkUtil.generateUrlRegex( data.getUrlSplits() ) );
                    ControllerMap.put( data );
                }
            }
        }
    }

}