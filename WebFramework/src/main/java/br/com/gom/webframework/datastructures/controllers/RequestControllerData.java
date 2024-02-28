package br.com.gom.webframework.datastructures.controllers;

import java.util.List;

import br.com.gom.webframework.enumerations.HTTP_METHOD_ENUM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestControllerData{
    
    private HTTP_METHOD_ENUM httpMethod;
    private String url;
    private boolean staticUrl;
    private String urlRegex;
    private List<SplitUrlControllerData> urlSplits;
    private String controllerClass;
    private String controllerMethod;
    private List<ParameterMethodControllerData> methodParameters;

    
    @Override
    public String toString(){
        if( !this.isStaticUrl() )
            return String.format( "\t(%6s) %s [%s] [%s.%s(%d)]", 
                this.getHttpMethod(), this.getUrl(), this.getUrlRegex(), this.getControllerClass(), 
                this.getControllerMethod(), this.getMethodParameters().size() );
        return String.format( "\t(%6s) %s [%s.%s()]", this.getHttpMethod(), 
            this.getUrl(), this.getControllerClass(), this.getControllerMethod() );
    }

}