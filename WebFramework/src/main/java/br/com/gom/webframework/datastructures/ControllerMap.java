package br.com.gom.webframework.datastructures;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class ControllerMap{

    private static final Map<String, RequestControllerData> MAPA = new HashMap<>();

    
    private ControllerMap(){
        super();
    }

    public static boolean hasKey(final String httpMethodUrl){
        return Optional.ofNullable( getByKey( httpMethodUrl ) ).isPresent();
    }

    public static RequestControllerData getByKey(final String httpMethodUrl){
        return MAPA.get( httpMethodUrl );
    }

    public static void put(final String httpMethodUrl, final RequestControllerData data){
        MAPA.put( httpMethodUrl, data );
    }

    public static List<RequestControllerData> listValues(){
        return MAPA.values().stream().toList();
    }
    
}