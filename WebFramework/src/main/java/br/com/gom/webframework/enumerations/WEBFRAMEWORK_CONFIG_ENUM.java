package br.com.gom.webframework.enumerations;

import java.util.Arrays;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Enumeração com as configurações padrão do framework.
 */
@Getter
@AllArgsConstructor( access=AccessLevel.PRIVATE )
public enum WEBFRAMEWORK_CONFIG_ENUM{
   
    /**
     * Porta de subida do APP.
     */
    PORT( "webframework.config.port", "WEBFRAMEWORK_PORT" );


    /**
     * ID da configuração se num properties.
     */
    private String idProperties;
    /**
     * ID da configuração se na variavel de ambiente.
     */
    private String idEnvironmentVar;

    /**
     * Obtem o enum a partir do ID informado, pode ser tanto o ID do properties ou da 
     * variavel de ambiente.
     * @param idConfig ID da configuração, pode ser tanto o ID do properties ou da 
     * variavel de ambiente.
     * @return enum encontrado ou null.
     */
    public static WEBFRAMEWORK_CONFIG_ENUM valueOfByIdConfig(final String idConfig){
        return ( idConfig == null || idConfig.trim().isEmpty() ) ? null 
            : Arrays.stream( WEBFRAMEWORK_CONFIG_ENUM.values() )
                .filter( item -> idConfig.equals( item.getIdProperties() ) 
                    || idConfig.equals( item.getIdEnvironmentVar() ) )
                .findAny().orElseGet( ()->null );
    }

}