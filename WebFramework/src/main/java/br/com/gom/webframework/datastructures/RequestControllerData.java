package br.com.gom.webframework.datastructures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestControllerData{
    
    private String httpMethod;
    private String url;
    private String controllerClass;
    private String controllerMethod;

    @Override
    public String toString(){
        return String.format( "    %s:%s [%s.%s]", this.getHttpMethod(), this.getUrl(), 
            this.getControllerClass(), this.getControllerMethod() );
    }

}