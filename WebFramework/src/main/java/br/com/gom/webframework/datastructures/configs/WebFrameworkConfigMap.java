package br.com.gom.webframework.datastructures.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import br.com.gom.webframework.enumerations.WEBFRAMEWORK_CONFIG_ENUM;

/**
 * Registra as configurações passadas para o APP.
 */
public class WebFrameworkConfigMap{

    /**
     * Conjunto chave/valor das configurações do APP.
     */
    private static final Map<String, String> MAPA = new HashMap<>();

    
    private WebFrameworkConfigMap(){
        super();
    }

    /**
     * Verifica se o ID de configuração é uma das configurações padrão do framework.
     * @param idConfig ID da configuração.
     * @return configuração padrão do framework encontada a partir do ID ou null se 
     * não for uma configuração padrão.
     */
    private static Optional<WEBFRAMEWORK_CONFIG_ENUM> findEnum(final String idConfig){
        return Optional.ofNullable( WEBFRAMEWORK_CONFIG_ENUM.valueOfByIdConfig( idConfig ) );
    }

    /**
     * Diz se existe alguma configuração com o ID desejado.
     * @param idConfig ID da configuração.
     * @return <b>true</b> se existe configuração com esta chave.
     */
    public static boolean hasKey(final String idConfig){
        return Optional.ofNullable( getByKey( idConfig ) ).isPresent();
    }

    /**
     * Configuração encontrada a partir do ID informado.
     * @param idConfig ID da configuração.
     * @return configuração encontrada ou null se não encontrado.
     */
    public static String getByKey(final String idConfig){
        final Optional<WEBFRAMEWORK_CONFIG_ENUM> enumConfig = findEnum( idConfig );
        return MAPA.get( enumConfig.isPresent() ? enumConfig.get().name() : idConfig );
    }

    /**
     * Acrescenta o conjunto chave/valor de configuração.
     * @param idConfig ID da configuração.
     * @param value valor da configuração.
     */
    public static void put(final String idConfig, final String value){
        final Optional<WEBFRAMEWORK_CONFIG_ENUM> enumConfig = findEnum( idConfig );
        MAPA.put( enumConfig.isPresent() ? enumConfig.get().name() : idConfig, value );
    }

    /**
     * Retorna a porta que o APP será levantado. 
     * <p>Se não houver configuração customizada é retornado o valor padrão <b><i>8080</i></b>.</p>
     * @return número da porta da aplicação.
     */
    public static int port(){
        final String value = getByKey( WEBFRAMEWORK_CONFIG_ENUM.PORT.name() );
        return ( value != null && !value.trim().isEmpty() ) ? Integer.parseInt( value.trim() ) 
            : 8080;
    }

}