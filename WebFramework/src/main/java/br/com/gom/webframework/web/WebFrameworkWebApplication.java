package br.com.gom.webframework.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkService;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathParameter;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParameter;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPutMethod;
import br.com.gom.webframework.datastructures.configs.WebFrameworkConfigMap;
import br.com.gom.webframework.datastructures.controllers.ControllerMap;
import br.com.gom.webframework.datastructures.controllers.ParameterMethodControllerData;
import br.com.gom.webframework.datastructures.controllers.RequestControllerData;
import br.com.gom.webframework.datastructures.controllers.SplitUrlControllerData;
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

    private static void extractMethods(final String className) throws SecurityException, ClassNotFoundException{
        // recuperar todos os métodos da classe
        for( Method method : Class.forName( className ).getDeclaredMethods() ){
            if( Modifier.PUBLIC == method.getModifiers() ){
                for( Annotation annotation : method.getAnnotations() ){
                    final RequestControllerData data = RequestControllerData.builder()
                        .httpMethod( HTTP_METHOD_ENUM.valueOfByAnnotation( annotation ) )
                        .url( getValueFromAnnotation( annotation ) )
                        .controllerClass( className )
                        .controllerMethod( method.getName() )
                    .build();
                    data.setMethodParameters( extractParametersFromMethod( method ) );
                    data.setUrlSplits( extractUrlSplits( data.getUrl(), data.getMethodParameters() ) );
                    ControllerMap.put( data );
                }
            }
        }
    }

    private static List<SplitUrlControllerData> extractUrlSplits(final String url, 
    final List<ParameterMethodControllerData> parametersMethod){
        final List<SplitUrlControllerData> splitsData = new ArrayList<>();
        if( url.lastIndexOf( '/' ) > 0 ){ // tem mais de uma barra
            Arrays.stream( url.substring( 1 ).split( "/" ) )
                .forEach( path -> {
                    final SplitUrlControllerData data = SplitUrlControllerData.builder()
                        .path( path.replaceAll( "[{}]", "" ) )
                        .parameter( path.charAt( 0 ) == '{' )
                    .build();
                    if( data.isParameter() ){
                        final int indexParam = parametersMethod.indexOf( ParameterMethodControllerData.builder().paramName( data.getPath() ).build() );
                        if( indexParam > -1 ){
                            data.setTypeToMethod( parametersMethod.get( indexParam ).getParamClass() );
                            data.setIndexParameterMethod( indexParam );
                        }
                    }
                    splitsData.add( data );
                } );
        }
        return splitsData;
    }

    private static List<ParameterMethodControllerData> extractParametersFromMethod(final Method method){
        final List<ParameterMethodControllerData> parametersData = new ArrayList<>();
        Optional<Annotation> annotation;
        ParameterMethodControllerData paramData;
        for( Parameter parameter : method.getParameters() ){
            annotation = Arrays.stream( parameter.getAnnotations() )
                .filter( item -> item.annotationType().isAssignableFrom( WebFrameworkBody.class )
                    || item.annotationType().isAssignableFrom( WebFrameworkPathParameter.class )
                    || item.annotationType().isAssignableFrom( WebFrameworkRequestParameter.class )
                ).findFirst();
            if( annotation.isPresent() ){
                paramData = ParameterMethodControllerData.builder()
                    .paramClass( parameter.getType() )
                    .paramAnnotation( annotation.get() )
                    .build();
                if( annotation.get().annotationType().isAssignableFrom( WebFrameworkPathParameter.class ) )
                    paramData.setParamName( ( (WebFrameworkPathParameter)annotation.get() ).value() );
                else if( annotation.get().annotationType().isAssignableFrom( WebFrameworkRequestParameter.class ) )
                    paramData.setParamName( ( (WebFrameworkRequestParameter)annotation.get() ).value() );
                parametersData.add( paramData  );
            }
        }
        return Collections.unmodifiableList( parametersData );
    }

    private static String getValueFromAnnotation(final Annotation annotation){
        String url = null;
        final Optional<HTTP_METHOD_ENUM> httpMethod = Optional.ofNullable( 
            HTTP_METHOD_ENUM.valueOfByAnnotation( annotation ) );
        if( httpMethod.isPresent() ){
            if( httpMethod.get().isGet() )
                url = ( (WebFrameworkGetMethod)annotation ).value();
            else if( httpMethod.get().isPost() )
                url = ( (WebFrameworkPostMethod)annotation ).value();
            else if( httpMethod.get().isPut() )
                url = ( (WebFrameworkPutMethod)annotation ).value();
            else if( httpMethod.get().isDelete() )
                url = ( (WebFrameworkDeleteMethod)annotation ).value();
            if( url != null && !url.startsWith( "/" ) )
                url = "/" + url;
        }
        return url;
    }

}