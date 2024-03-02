package br.com.gom.webframework.annotations.datarequests;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.PARAMETER )
public @interface WebFrameworkPathVariable{
    
    /**
     * @return nome da variavel na URL da requisição.
     */
    String value();
    
}