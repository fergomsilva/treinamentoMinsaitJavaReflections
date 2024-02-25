package br.com.gom.webframework.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import com.google.gson.Gson;

import br.com.gom.webframework.datastructures.ControllerInstance;
import br.com.gom.webframework.datastructures.ControllerMap;
import br.com.gom.webframework.datastructures.DependencyInjectionInstance;
import br.com.gom.webframework.datastructures.RequestControllerData;
import br.com.gom.webframework.datastructures.ServiceImplementationMap;
import br.com.gom.webframework.util.WebFrameworkLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class WebFrameworkDispatcherServlet extends HttpServlet{

    private static final String MODULO_LOG = "WebFrameworkDispatcherServlet";

    @Override
    protected void service(final HttpServletRequest req, final HttpServletResponse resp) 
    throws ServletException, IOException{
        // ignorar o favIcon
        if( req.getRequestURL().toString().endsWith( "favicon.ico" ) )
            return;

        final String url = req.getRequestURI();
        final String httpMethod = req.getMethod().toUpperCase();

        String key = ( httpMethod.toUpperCase() + url );

        // busca a informacao da classe; metodo; parametros... da requisicao
        final RequestControllerData data = ControllerMap.getByKey( key );

        WebFrameworkLogger.log( MODULO_LOG, "URL: %s (%s) - Handler: %s.%s", 
            url, httpMethod, data.getControllerClass(), data.getControllerMethod() );

        // verifica se existe uma instancia da classe correspondente, senao cria
        Object controller;
        WebFrameworkLogger.log( MODULO_LOG, "Procurar instancia da controladora" );
        try{
            try( final PrintWriter out = new PrintWriter( resp.getWriter() ); ){
                controller = ControllerInstance.getByKey( data.getControllerClass() );
                if( controller == null ){
                    WebFrameworkLogger.log( MODULO_LOG, "Criar nova instancia da controladora" );
                    controller = Class.forName( data.getControllerClass() ).getConstructor().newInstance();
                    ControllerInstance.put( data.getControllerClass(), controller );
                    this.injectDependencies( controller );
                }

                // extrair o metodo desta classe, ou seja o metodo que vai atender a requisicao.
                // executar o metodo e escrever a saida dele
                Method controllerMethod = null;
                for( Method method : controller.getClass().getMethods() ){
                    if( data.getControllerMethod().equals( method.getName() ) ){
                        controllerMethod = method;
                        break;
                    }
                }

                final Gson gson = new Gson();
                // metodo tem parametros???
                if( controllerMethod.getParameterCount() > 0 ){
                    WebFrameworkLogger.log( MODULO_LOG, "Metodo '%s' tem '%d' parametro(s)!", controllerMethod.getName(), 
                    controllerMethod.getParameterCount() );
                    Object arg;
                    final Parameter parameter = controllerMethod.getParameters()[ 0 ];
                    if( "br.com.gom.webframework.annotations.WebFrameworkBody".equals( parameter.getAnnotations()[ 0 ].annotationType().getName() ) ){
                        WebFrameworkLogger.log( MODULO_LOG, "\tProcurando parmetro da requisicao do tipo '%s' ", parameter.getType().getName() );
                        final String body = this.readBytesFromRequest( req );
                        WebFrameworkLogger.log( MODULO_LOG, "\tconteudo do parametro '%s' ", body );
                        arg = gson.fromJson( body, parameter.getType() );
                        WebFrameworkLogger.log( MODULO_LOG, "Invocar o metodo '%s', com parametro do tipo '%s' para requisicao.", 
                            controllerMethod.getName(), parameter.getType().toString() );
                        out.println( gson.toJson( controllerMethod.invoke( controller, arg ) ) );
                    }
                }else{
                    WebFrameworkLogger.log( MODULO_LOG, "Invocar o metodo '%s' para requisicao.", 
                        controllerMethod.getName() );
                    out.println( gson.toJson( controllerMethod.invoke( controller ) ) );
                }
                out.flush();
            }
        }catch( Exception e ){
            WebFrameworkLogger.error( MODULO_LOG, e, e.getMessage() );
        }
        
    }

    private void injectDependencies(final Object controller) throws Exception{
        // ver apenas os campos anotados por Inject
        for( Field attr : controller.getClass().getDeclaredFields() ){
            String attrTipo = attr.getType().getName();
            WebFrameworkLogger.log( MODULO_LOG, "Injetar '%s' do tipo '%s'", attr.getName(), attrTipo );
            Object serviceImpl;
            if( DependencyInjectionInstance.getByKey( attrTipo ) == null ){
                // tem declaracao da interface?
                String implType = ServiceImplementationMap.getByKey( attrTipo );
                WebFrameworkLogger.log( MODULO_LOG, "Procurar instancias de '%s'", implType );
                if( implType != null ){
                    WebFrameworkLogger.log( MODULO_LOG, "Injetar novo objeto" );
                    serviceImpl = DependencyInjectionInstance.getByKey( implType );
                    if( serviceImpl == null ){
                        serviceImpl = Class.forName( implType ).getDeclaredConstructor().newInstance();
                        DependencyInjectionInstance.put( implType, serviceImpl );
                    }
                    // atribuir essa instancia ao atributo anotado - INJECAO DE DEPENDENCIA
                    attr.setAccessible( true );
                    attr.set( controller, serviceImpl );
                    WebFrameworkLogger.log( MODULO_LOG, "Objeto injetado com sucesso!" );
                }
            }
        }
    }

    private String readBytesFromRequest(final HttpServletRequest req) throws IOException{
        final StringBuilder buffer = new StringBuilder();
        try( final BufferedReader reader = new BufferedReader( new InputStreamReader( req.getInputStream() ) ) ){
            String line;
            while( ( line = reader.readLine() ) != null ){
                buffer.append( line );
            }
        }
        return buffer.toString();
    }

}