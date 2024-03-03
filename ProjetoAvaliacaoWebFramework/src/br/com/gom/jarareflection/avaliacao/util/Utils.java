package br.com.gom.jarareflection.avaliacao.util;

import java.util.Collection;


/**
 * Classe com metodos utilitarios.
 */
public final class Utils{

    private Utils(){
        super();
    }

    /**
     * Verifica se o objeto informado esta vazio ou nulo.
     * @param value valor para verificacao
     * @return <b>true</b> se o valor é vazio ou nulo.
     */
    public static final boolean isEmpty(final Object value){
        if( value == null )
            return true;
        if( String.class.isAssignableFrom( value.getClass() ) )
            return value.toString().strip().isEmpty();
        if( Collection.class.isAssignableFrom( value.getClass() ) )
            return ( (Collection<?>)value ).isEmpty();
        return false;
    }

    /**
     * Verifica se o objeto informado NAO esta vazio ou nulo.
     * @param value valor para verificacao
     * @return <b>true</b> se o valor NAO é vazio ou nulo.
     */
    public static final boolean isNotEmpty(final Object value){
        return !isEmpty( value );
    }
    
}