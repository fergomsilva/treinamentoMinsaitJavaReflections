package br.com.gom.webframework.datastructures.controllers;

import java.lang.annotation.Annotation;
import java.util.Objects;

import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathVariable;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParam;


/**
 * Bean que mantém os dados a respeito de cada parametro/argumento do método no controller.
 */
public class ParameterMethodControllerData{
    
    /**
     * É a classe do parametro/argumento do método no controller.
     */
    private Class<?> paramClass;
    /**
     * É a anotação do parametro/argumento do método no controller.
     * @see WebFrameworkBody
     * @see WebFrameworkPathVariable
     * @see WebFrameworkRequestParam
     */
    private Annotation paramAnnotation;
    /**
     * É o nome correspondente a variavel da URI..
     * @see WebFrameworkPathVariable
     * @see WebFrameworkRequestParam
     */
    private String paramName;


    public ParameterMethodControllerData(){
        super();
    }
    public ParameterMethodControllerData(final String paramName){
        this();
        this.setParamName( paramName );
    }
    public ParameterMethodControllerData(final Class<?> paramClass){
        this();
        this.setParamClass( paramClass );
    }
    public ParameterMethodControllerData(final Class<?> paramClass, final Annotation paramAnnotation, final String paramName){
        this( paramName );
        this.setParamClass( paramClass );
        this.setParamAnnotation( paramAnnotation );
    }

    public Class<?> getParamClass(){
        return paramClass;
    }
    public void setParamClass(final Class<?> paramClass){
        this.paramClass = paramClass;
    }

    public Annotation getParamAnnotation(){
        return paramAnnotation;
    }
    public void setParamAnnotation(final Annotation paramAnnotation){
        this.paramAnnotation = paramAnnotation;
    }

    public String getParamName(){
        if( paramName == null )
            paramName = "";
        return paramName;
    }
    public void setParamName(final String paramName){
        this.paramName = paramName;
    }
    
    /**
     * Diz se a anotação vinculada ao parametro/argumento é {@link WebFrameworkBody @WebFrameworkBody}
     * @return <b>true</b> se a anotação é {@link WebFrameworkBody @WebFrameworkBody}
     */
    public boolean isBodyParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkBody.class );
    }

    /**
     * Diz se a anotação vinculada ao parametro/argumento é {@link WebFrameworkPathVariable @WebFrameworkPathVariable}
     * @return <b>true</b> se a anotação é {@link WebFrameworkPathVariable @WebFrameworkPathVariable}
     */
    public boolean isPathParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkPathVariable.class );
    }

    /**
     * Diz se a anotação vinculada ao parametro/argumento é {@link WebFrameworkRequestParam @WebFrameworkRequestParam}
     * @return <b>true</b> se a anotação é {@link WebFrameworkRequestParam @WebFrameworkRequestParam}
     */
    public boolean isRequestParameterAnnotation(){
        return this.getParamAnnotation() != null && this.getParamAnnotation().annotationType()
            .isAssignableFrom( WebFrameworkRequestParam.class );
    }

    @Override
    public boolean equals(final Object obj){
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( this.getClass() == obj.getClass() )
            return false;
        final ParameterMethodControllerData other = (ParameterMethodControllerData)obj;
        return ( this.getParamName().equals( other.getParamName() ) );
    }

    @Override
    public int hashCode(){
        return Objects.hash( this.getParamName() );
    }
    
    @Override
    public String toString(){
        return String.format( "Class[%s];Annotation[%s];Name[%s]", 
            this.getParamClass(), this.getParamAnnotation(), this.getParamName() );
    }

}