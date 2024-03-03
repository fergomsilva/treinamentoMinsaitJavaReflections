package br.com.gom.webframework.datastructures.controllers;

import java.util.Objects;


/**
 * Bean que mantém os dados a respeito de cada 'parte' da URI configurada na requisição 
 * no método do controller.
 */
public class SplitUrlControllerData{
    
    /**
     * 'Parte' da URI.
     */
    private String path;
    /**
     * Se a 'parte' da URI for uma varíavel correspondente a um parametro/argumento 
     * do método, a classe do parametro/argumento é registrado aqui.
     */
    private Class<?> paramClassFromMethod;


    public SplitUrlControllerData(){
        super();
    }
    public SplitUrlControllerData(final String path){
        this();
        this.setPath( path );
    }
    

    public String getPath(){
        return path;
    }
    public void setPath(final String path){
        this.path = path;
    }

    public Class<?> getParamClassFromMethod(){
        return paramClassFromMethod;
    }
    public void setParamClassFromMethod(final Class<?> paramClassFromMethod){
        this.paramClassFromMethod = paramClassFromMethod;
    }
    
    /**
     * Diz se esta configuração correspondente a uma varíavel da URI.
     * @return
     */
    public boolean isParameter(){
        return this.getParamClassFromMethod() != null;
    }

    @Override
    public boolean equals(final Object obj){
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( this.getClass() != obj.getClass() )
            return false;
        final SplitUrlControllerData other = (SplitUrlControllerData)obj;
        return ( this.getPath().equals( other.getPath() ) );
    }

    @Override
    public int hashCode(){
        return Objects.hash( this.getPath() );
    }

    @Override
    public String toString(){
        return this.isParameter() ? String.format( "path[%s];isParameter[%s];Class[%s];", 
                this.getPath(), this.isParameter(), this.getParamClassFromMethod() )
            : String.format( "path[%s];isParameter[%s];", this.getPath(), this.isParameter() );
    }

}