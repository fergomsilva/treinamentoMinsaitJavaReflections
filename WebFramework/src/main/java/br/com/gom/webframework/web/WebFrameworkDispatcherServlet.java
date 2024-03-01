package br.com.gom.webframework.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathParameter;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParameter;
import br.com.gom.webframework.datastructures.InterfaceImplementationMap;
import br.com.gom.webframework.datastructures.controllers.ControllerInstance;
import br.com.gom.webframework.datastructures.controllers.ControllerMap;
import br.com.gom.webframework.datastructures.controllers.ParameterMethodControllerData;
import br.com.gom.webframework.datastructures.controllers.RequestControllerData;
import br.com.gom.webframework.datastructures.injections.DependencyInjectionInstance;
import br.com.gom.webframework.util.WebFrameworkLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class WebFrameworkDispatcherServlet extends HttpServlet{

    private static final Gson GSON = new Gson();
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

        try{
            final Object controller = getController( data.getControllerClass() );
            final Method controllerMethod = findMethodFromController( data, controller );
            if( controllerMethod != null ){
                // metodo tem parametros???
                if( data.getMethodParameters() != null && !data.getMethodParameters().isEmpty() )
                    this.writeReturnOut( this.invokeMethodWithParameters( controllerMethod, controller, 
                        url, data, req ), resp );
                else{
                    WebFrameworkLogger.log( MODULO_LOG, "Invocar o metodo '%s' para requisicao.\n", 
                        controllerMethod.getName() );
                    this.writeReturnOut( controllerMethod.invoke( controller ), resp );
                }
            }else{
                WebFrameworkLogger.log( MODULO_LOG, "Metodo '%s.%s' nao encontrado ou nao acessivel!\n", 
                    data.getControllerClass(), data.getControllerMethod() );
                resp.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
            }
        }catch( Exception e ){
            WebFrameworkLogger.error( MODULO_LOG, e, e.getMessage() );
        }
    }

    private Object getController(final String controllerClass) throws InstantiationException, IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
        // verifica se existe uma instancia da classe correspondente, senao cria
        WebFrameworkLogger.log( MODULO_LOG, "Procurar instancia da controladora" );
        Object controller = ControllerInstance.getByKey( controllerClass );
        if( controller == null ){
            WebFrameworkLogger.log( MODULO_LOG, "Criar nova instancia da controladora" );
            controller = Class.forName( controllerClass ).getConstructor().newInstance();
            ControllerInstance.put( controllerClass, controller );
            this.injectDependencies( controller );
        }
        return controller;
    }

    private Object invokeMethodWithParameters(final Method controllerMethod, final Object controller, 
        final String url, final RequestControllerData data, final HttpServletRequest req) 
    throws JsonSyntaxException, IOException, IllegalAccessException, InvocationTargetException{
        WebFrameworkLogger.log( MODULO_LOG, "Metodo '%s' tem '%d' parametro(s)!", controllerMethod.getName(), 
            controllerMethod.getParameterCount() );
        
        final Map<String, Object> mapaValores = extractValuesFromRequest( url, req.getParameterMap(), data );
        final Object[] valuesToMethod = new Object[ data.getMethodParameters().size() ];
        
        final AtomicInteger index = new AtomicInteger( 0 );
        WebFrameworkLogger.log( MODULO_LOG, "\tMontando array com '%d' argumento(s) para passar na chamada do metodo.", 
            valuesToMethod.length );
        for( ParameterMethodControllerData paramMethodData : data.getMethodParameters() ){
            if( paramMethodData.getParamAnnotation().annotationType().isAssignableFrom( WebFrameworkBody.class ) )
                valuesToMethod[ index.getAndIncrement() ] = GSON.fromJson( this.readBytesFromRequest( req ), 
                    paramMethodData.getParamClass() );
            else if( paramMethodData.getParamAnnotation().annotationType().isAssignableFrom( WebFrameworkPathParameter.class ) )
                valuesToMethod[ index.getAndIncrement() ] = mapaValores.get( paramMethodData.getParamName() );
            else if( paramMethodData.getParamAnnotation().annotationType().isAssignableFrom( WebFrameworkRequestParameter.class ) )
                valuesToMethod[ index.getAndIncrement() ] = mapaValores.get( "query-" + paramMethodData.getParamName() );
            else
                valuesToMethod[ index.getAndIncrement() ] = null;
        }
        WebFrameworkLogger.log( MODULO_LOG, "Invocar o metodo '%s' com argumentos para requisicao.\n", 
            controllerMethod.getName() );
        return controllerMethod.invoke( controller, valuesToMethod );
    }

    private void writeReturnOut(final Object returnValue, final HttpServletResponse resp) throws IOException{
        if( returnValue == null )
            return;
        try( final PrintWriter out = new PrintWriter( resp.getWriter() ) ){
            out.println( GSON.toJson( returnValue ) );
            out.flush();
        }
    }

    @SuppressWarnings( { "all" } )
    private void injectDependencies(final Object mainObj) throws InstantiationException, IllegalAccessException, 
    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException{
        // ver apenas os campos anotados por Inject
        String attrTipo = null;
        String implType = null;
        Object classImpl = null;
        for( Field attr : mainObj.getClass().getDeclaredFields() ){
            attrTipo = attr.getType().getName();
            if( DependencyInjectionInstance.getByKey( attrTipo ) == null ){
                WebFrameworkLogger.log( MODULO_LOG, "Injetar '%s' do tipo '%s'", attr.getName(), attrTipo );
                // tem declaracao da interface?
                implType = InterfaceImplementationMap.getByKey( attrTipo );
                WebFrameworkLogger.log( MODULO_LOG, "Procurar instancias de '%s'", implType );
                if( implType != null ){
                    WebFrameworkLogger.log( MODULO_LOG, "Injetar novo objeto" );
                    classImpl = DependencyInjectionInstance.getByKey( implType );
                    if( classImpl == null ){
                        classImpl = Class.forName( implType ).getDeclaredConstructor().newInstance();
                        DependencyInjectionInstance.put( implType, classImpl );
                    }
                    // atribuir essa instancia ao atributo anotado - INJECAO DE DEPENDENCIA
                    attr.setAccessible( true );
                    attr.set( mainObj, classImpl );
                    WebFrameworkLogger.log( MODULO_LOG, "Objeto injetado com sucesso!" );
                    WebFrameworkLogger.log( MODULO_LOG, "Verificar injecoes da implementacao %s.", 
                        classImpl.getClass().getName() );
                    this.injectDependencies( classImpl );
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

    private Method findMethodFromController(final RequestControllerData data, final Object controller) 
    throws NoSuchMethodException, SecurityException{
        // extrair o metodo desta classe, ou seja o metodo que vai atender a requisicao.
        if( data.getMethodParameters() == null || data.getMethodParameters().isEmpty() )
            return controller.getClass().getMethod( data.getControllerMethod() );
        return controller.getClass().getMethod( data.getControllerMethod(), 
            data.getMethodParameters().stream()
                .map( item -> item.getParamClass() )
                .toList().toArray( new Class<?>[0] ) );
    }

    private Map<String, Object> extractValuesFromRequest(final String url, final Map<String, String[]> queryParameters, 
    final RequestControllerData data){
        WebFrameworkLogger.log( MODULO_LOG, "\tExtraindo valores dos parametros da requisicao." );
        final Map<String, Object> mapaValores = new HashMap<>();
        if( !data.isStaticUrl() ){
            final String[] urlSplits = url.substring( 1 ).split( "/" );
            final AtomicInteger index = new AtomicInteger( 0 );
            data.getUrlSplits().stream().forEach( item -> {
                final int i = index.getAndIncrement();
                if( item.isParameter() )
                    mapaValores.put( item.getPath(), convertStringToValue( urlSplits[ i ], 
                        item.getParamClassFromMethod() ) );
            } );
        }
        if( data.hasRequestParameterAnnotation() && queryParameters != null && !queryParameters.isEmpty() ){
            data.getMethodParameters().stream()
                .filter( item -> item.isRequestParameterAnnotation() && queryParameters.containsKey( item.getParamName() ) )
                .forEach( item -> {
                    final String[] values = queryParameters.get( item.getParamName() );
                    mapaValores.put( "query-" + item.getParamName(), 
                        convertStringToValue( ( ( values != null && values.length > 0 ) ? values[ 0 ] : null ), 
                        item.getParamClass() ) );
                } );
        }
        return mapaValores;
    }

    private Object convertStringToValue(final String value, final Class<?> valueClass){
        if( value != null && !value.strip().isEmpty() ){
            if( Byte.class.isAssignableFrom( valueClass ) )
                return Byte.valueOf( value );
            else if( Integer.class.isAssignableFrom( valueClass ) )
                return Integer.valueOf( value );
            else if( Long.class.isAssignableFrom( valueClass ) )
                return Long.valueOf( value );
            else if( BigInteger.class.isAssignableFrom( valueClass ) )
                return new BigInteger( value );
            else if( Float.class.isAssignableFrom( valueClass ) )
                return Float.valueOf( value );
            else if( Double.class.isAssignableFrom( valueClass ) )
                return Double.valueOf( value );
            else if( BigDecimal.class.isAssignableFrom( valueClass ) )
                return new BigDecimal( value );
        }
        return value;
    }

}