package br.com.gom.webframework.datastructures.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ControllerInstance{
    
    private static final Map<String, Object> CONTROLLERS = new HashMap<>();

    
    private ControllerInstance(){
        super();
    }

    public static boolean hasKey(final String controllerClassName){
        return Optional.ofNullable( getByKey( controllerClassName ) ).isPresent();
    }

    public static Object getByKey(final String controllerClassName){
        return CONTROLLERS.get( controllerClassName );
    }

    public static void put(final String controllerClassName, final Object controller){
        CONTROLLERS.put( controllerClassName, controller );
    }

}