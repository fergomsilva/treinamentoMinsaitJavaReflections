package br.com.gom.webframework.web;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import br.com.gom.webframework.annotations.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.WebFrameworkPostMethod;
import br.com.gom.webframework.datastructures.ControllerMap;
import br.com.gom.webframework.datastructures.RequestControllerData;
import br.com.gom.webframework.datastructures.ServiceImplementationMap;
import br.com.gom.webframework.explorer.ClassExplorer;
import br.com.gom.webframework.util.WebFrameworkLogger;


public class WebFrameworkWebApplication{
    private static final String LOG_MODULO = "Embeded Web Container";
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

            final Tomcat tomcat = new Tomcat();
            final Connector connector = new Connector();
            connector.setPort( 8080 );
            tomcat.setConnector( connector );
            WebFrameworkLogger.log( LOG_MODULO, "Iniciando na porta 8080!" );
            
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
                    if( classAnnotation.annotationType().getName().equals( "br.com.gom.webframework.annotations.WebFrameworkController" ) ){
                        WebFrameworkLogger.log( LOG_MODULO_METADATA, "Found a controller %s", classe );
                        extractMethods( classe );
                    }else if( classAnnotation.annotationType().getName().equals( "br.com.gom.webframework.annotations.WebFrameworkService" ) ){
                        WebFrameworkLogger.log( LOG_MODULO_METADATA, "Found a service Implementation %s", classe );
                        for( Class<?> interfaceWeb : Class.forName( classe ).getInterfaces() ){
                            WebFrameworkLogger.log( LOG_MODULO_METADATA, "Class implements %s", interfaceWeb.getName() );
                            ServiceImplementationMap.put( interfaceWeb.getName(), classe );
                        }
                    }
                }
            }
            for( RequestControllerData item : ControllerMap.listValues() ){
                WebFrameworkLogger.log( "", item.toString() );
            }
        }catch( Exception e ){
            WebFrameworkLogger.error( LOG_MODULO_METADATA, e, e.getLocalizedMessage() );
        }
    }

    private static void  extractMethods(final String className) throws Exception{
        String path = "";
        String httpMethod = "";

        // recuperar todos os métodos da classe
        for( Method method : Class.forName( className ).getDeclaredMethods() ){
            for( Annotation annotation : method.getAnnotations() ){
                if( annotation.annotationType().getName().equals( "br.com.gom.webframework.annotations.WebFrameworkGetMethod" ) ){
                    httpMethod = "GET";
                    path = ( (WebFrameworkGetMethod)annotation ).value();
                }else if( annotation.annotationType().getName().equals( "br.com.gom.webframework.annotations.WebFrameworkPostMethod" ) ){
                    httpMethod = "POST";
                    path = ( (WebFrameworkPostMethod)annotation ).value();
                }
                RequestControllerData getData = new RequestControllerData( httpMethod, path, className, method.getName() );
                ControllerMap.put( ( httpMethod + path ), getData );
            }
        }

    }

}