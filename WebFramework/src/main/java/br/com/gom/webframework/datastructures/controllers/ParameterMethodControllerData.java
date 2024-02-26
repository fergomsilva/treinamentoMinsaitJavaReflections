package br.com.gom.webframework.datastructures.controllers;

import java.lang.annotation.Annotation;

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

    
    @Override
    public String toString(){
        return String.format( "Class[%s];Annotation[%s];Name[%s]", 
            this.getParamClass(), this.getParamAnnotation(), this.getParamName() );
    }

}