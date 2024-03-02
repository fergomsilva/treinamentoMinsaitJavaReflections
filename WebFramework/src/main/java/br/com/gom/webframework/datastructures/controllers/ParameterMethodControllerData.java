package br.com.gom.webframework.datastructures.controllers;

import java.lang.annotation.Annotation;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathVariable;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * Bean que mantém os dados a respeito de cada parametro/argumento do método no controller.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of={ "paramName" } )
public class ParameterMethodControllerData{
    
    /**
     * É a classe do parametro/argumento do método no controller.
     */
    private Class<?> paramClass;
    /**
     * É a anotação do parametro/argumento do método no controller.
     * @see WebFrameworkBody
     * @see WebFrameworkPathVariable
     * @see WebFrameworkRequestParam
     */
    private Annotation paramAnnotation;
    /**
     * É o nome correspondente a variavel da URI..
     * @see WebFrameworkPathVariable
     * @see WebFrameworkRequestParam
     */
    private @Builder.Default String paramName = "";

    
    /**
     * Diz se a anotação vinculada ao parametro/argumento é {@link WebFrameworkBody @WebFrameworkBody}
     * @return <b>true</b> se a anotação é {@link WebFrameworkBody @WebFrameworkBody}
     */
    public boolean isBodyParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkBody.class );
    }

    /**
     * Diz se a anotação vinculada ao parametro/argumento é {@link WebFrameworkPathVariable @WebFrameworkPathVariable}
     * @return <b>true</b> se a anotação é {@link WebFrameworkPathVariable @WebFrameworkPathVariable}
     */
    public boolean isPathParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkPathVariable.class );
    }

    /**
     * Diz se a anotação vinculada ao parametro/argumento é {@link WebFrameworkRequestParam @WebFrameworkRequestParam}
     * @return <b>true</b> se a anotação é {@link WebFrameworkRequestParam @WebFrameworkRequestParam}
     */
    public boolean isRequestParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkRequestParam.class );
    }
    
    @Override
    public String toString(){
        return String.format( "Class[%s];Annotation[%s];Name[%s]", 
            this.getParamClass(), this.getParamAnnotation(), this.getParamName() );
    }

}