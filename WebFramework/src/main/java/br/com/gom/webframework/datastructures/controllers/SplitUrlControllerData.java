package br.com.gom.webframework.datastructures.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 * Bean que mantém os dados a respeito de cada 'parte' da URI configurada na requisição 
 * no método do controller.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of={ "path" } )
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
    

    /**
     * Diz se esta configuração correspondente a uma varíavel da URI.
     * @return
     */
    public boolean isParameter(){
        return this.getParamClassFromMethod() != null;
    }
    
    @Override
    public String toString(){
        return this.isParameter() ? String.format( "path[%s];isParameter[%s];Class[%s];", 
                this.getPath(), this.isParameter(), this.getParamClassFromMethod() )
            : String.format( "path[%s];isParameter[%s];", this.getPath(), this.isParameter() );
    }

}