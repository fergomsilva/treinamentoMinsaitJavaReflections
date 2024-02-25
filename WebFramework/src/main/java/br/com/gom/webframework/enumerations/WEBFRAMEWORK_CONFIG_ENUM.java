package br.com.gom.webframework.enumerations;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor( access=AccessLevel.PRIVATE )
public enum WEBFRAMEWORK_CONFIG_ENUM{
    
    PORT( "webframework.config.port", "WEBFRAMEWORK_PORT" );

    private String idProperties;
    private String idEnvironmentVar;


    public static WEBFRAMEWORK_CONFIG_ENUM valueOfByIdConfig(final String idConfig){
        return ( idConfig == null || idConfig.trim().isEmpty() ) ? null 
            : Arrays.stream( WEBFRAMEWORK_CONFIG_ENUM.values() )
                .filter( item -> idConfig.equals( item.getIdProperties() ) 
                    || idConfig.equals( item.getIdEnvironmentVar() ) )
                .findAny().orElseGet( ()->null );
    }

}