package br.com.gom.webframework.datastructures.injections;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class DependencyInjectionInstance{

    private static final Map<String, Object> INJECTIONS = new HashMap<>();


    private DependencyInjectionInstance(){
        super();
    }

    public static boolean hasKey(final String injectionClassName){
        return Optional.ofNullable( getByKey( injectionClassName ) ).isPresent();
    }

    public static Object getByKey(final String injectionClassName){
        return INJECTIONS.get( injectionClassName );
    }

    public static void put(final String injectionClassName, final Object injectionObj){
        INJECTIONS.put( injectionClassName, injectionObj );
    }
    
}