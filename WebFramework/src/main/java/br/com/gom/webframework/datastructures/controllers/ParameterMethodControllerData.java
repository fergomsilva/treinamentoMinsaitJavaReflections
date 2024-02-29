package br.com.gom.webframework.datastructures.controllers;

import java.lang.annotation.Annotation;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathParameter;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of={ "paramName" } )
public class ParameterMethodControllerData{
    
    private Class<?> paramClass;
    private Annotation paramAnnotation;
    private @Builder.Default String paramName = "";

    
    public boolean isBodyParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkBody.class );
    }

    public boolean isPathParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkPathParameter.class );
    }

    public boolean isRequestParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkRequestParameter.class );
    }
    
    @Override
    public String toString(){
        return String.format( "Class[%s];Annotation[%s];Name[%s]", 
            this.getParamClass(), this.getParamAnnotation(), this.getParamName() );
    }

}