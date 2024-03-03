package br.com.gom.webframework.datastructures.injections;

import java.util.HashMap;
import java.util.Map;

import br.com.gom.webframework.annotations.WebFrameworkInject;


/**
 * Mapa que armazena o conjunto chave/valor dos nomes das classes injetadas e 
 * as respectivas instancias injetadas.<br>
 * <p>Os atributos anotados com {@link WebFrameworkInject WebFrameworkInject} 
 * serão injetados na classe pai e as instancias criadas serão armazenadas aqui.</p>
 * @see WebFrameworkInject
 */
public class DependencyInjectionInstance{

    /**
     * Conjunto chave/valor dos nomes das classes injetadas e as respectivas 
     * instancias injetadas.
     */
    private static final Map<String, Object> INJECTIONS = new HashMap<>();


    private DependencyInjectionInstance(){
        super();
    }

    /**
     * Obtém a instancia injetada a partir do nome da classe.
     * @param injectionClassName nome da classe.
     * @return instancia injetada.
     */
    public static Object getByKey(final String injectionClassName){
        return INJECTIONS.get( injectionClassName );
    }

    /**
     * Acrescenta o conjunto chave/valor do nome da classe e a instancia desta.
     * @param injectionClassName nome da classe.
     * @param injectionObj instancia da classe.
     */
    public static void put(final String injectionClassName, final Object injectionObj){
        INJECTIONS.put( injectionClassName, injectionObj );
    }
    
}