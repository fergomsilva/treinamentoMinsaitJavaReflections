package br.com.gom.webframework.datastructures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestControllerData{
    
    private String httpMethod;
    private String url;
    private String controllerClass;
    private String controllerMethod;
    private @Builder.Default String parameter = "";

    @Override
    public String toString(){
        return String.format( "    %s:%s [%s.%s]%s", this.getHttpMethod(), this.getUrl(), 
            this.getControllerClass(), this.getControllerMethod(), 
            ( !this.getParameter().isEmpty() ? ( " - Expected parameter " + this.getParameter() ) 
                : "" ) );
    }

}