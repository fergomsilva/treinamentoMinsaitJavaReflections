package br.com.gom.webframework.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathParameter;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParameter;
import br.com.gom.webframework.datastructures.controllers.ControllerInstance;
import br.com.gom.webframework.datastructures.controllers.ControllerMap;
import br.com.gom.webframework.datastructures.controllers.RequestControllerData;
import br.com.gom.webframework.datastructures.injections.DependencyInjectionInstance;
import br.com.gom.webframework.datastructures.injections.ServiceImplementationMap;
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

        // busca a informacao da classe; metodo; parametros... da requisicao
        final RequestControllerData data = ControllerMap.find( httpMethod.toUpperCase(), url );
        if( data == null ){
            WebFrameworkLogger.log( MODULO_LOG, "Nao encontrada informacoes de request: %s (%s)", 
                url, httpMethod );
            resp.setStatus( HttpServletResponse.SC_NOT_FOUND );
            return;
        }

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
                if( data.getMethodParameters() == null || data.getMethodParameters().isEmpty() )
                    controllerMethod = controller.getClass().getMethod( data.getControllerMethod() );
                else
                    controllerMethod = controller.getClass().getMethod( data.getControllerMethod(), 
                        data.getMethodParameters().stream()
                            .map( item -> item.getParamClass() )
                            .toList().toArray( new Class<?>[0] )
                    );
                
                if( controllerMethod != null ){
                    final Gson gson = new Gson();
                    if( data.getMethodParameters() != null && !data.getMethodParameters().isEmpty() ){ // metodo tem parametros???
                        WebFrameworkLogger.log( MODULO_LOG, "Metodo '%s' tem '%d' parametro(s)!", controllerMethod.getName(), 
                            controllerMethod.getParameterCount() );
                        
                        if( !data.isStaticUrl() ){
                            final String[] urlSplits = url.substring( 1 ).split( "/" );
                            final Map<String, Object> mapaValores = new HashMap<>( urlSplits.length );

                            final AtomicInteger index = new AtomicInteger( 0 );
                            data.getUrlSplits().stream()
                                .forEach( item -> {
                                    final int i = index.getAndIncrement();
                                    if( item.isParameter() )
                                        mapaValores.put( item.getPath(), urlSplits[ i ] );
                                    item.getTypeToMethod();
                                } );
                            
                            index.set( 0 );
                            final Object[] valores = new Object[ data.getMethodParameters().size() ];
                            data.getMethodParameters().stream()
                                .forEach( item -> {
                                    if( item.getParamAnnotation().annotationType().isAssignableFrom( WebFrameworkBody.class ) ){
                                    }else if( item.getParamAnnotation().annotationType().isAssignableFrom( WebFrameworkPathParameter.class ) ){
                                        valores[ index.getAndIncrement() ] = mapaValores.get( item.getParamName() );
                                    }else if( item.getParamAnnotation().annotationType().isAssignableFrom( WebFrameworkRequestParameter.class ) ){
                                        // quando tem ?
                                        // RequestParameter  ?id=abc
                                        // System.out.println( "03: " + req.getParameterMap() );
                                        // System.out.println( "04: " + req.getQueryString() );
                                        final String[] paramsValues = req.getParameterValues( item.getParamName() );
                                        if( paramsValues != null && paramsValues.length > 0 )
                                            valores[ index.getAndIncrement() ] = paramsValues[ 0 ];
                                    }else
                                        valores[ index.getAndIncrement() ] = null;
                                } );
                            System.out.println( " ARGUMENTOS " + Arrays.stream( valores ).toList() );
                        }


                        Object arg;
                        final Parameter parameter = controllerMethod.getParameters()[ 0 ];
                        if( parameter.getAnnotations()[ 0 ].annotationType().isAssignableFrom( WebFrameworkBody.class ) ){
                            WebFrameworkLogger.log( MODULO_LOG, "\tProcurando parmetro da requisicao do tipo '%s' ", parameter.getType().getName() );
                            final String body = this.readBytesFromRequest( req );
                            WebFrameworkLogger.log( MODULO_LOG, "\tconteudo do parametro '%s' ", body );
                                arg = gson.fromJson( body, parameter.getType() );
                            WebFrameworkLogger.log( MODULO_LOG, "Invocar o metodo '%s', com parametro do tipo '%s' para requisicao.", 
                                controllerMethod.getName(), parameter.getType().toString() );
                            out.println( gson.toJson( controllerMethod.invoke( controller, arg ) ) );
                            out.flush();
                        }
                    }else{
                        WebFrameworkLogger.log( MODULO_LOG, "Invocar o metodo '%s' para requisicao.", 
                            controllerMethod.getName() );
                        out.println( gson.toJson( controllerMethod.invoke( controller ) ) );
                        out.flush();
                    }
                }else{
                    WebFrameworkLogger.log( MODULO_LOG, "Metodo '%s.%s' nao encontrado ou nao acessivel!", 
                        data.getControllerClass(), data.getControllerMethod() );
                    resp.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
                }
            }
        }catch( Exception e ){
            WebFrameworkLogger.error( MODULO_LOG, e, e.getMessage() );
        }
    }

    @SuppressWarnings( { "all" } )
    private void injectDependencies(final Object mainObj) throws InstantiationException, IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
        // ver apenas os campos anotados por Inject
        String attrTipo = null;
        String implType = null;
        Object serviceImpl = null;
        for( Field attr : mainObj.getClass().getDeclaredFields() ){
            attrTipo = attr.getType().getName();
            if( DependencyInjectionInstance.getByKey( attrTipo ) == null ){
                WebFrameworkLogger.log( MODULO_LOG, "Injetar '%s' do tipo '%s'", attr.getName(), attrTipo );
                // tem declaracao da interface?
                implType = ServiceImplementationMap.getByKey( attrTipo );
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
                    attr.set( mainObj, serviceImpl );
                    WebFrameworkLogger.log( MODULO_LOG, "Objeto injetado com sucesso!" );
                    WebFrameworkLogger.log( MODULO_LOG, "Verificar injecoes da implementacao %s.", 
                        serviceImpl.getClass().getName() );
                    this.injectDependencies( serviceImpl );
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