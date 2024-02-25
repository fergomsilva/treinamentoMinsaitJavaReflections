package br.com.gom.webframework.datastructures.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import br.com.gom.webframework.enumerations.WEBFRAMEWORK_CONFIG_ENUM;


public class WebFrameworkConfigMap{

    private static final Map<String, String> MAPA = new HashMap<>();

    
    private WebFrameworkConfigMap(){
        super();
    }

    private static Optional<WEBFRAMEWORK_CONFIG_ENUM> findEnum(final String idConfig){
        return Optional.ofNullable( WEBFRAMEWORK_CONFIG_ENUM.valueOfByIdConfig( idConfig ) );
    }

    public static boolean hasKey(final String idConfig){
        return Optional.ofNullable( getByKey( idConfig ) ).isPresent();
    }

    public static String getByKey(final String idConfig){
        final Optional<WEBFRAMEWORK_CONFIG_ENUM> enumConfig = findEnum( idConfig );
        return MAPA.get( enumConfig.isPresent() ? enumConfig.get().name() : idConfig );
    }

    public static void put(final String idConfig, final String value){
        final Optional<WEBFRAMEWORK_CONFIG_ENUM> enumConfig = findEnum( idConfig );
        MAPA.put( enumConfig.isPresent() ? enumConfig.get().name() : idConfig, value );
    }

    public static int port(){
        final String value = getByKey( WEBFRAMEWORK_CONFIG_ENUM.PORT.name() );
        return ( value != null && !value.trim().isEmpty() ) ? Integer.parseInt( value.trim() ) 
            : 8080;
    }

}