package br.com.gom.webframework.datastructures.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParam;
import br.com.gom.webframework.enumerations.HTTP_METHOD_ENUM;


/**
 * Bean que mantém os dados do mapeamento de cada requisição anotada nos métodos dos controllers.
 */
public class RequestControllerData{
    
    /**
     * Método HTTP da requisição anotada.
     * @see HTTP_METHOD_ENUM
     */
    private HTTP_METHOD_ENUM httpMethod;
    /**
     * URL configurada no método da requisição no controller.
     */
    private String url;
    /**
     * Diz se a URL é estatica ou possui variáveis na URL.
     */
    private boolean staticUrl;
    /**
     * URL adapatada como regex para as situações que a configuração da requisição possui variaveis.
     */
    private String urlRegex;
    /**
     * As 'partes' da URL configurada na requisição do controller.
     * @see SplitUrlControllerData
     */
    private List<SplitUrlControllerData> urlSplits;
    /**
     * Nome da classe controller na qual a requisição está configurada.
     */
    private String controllerClass;
    /**
     * Nome do método no controller na qual a requisição está configurada.
     */
    private String controllerMethod;
    /**
     * Todos os parametros/argumentos existentes no método do controller.
     */
    private List<ParameterMethodControllerData> methodParameters;
    /**
     * Diz se existe algum parametro/argumento no método do controller anotado com {@link WebFrameworkBody @WebFrameworkBody}.
     */
    private boolean hasBodyParameterAnnotation;
    /**
     * Diz se existe algum parametro/argumento no método do controller anotado com {@link WebFrameworkRequestParam @WebFrameworkRequestParam}.
     */
    private boolean hasRequestParamAnnotation;

    
    public HTTP_METHOD_ENUM getHttpMethod(){
        return httpMethod;
    }
    public void setHttpMethod(final HTTP_METHOD_ENUM httpMethod){
        this.httpMethod = httpMethod;
    }

    public String getUrl(){
        return url;
    }
    public void setUrl(final String url){
        this.url = url;
    }

    public boolean isStaticUrl(){
        return staticUrl;
    }
    public void setStaticUrl(final boolean staticUrl){
        this.staticUrl = staticUrl;
    }

    public String getUrlRegex(){
        return urlRegex;
    }
    public void setUrlRegex(final String urlRegex){
        this.urlRegex = urlRegex;
    }

    public List<SplitUrlControllerData> getUrlSplits(){
        if( this.urlSplits == null )
            this.urlSplits = new ArrayList<>();
        return this.urlSplits;
    }
    public void setUrlSplits(final List<SplitUrlControllerData> urlSplits){
        this.urlSplits = urlSplits;
    }

    public String getControllerClass(){
        return controllerClass;
    }
    public void setControllerClass(final String controllerClass){
        this.controllerClass = controllerClass;
    }

    public String getControllerMethod(){
        return controllerMethod;
    }
    public void setControllerMethod(final String controllerMethod){
        this.controllerMethod = controllerMethod;
    }

    public List<ParameterMethodControllerData> getMethodParameters(){
        if( this.methodParameters == null )
            this.methodParameters = new ArrayList<>();
        return this.methodParameters;
    }
    public void setMethodParameters(final List<ParameterMethodControllerData> methodParameters){
        this.methodParameters = methodParameters;
    }

    public boolean hasBodyParameterAnnotation(){
        return hasBodyParameterAnnotation;
    }
    public void setHasBodyParameterAnnotation(final boolean hasBodyParameterAnnotation){
        this.hasBodyParameterAnnotation = hasBodyParameterAnnotation;
    }

    public boolean hasRequestParamAnnotation(){
        return hasRequestParamAnnotation;
    }
    public void setHasRequestParamAnnotation(final boolean hasRequestParamAnnotation) {
        this.hasRequestParamAnnotation = hasRequestParamAnnotation;
    }

    @Override
    public boolean equals(final Object obj){
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( this.getClass() == obj.getClass() )
            return false;
        final RequestControllerData other = (RequestControllerData)obj;
        return ( this.getHttpMethod() == other.getHttpMethod() ) 
            && ( this.getUrl().equals( other.getUrl() ) );
    }

    @Override
    public int hashCode(){
        return Objects.hash( this.getHttpMethod(), this.getUrl() );
    }

    @Override
    public String toString(){
        return String.format( "\t(%6s) %-10s\t[%s.%s]", 
            this.getHttpMethod(), ( this.getUrl() + ( this.hasRequestParamAnnotation() ? "?..." : "" ) ),
            this.getControllerClass(), this.getControllerMethod() );
    }

}