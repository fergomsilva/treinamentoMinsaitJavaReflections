package br.com.gom.webframework.datastructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class InterfaceImplementationMap{
    
    private static final Map<String, String> MAPA = new HashMap<>();

    
    private InterfaceImplementationMap(){
        super();
    }

    public static boolean hasKey(final String interfaceName){
        return Optional.ofNullable( getByKey( interfaceName ) ).isPresent();
    }

    public static String getByKey(final String interfaceName){
        return MAPA.get( interfaceName );
    }

    public static void put(final String interfaceName, final String implClassName){
        MAPA.put( interfaceName, implClassName );
    }

}