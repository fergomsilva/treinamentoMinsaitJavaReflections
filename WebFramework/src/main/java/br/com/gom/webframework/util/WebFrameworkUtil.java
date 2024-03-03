package br.com.gom.webframework.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathVariable;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParam;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPutMethod;
import br.com.gom.webframework.datastructures.controllers.ParameterMethodControllerData;
import br.com.gom.webframework.datastructures.controllers.SplitUrlControllerData;
import br.com.gom.webframework.enumerations.HTTP_METHOD_ENUM;

/**
 * Classe utilitaria.
 */
public final class WebFrameworkUtil{

    /**
     * tipos primitivos numericos inteiros
     */
    private static final String INTEGER_PRIMITIVE_CLASSES = "byte|short|int|long";
    /**
     * tipos primitivos numericos decimais
     */
    private static final String DECIMAL_PRIMITIVE_CLASSES = "float|double";
    /**
     * tipos primitivos numericios inteiros e decimais
     */
    private static final String PRIMITIVE_CLASSES = INTEGER_PRIMITIVE_CLASSES + "|" + DECIMAL_PRIMITIVE_CLASSES;


    private WebFrameworkUtil(){
        super();
    }

    /**
     * Verifica se o parametro e um valor numerico.
     * @param value valor a ser verificado
     * @return retorna true caso o parametor seja numerico
     */
    private static final boolean isNumeric(final String value){
        if( value == null )
            return false;
        try{
            Double.parseDouble( value );
            return true;
        }catch( Exception e ){
            return false;
        }
    }

    /**
     * Extrair parametros no método do controller.
     * @param method método do controller.
     * @return lista de parametros do método do controller.
     * @see ParameterMethodControllerData
     * @see WebFrameworkBody
     * @see WebFrameworkPathVariable
     * @see WebFrameworkRequestParam
     */
    public static final List<ParameterMethodControllerData> extractParametersFromMethod(final Method method){
        final List<ParameterMethodControllerData> parametersData = new ArrayList<>();
        for( Parameter parameter : method.getParameters() ){
            Arrays.stream( parameter.getAnnotations() )
                .filter( item -> item.annotationType().isAssignableFrom( WebFrameworkBody.class )
                    || item.annotationType().isAssignableFrom( WebFrameworkPathVariable.class )
                    || item.annotationType().isAssignableFrom( WebFrameworkRequestParam.class ) )
                .findFirst() // para o mesmo parametro considera apenas a primeira anotação
                .ifPresentOrElse( 
                    // se existir alguma anotação no parametro vincula-se a classe, anotação e nome 
                    // configurado na anotação
                    ann -> parametersData.add( new ParameterMethodControllerData( parameter.getType(), 
                        ann, WebFrameworkUtil.getNameParameterFromAnnotation( ann ) ) ), 
                    // não existe anotação, apenas utiliza a classe
                    () -> parametersData.add( 
                        new ParameterMethodControllerData( parameter.getType() ) )
                );
        }
        return Collections.unmodifiableList( parametersData );
    }

    /**
     * Extrai as 'partes' da URI configurada nas requisições. <br>
     * @param url URI configurada nas requisições.
     * @param parametersMethod lista de parametros do método da controller da requisição.
     * @return lista de 'partes' da URI configurada nas requisições.
     */
    public static final List<SplitUrlControllerData> extractUrlSplits(final String url, 
    final List<ParameterMethodControllerData> parametersMethod){
        if( url.length() == 1 )
            return Arrays.asList( new SplitUrlControllerData( "" ) );
        final List<SplitUrlControllerData> splitsData = new ArrayList<>();
        // separa a URI nas partes utilizando o delimitador '/'
        Arrays.stream( url.substring( 1 ).split( "/" ) )
            .forEach( path -> {
                final SplitUrlControllerData data = new SplitUrlControllerData( 
                    path.replaceAll( "[{}]", "" ) );
                if( path.matches( "^[{]\\S*[}]$" ) ){
                    // se a 'parte' é uma variável tenta buscar o parametro correspondente no método.
                    final int indexParam = parametersMethod.indexOf( new ParameterMethodControllerData( data.getPath() ) );
                    if( indexParam > -1 ){
                        // se houver parametro correspondente será vinculada a classe para eventual conversão de valores.
                        data.setParamClassFromMethod( parametersMethod.get( indexParam ).getParamClass() );
                    }
                }
                splitsData.add( data );
            } );
        return splitsData;
    }

    /**
     * Diz se a classe corresponde algum tipo de número, inteiro ou decimal.
     * @param type classe para verificar se é de algum tipo numerico.
     * @return <b>true</b> se a classe é numerica
     */
    private static final boolean isNumericClass(final Class<?> type){
        if( type.isPrimitive() )
            return PRIMITIVE_CLASSES.contains( type.getName() );
        return ( type.getSuperclass() != null && Number.class.isAssignableFrom( type.getSuperclass() ) )
            || BigInteger.class.isAssignableFrom( type ) || BigDecimal.class.isAssignableFrom( type );
    }

    /**
     * Diz se a classe corresponde algum tipo de número inteiro.
     * @param type classe para verificar se é de algum tipo numerico inteiro.
     * @return <b>true</b> se a classe é numerica e de algum tipo inteiro
     */
    private static final boolean isIntegerClass(final Class<?> type){
        if( type.isPrimitive() )
            return INTEGER_PRIMITIVE_CLASSES.contains( type.getName() );
        if( ( type.getSuperclass() != null && Number.class.isAssignableFrom( type.getSuperclass() ) ) 
                || BigInteger.class.isAssignableFrom( type ) ){
            return Byte.class.isAssignableFrom( type ) || Short.class.isAssignableFrom( type ) 
                || Integer.class.isAssignableFrom( type ) || Long.class.isAssignableFrom( type ) 
                || BigInteger.class.isAssignableFrom( type );
        }
        return false;
    }

    /**
     * Diz se a classe corresponde algum tipo de número decimal.
     * @param type classe para verificar se é de algum tipo numerico decimal.
     * @return <b>true</b> se a classe é numerica e de algum tipo decimal
     */
    private static final boolean isDecimalClass(final Class<?> type){
        if( type.isPrimitive() )
            return DECIMAL_PRIMITIVE_CLASSES.contains( type.getName() );
        if( ( type.getSuperclass() != null && Number.class.isAssignableFrom( type.getSuperclass() ) ) 
                || BigDecimal.class.isAssignableFrom( type ) ){
            return Float.class.isAssignableFrom( type ) || Double.class.isAssignableFrom( type ) 
                || BigDecimal.class.isAssignableFrom( type );
        }
        return false;
    }

    /**
     * Gera a URI com regex nas 'partes' que são variáveis.
     * @param urlSplits 'partes' da URI configuração na requisição do método na controller.
     * @return URI com regex nas 'partes' que são variáveis
     */
    public static final String generateUrlRegex(final List<SplitUrlControllerData> urlSplits){
        // percorre todas as 'partes' e converte para regex as que são variáveis
        final String regex = String.join( "/", urlSplits.stream().map( item -> {
            if( !item.isParameter() )
                return item.getPath();
            else{
                // verifica se a classe e numerica
                if( isNumericClass( item.getParamClassFromMethod() ) ){
                    if( isDecimalClass( item.getParamClassFromMethod() ) )
                        // se a classe do parametro é decimal, considera este regex para a variável da URI
                        return "[-.0-9]*";
                    else if ( isIntegerClass( item.getParamClassFromMethod() ) )
                        // se a classe do parametro é inteiro, considera este regex para a variável da URI
                        return "\\d*";
                }
                // qualquer outra classe considera este regex para a variável da URI
                return "\\S*";
            }
        } ).toList() );
        // montagem final da URI com regex
        return "^/" + regex + " $";
    }

    /**
     * Converte o valor String para a classe esperada pelo parametro/argumento.
     * @param value valor em String
     * @param valueClass classe esperada pelo parametro/argumento.
     * @return valor convertido para a classe esperada.
     * @throws Exception exceção ocorrida na conversão do valor.
     */
    @SuppressWarnings( { "all" } )
    public static final Object convertStringToValue(final String value, final Class<?> valueClass) throws Exception{
        if( value != null && isNumeric( value ) ){
            if( Byte.class.isAssignableFrom( valueClass ) || "byte".equals( valueClass.getName() ) )
                return Byte.parseByte( value );
            else if( Short.class.isAssignableFrom( valueClass ) || "short".equals( valueClass.getName() ) )
                return Short.parseShort( value );
            else if( Integer.class.isAssignableFrom( valueClass ) || "int".equals( valueClass.getName() ) )
                return Integer.parseInt( value );
            else if( Long.class.isAssignableFrom( valueClass ) || "long".equals( valueClass.getName() ) )
                return Long.parseLong( value );
            else if( BigInteger.class.isAssignableFrom( valueClass ) )
                return new BigInteger( value );
            else if( Float.class.isAssignableFrom( valueClass ) || "float".equals( valueClass.getName() ) )
                return Float.parseFloat( value );
            else if( Double.class.isAssignableFrom( valueClass ) || "double".equals( valueClass.getName() ) )
                return Double.parseDouble( value );
            else if( BigDecimal.class.isAssignableFrom( valueClass ) )
                return new BigDecimal( value );
        }else
            WebFrameworkLogger.log( "WebFrameworkUtil", "Valor informado eh null." );
        return value;
    }

    /**
     * Retorna o valor da URI configurada na anotação.
     * @param annotation anotação para pegar a URI configurada da requisição.
     * @return valor da URI configurada na anotação.
     * @see WebFrameworkGetMethod
     * @see WebFrameworkPostMethod
     * @see WebFrameworkPutMethod
     * @see WebFrameworkDeleteMethod
     */
    public static final String getUrlValueFromAnnotation(final Annotation annotation){
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

    /**
     * Nome da variável/parametro configurado na anotação.
     * @param annotation anotação de variavel ou query parameter.
     * @return nome da variável/parametro correspondente a URI do método controller.
     * @see WebFrameworkPathVariable
     * @see WebFrameworkRequestParam
     */
    public static final String getNameParameterFromAnnotation(final Annotation annotation){
        if( annotation != null ){
            if( annotation.annotationType().isAssignableFrom( WebFrameworkPathVariable.class ) )
                return ( (WebFrameworkPathVariable)annotation ).value();
            else if( annotation.annotationType().isAssignableFrom( WebFrameworkRequestParam.class ) )
                return ( (WebFrameworkRequestParam)annotation ).value();
        }
        return null;
    }
    
}