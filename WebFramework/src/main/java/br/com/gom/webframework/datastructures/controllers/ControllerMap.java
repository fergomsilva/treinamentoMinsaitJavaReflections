package br.com.gom.webframework.datastructures.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class ControllerMap{

    private static final Map<String, RequestControllerData> MAPA = new HashMap<>();

    
    private ControllerMap(){
        super();
    }

    public static RequestControllerData find(final String httpMethod, final String url){
        RequestControllerData data = MAPA.get( httpMethod + url );
        if( data == null ){
            data = MAPA.entrySet().stream()
                .filter( item -> !item.getValue().isStaticUrl() 
                    && httpMethod.equals( item.getValue().getHttpMethod().name() ) )
                .map( Entry::getValue )
                .filter( reqData -> {
                    return ( url + " " ).matches( reqData.getUrlRegex() );
                } )
                .findFirst()
                .orElseGet( ()->null )
            ;
        }
        return data;
    }

    public static void put(final RequestControllerData data){
        if( data.isStaticUrl() )
            MAPA.put( ( data.getHttpMethod() + data.getUrl() ), data );
        else
            MAPA.put( ( data.getHttpMethod() + data.getUrlRegex() ), data );
    }

    public static List<RequestControllerData> listValues(){
        return MAPA.values().stream().toList();
    }
    
}