package br.com.gom.webframework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.gom.webframework.datastructures.MethodParam;


public class WebFrameworkUtil{

    private WebFrameworkUtil(){
        super();
    }
    
    public static MethodParam convertUri2MethodParam(final String requestURI){
        final Pattern pattern = Pattern.compile( "([^/]+)(?:/([^/]+))?" );
        final Matcher mather = pattern.matcher( requestURI );
        if( mather.find() ){
            final String method = mather.group( 1 );
            final String param = mather.group( 2 );
            final MethodParam methodParam = MethodParam.builder()
                .method( "/" + method ).build();
            if( param != null )
                methodParam.setParam( param );
            return methodParam;
        }else{
            WebFrameworkLogger.log( "WebFrameworkUtil", 
                "A URI nao corresponde ao padrão esperado." );
        }
        return null;
    }

    public static Object convert2Type(String value, Class<?> type){
        if( value == null ){
            WebFrameworkLogger.log( "WebFrameworkUtil", 
                "Parametro informado e null." );
            value = "";
        }
        try{
            if( String.class.isAssignableFrom( type ) )
                return value;
            else if( Integer.class.isAssignableFrom( type ) || "int".equals( type.getName() ) ){
                if( isNumeric( value ) )
                    return Integer.parseInt( value );
                return 0;
            }else if( Double.class.isAssignableFrom( type ) || "double".equals( type.getName() ) ){
                if( isNumeric( value ) )
                    return Double.parseDouble( value );
                return 0.0;
            }
            return null;
        }catch( Exception e ){
            WebFrameworkLogger.error( "WebFrameworkUtil", e, 
                "Erro ao converter parmetro em objeto: %s", e.getMessage() );
            if( String.class.isAssignableFrom( type ) )
                return "";
            else if( Integer.class.isAssignableFrom( type ) || "int".equals( type.getName() ) )
                return 0;
            else if( Double.class.isAssignableFrom( type ) || "double".equals( type.getName() ) )
                return 0.0;
            return null;
        }
    }

    /**
     * Verifica se o parametro e um valor numerico.
     * @param value valor a ser verificado
     * @return retorna true caso o parametor seja numerico
     */
    private static boolean isNumeric(final String value){
        if( value == null )
            return false;
        try{
            Double.parseDouble( value );
            return true;
        }catch( Exception e ){
            return false;
        }
    }

}