package br.com.gom.webframework.datastructures.controllers;

import java.util.List;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParam;
import br.com.gom.webframework.enumerations.HTTP_METHOD_ENUM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * Bean que mantém os dados do mapeamento de cada requisição anotada nos métodos dos controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of={ "httpMethod", "url" } )
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
    private @Accessors( fluent=true ) boolean hasBodyParameterAnnotation;
    /**
     * Diz se existe algum parametro/argumento no método do controller anotado com {@link WebFrameworkRequestParam @WebFrameworkRequestParam}.
     */
    private @Accessors( fluent=true ) boolean hasRequestParamAnnotation;

    
    @Override
    public String toString(){
        return String.format( "\t(%6s) %-10s\t[%s.%s]", 
            this.getHttpMethod(), ( this.getUrl() + ( this.hasRequestParamAnnotation() ? "?..." : "" ) ),
            this.getControllerClass(), this.getControllerMethod() );
    }

}