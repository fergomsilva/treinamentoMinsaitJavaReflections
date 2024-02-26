package br.com.gom.webframework.enumerations;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import br.com.gom.webframework.annotations.httpmethods.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPutMethod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor( access=AccessLevel.PRIVATE )
public enum HTTP_METHOD_ENUM{

    GET( WebFrameworkGetMethod.class ), 
    POST( WebFrameworkPostMethod.class ), 
    PUT( WebFrameworkPutMethod.class ), 
    DELETE( WebFrameworkDeleteMethod.class );


    private Class<? extends Annotation> classAnnotation;
    public boolean isGet(){
        return this.classAnnotation == HTTP_METHOD_ENUM.GET.getClassAnnotation();
    }
    public boolean isPost(){
        return this.classAnnotation == HTTP_METHOD_ENUM.POST.getClassAnnotation();
    }
    public boolean isPut(){
        return this.classAnnotation == HTTP_METHOD_ENUM.PUT.getClassAnnotation();
    }
    public boolean isDelete(){
        return this.classAnnotation == HTTP_METHOD_ENUM.DELETE.getClassAnnotation();
    }


    public static HTTP_METHOD_ENUM valueOfByAnnotation(final Annotation annotation){
        return annotation == null ? null 
            : Arrays.stream( HTTP_METHOD_ENUM.values() )
                .filter( item -> annotation.annotationType().isAssignableFrom( item.getClassAnnotation() ) )
                .findAny().orElseGet( ()->null );
    }

    public static HTTP_METHOD_ENUM valueOfByClassAnnotation(final Class<? extends Annotation> classAnnotation){
        return classAnnotation == null ? null 
            : Arrays.stream( HTTP_METHOD_ENUM.values() )
                .filter( item -> classAnnotation.equals( item.getClassAnnotation() ) )
                .findAny().orElseGet( ()->null );
    }

}