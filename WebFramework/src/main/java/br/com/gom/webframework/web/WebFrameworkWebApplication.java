package br.com.gom.webframework.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.WebFrameworkPutMethod;
import br.com.gom.webframework.annotations.WebFrameworkService;
import br.com.gom.webframework.datastructures.configs.WebFrameworkConfigMap;
import br.com.gom.webframework.datastructures.controllers.ControllerMap;
import br.com.gom.webframework.datastructures.controllers.RequestControllerData;
import br.com.gom.webframework.datastructures.injections.ServiceImplementationMap;
import br.com.gom.webframework.enumerations.HTTP_METHOD_ENUM;
import br.com.gom.webframework.explorer.ClassExplorer;
import br.com.gom.webframework.util.WebFrameworkLogger;


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
            final int port = WebFrameworkConfigMap.port();
            connector.setPort( port );
            tomcat.setConnector( connector );
            WebFrameworkLogger.log( LOG_MODULO, "Iniciando na porta %d.", port );
            WebFrameworkLogger.log( LOG_MODULO, "Acesso disponivel a partir da URL: http://localhost:%s.", 
                port );
            
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
            WebFrameworkLogger.log( LOG_MODULO, "Tomcat iniciado em %dms", (fim - ini) );

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
                        extractMethods( classe );
                    }else if( classAnnotation.annotationType().isAssignableFrom( WebFrameworkService.class ) ){
                        WebFrameworkLogger.log( LOG_MODULO_METADATA, "Found a service Implementation %s", classe );
                        for( Class<?> interfaceWeb : Class.forName( classe ).getInterfaces() ){
                            WebFrameworkLogger.log( LOG_MODULO_METADATA, "Class implements %s", interfaceWeb.getName() );
                            ServiceImplementationMap.put( interfaceWeb.getName(), classe );
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

    private static void findConfig(final Class<?> sourceClass) throws IOException{
        WebFrameworkLogger.log( LOG_MODULO_CONFIG, "Procurando pelo properties 'webframework-config'." );
        try( final InputStream streamProperties = sourceClass.getClassLoader().getResourceAsStream( "webframework-config.properties" ) ){
            if( streamProperties != null ){
                final Properties prop = new Properties();
                prop.load( streamProperties );
                if( !prop.isEmpty() ){
                    WebFrameworkLogger.log( LOG_MODULO_CONFIG, "Carregando as propriedades do properties." );
                    final AtomicInteger count = new AtomicInteger( 0 );
                    prop.entrySet().stream().forEach( config -> {
                        count.incrementAndGet();
                        WebFrameworkConfigMap.put( config.getKey().toString(), config.getValue().toString() );
                    } );
                    WebFrameworkLogger.log( LOG_MODULO_CONFIG, "\tCarreganda(s) %s propriedade(s) do properties.", 
                        count.get() );
                }
            }
        }
        WebFrameworkLogger.log( LOG_MODULO_CONFIG, "Procurando por variveis de ambiente." );
        final AtomicInteger count = new AtomicInteger( 0 );
        System.getenv().entrySet().stream()
            .filter( env -> env.getKey().startsWith( "WEBFRAMEWORK_" ) 
                && !WebFrameworkConfigMap.hasKey( env.getKey() ) )
            .forEach( env -> {
                count.incrementAndGet();
                WebFrameworkConfigMap.put( env.getKey(), env.getValue() );
             } );
        if( count.get() > 0 )
            WebFrameworkLogger.log( LOG_MODULO_CONFIG, "\tCarreganda(s) %s variavel(is) de ambiente.", 
                count.get() );
    }

    private static void  extractMethods(final String className) throws SecurityException, ClassNotFoundException{
        // recuperar todos os métodos da classe
        for( Method method : Class.forName( className ).getDeclaredMethods() ){
            if( Modifier.PUBLIC == method.getModifiers() ){
                for( Annotation annotation : method.getAnnotations() ){
                    ControllerMap.put( RequestControllerData.builder()
                        .httpMethod( HTTP_METHOD_ENUM.valueOfByAnnotation( annotation ) )
                        .url( getValueFromAnnotation( annotation ) )
                        .controllerClass( className )
                        .controllerMethod( method.getName() )
                        .build()
                    );
                }
            }
        }
    }

    private static String getValueFromAnnotation(final Annotation annotation){
        final Optional<HTTP_METHOD_ENUM> httpMethod = Optional.ofNullable( 
            HTTP_METHOD_ENUM.valueOfByAnnotation( annotation ) );
        if( httpMethod.isPresent() ){
            if( httpMethod.get().isGet() )
                return ( (WebFrameworkGetMethod)annotation ).value();
            else if( httpMethod.get().isPost() )
                return ( (WebFrameworkPostMethod)annotation ).value();
            else if( httpMethod.get().isPut() )
                return ( (WebFrameworkPutMethod)annotation ).value();
            else if( httpMethod.get().isDelete() )
                return ( (WebFrameworkDeleteMethod)annotation ).value();
        }
        return null;
    }

}