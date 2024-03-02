package br.com.gom.webframework.annotations.datarequests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.PARAMETER )
public @interface WebFrameworkRequestParam{
    
    /**
     * @return nome do query parameter informado após a URL da requisição.
     */
    String value();

}