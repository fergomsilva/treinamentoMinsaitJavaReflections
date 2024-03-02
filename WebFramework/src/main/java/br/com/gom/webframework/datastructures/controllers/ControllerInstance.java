package br.com.gom.webframework.datastructures.controllers;

import java.util.HashMap;
import java.util.Map;

import br.com.gom.webframework.annotations.WebFrameworkController;


/**
 * Mapa que armazena o conjunto chave/valor dos nomes das classes controllers e 
 * as respectivas instancias.
 * <p>As classes anotadas com {@link WebFrameworkController WebFrameworkController} 
 * serão instanciadas e armazenadas aqui.</p>
 * @see WebFrameworkController
 */
public class ControllerInstance{
    
    /**
     * Conjunto chave/valor dos nomes das classes controllers e as respectivas 
     * instancias.
     */
    private static final Map<String, Object> CONTROLLERS = new HashMap<>();

    
    private ControllerInstance(){
        super();
    }

    /**
     * Obtém a instancia do controller a partir do nome da classe.
     * @param controllerClassName nome da classe controller.
     * @return instancia do controller.
     */
    public static Object getByKey(final String controllerClassName){
        return CONTROLLERS.get( controllerClassName );
    }

    /**
     * Acrescenta o conjunto chave/valor do nome da classe controller e a instancia desta.
     * @param controllerClassName nome da classe controller.
     * @param controller instancia da classe controller.
     */
    public static void put(final String controllerClassName, final Object controller){
        CONTROLLERS.put( controllerClassName, controller );
    }

}