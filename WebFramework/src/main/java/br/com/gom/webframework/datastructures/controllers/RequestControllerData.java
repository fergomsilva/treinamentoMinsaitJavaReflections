package br.com.gom.webframework.datastructures.controllers;

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
    private String controllerClass;
    private String controllerMethod;

    @Override
    public String toString(){
        return String.format( "    %s:%s [%s.%s]", this.getHttpMethod(), this.getUrl(), 
            this.getControllerClass(), this.getControllerMethod() );
    }

}