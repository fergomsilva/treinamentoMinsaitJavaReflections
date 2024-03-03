package br.com.gom.webframework.enumerations;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import br.com.gom.webframework.annotations.httpmethods.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPutMethod;


/**
 * Enumeração com os métodos HTTP tratatos pelo framework nas requisições.<br>
 * <p>Cada método HTTP é atrelado a anotação correspondente.</p>
 * Métodos HTTP:
 * <ul>
 * <li>GET -> {@link WebFrameworkGetMethod @WebFrameworkGetMethod}</li>
 * <li>POSTO -> {@link WebFrameworkPostMethod @WebFrameworkPostMethod}</li>
 * <li>PUT -> {@link WebFrameworkPutMethod @WebFrameworkPutMethod}</li>
 * <li>DELETE -> {@link WebFrameworkDeleteMethod @WebFrameworkDeleteMethod}</li>
 * </ul>
 * ---------------------
 * @see WebFrameworkGetMethod
 * @see WebFrameworkPostMethod
 * @see WebFrameworkPutMethod
 * @see WebFrameworkDeleteMethod
 */
public enum HTTP_METHOD_ENUM{

    /**
     * @see WebFrameworkGetMethod
     */
    GET( WebFrameworkGetMethod.class ), 
    /**
     * @see WebFrameworkPostMethod
     */
    POST( WebFrameworkPostMethod.class ), 
    /**
     * @see WebFrameworkPutMethod
     */
    PUT( WebFrameworkPutMethod.class ), 
    /**
     * @see WebFrameworkDeleteMethod
     */
    DELETE( WebFrameworkDeleteMethod.class );


    /**
     * Classe correspondente a anotação vinculao ao método HTTP.
     * @see WebFrameworkGetMethod
     * @see WebFrameworkPostMethod
     * @see WebFrameworkPutMethod
     * @see WebFrameworkDeleteMethod
     */
    private Class<? extends Annotation> classAnnotation;

    private HTTP_METHOD_ENUM(final Class<? extends Annotation> classAnnotation){
        this.classAnnotation = classAnnotation;
    }

    public Class<? extends Annotation> getClassAnnotation(){
        return this.classAnnotation;
    }

    /**
     * Diz se a anotação vinculada ao enum do método HTTP é {@link WebFrameworkGetMethod @WebFrameworkGetMethod}.
     * @return <b>true</b> se anotação vinculada ao enum do método HTTP é {@link WebFrameworkGetMethod @WebFrameworkGetMethod}.
     * @see WebFrameworkGetMethod
     */
    public boolean isGet(){
        return this.classAnnotation == HTTP_METHOD_ENUM.GET.getClassAnnotation();
    }
    /**
     * Diz se a anotação vinculada ao enum do método HTTP é {@link WebFrameworkPostMethod @WebFrameworkPostMethod}.
     * @return <b>true</b> se anotação vinculada ao enum do método HTTP é {@link WebFrameworkPostMethod @WebFrameworkPostMethod}.
     * @see WebFrameworkGetMethod
     */
    public boolean isPost(){
        return this.classAnnotation == HTTP_METHOD_ENUM.POST.getClassAnnotation();
    }
    /**
     * Diz se a anotação vinculada ao enum do método HTTP é {@link WebFrameworkPutMethod @WebFrameworkPutMethod}.
     * @return <b>true</b> se anotação vinculada ao enum do método HTTP é {@link WebFrameworkPutMethod @WebFrameworkPutMethod}.
     * @see WebFrameworkGetMethod
     */
    public boolean isPut(){
        return this.classAnnotation == HTTP_METHOD_ENUM.PUT.getClassAnnotation();
    }
    /**
     * Diz se a anotação vinculada ao enum do método HTTP é {@link WebFrameworkDeleteMethod @WebFrameworkDeleteMethod}.
     * @return <b>true</b> se anotação vinculada ao enum do método HTTP é {@link WebFrameworkDeleteMethod @WebFrameworkDeleteMethod}.
     * @see WebFrameworkGetMethod
     */
    public boolean isDelete(){
        return this.classAnnotation == HTTP_METHOD_ENUM.DELETE.getClassAnnotation();
    }


    /**
     * Obtem o enum a partir da anotação utilizada no método do controller.
     * @param annotation anotação configurada no método do controller.
     * @return enumeração encontrada a partir da anotação.
     */
    public static HTTP_METHOD_ENUM valueOfByAnnotation(final Annotation annotation){
        return annotation == null ? null 
            : Arrays.stream( HTTP_METHOD_ENUM.values() )
                .filter( item -> annotation.annotationType().isAssignableFrom( item.getClassAnnotation() ) )
                .findAny().orElseGet( ()->null );
    }

}