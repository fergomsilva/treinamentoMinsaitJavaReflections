package br.com.gom.webframework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME ) // anotacao em tempo de execucao
@Target( ElementType.TYPE ) // qual target pode receber essa anotacao aplicando a uma classe
public @interface WebFrameworkController{
    
}