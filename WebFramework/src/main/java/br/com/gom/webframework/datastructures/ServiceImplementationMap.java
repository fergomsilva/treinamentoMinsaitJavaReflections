package br.com.gom.webframework.datastructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class ServiceImplementationMap{
    
    private static final Map<String, String> MAPA = new HashMap<>();

    
    private ServiceImplementationMap(){
        super();
    }

    public static boolean hasKey(final String iClassName){
        return Optional.ofNullable( getByKey( iClassName ) ).isPresent();
    }

    public static String getByKey(final String iClassName){
        return MAPA.get( iClassName );
    }

    public static void put(final String iClassName, final String implClassName){
        MAPA.put( iClassName, implClassName );
    }

}