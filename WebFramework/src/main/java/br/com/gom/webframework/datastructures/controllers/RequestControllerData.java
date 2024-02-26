package br.com.gom.webframework.datastructures.controllers;

import java.util.List;

import br.com.gom.webframework.enumerations.HTTP_METHOD_ENUM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestControllerData{
    
    private HTTP_METHOD_ENUM httpMethod;
    private String url;
    private List<SplitUrlControllerData> urlSplits;
    private String controllerClass;
    private String controllerMethod;
    private List<ParameterMethodControllerData> methodParameters;

    
    @Override
    public String toString(){
        return String.format( "\t(%6s) %s\t%s \t[%s.%s(%s)]", 
            this.getHttpMethod(), this.getUrl(), this.getUrlSplits(),
            this.getControllerClass(), this.getControllerMethod(), 
            ( this.getMethodParameters().isEmpty() ? "" : this.getMethodParameters() ) );
    }

}