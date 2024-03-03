package br.com.gom.webframework.datastructures.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.gom.webframework.annotations.WebFrameworkController;


/**
 * Mapa que armazena o conjunto chave/valor das requisições configuradas com os dados dos 
 * métodos correspondentes que estão nas classes anotadas com {@link WebFrameworkController @WebFrameworkController}.
 * @see WebFrameworkController
 */
public class ControllerMap{

    /**
     * Conjunto chave/valor das requisições mapeadas nas controllers.
     */
    private static final Map<String, RequestControllerData> MAPA = new HashMap<>();

    
    private ControllerMap(){
        super();
    }

    /**
     * Obtém os dados da requisição encontrada a partir dos dados de busca.
     * @param httpMethod HTTP método da requisição.
     * @param url URI da requisição.
     * @return dados da requisição encontrada.
     */
    public static RequestControllerData find(final String httpMethod, final String url){
        // tenta buscar por url estatica, ou seja, sem variaveis
        RequestControllerData data = MAPA.get( httpMethod + url );
        if( data == null ){
            // nao existem dados para a url estatica, vai tentar buscar por 
            // regex as nao estaticas
            data = MAPA.entrySet().stream()
                .filter( item -> !item.getValue().isStaticUrl() 
                    && httpMethod.equals( item.getValue().getHttpMethod().name() ) )
                .map( Entry::getValue )
                .filter( reqData -> ( url + " " ).matches( reqData.getUrlRegex() ) )
                .findFirst()
                .orElseGet( ()->null )
            ;
        }
        return data;
    }

    /**
     * Acrescenta os dados da requisição configurada.
     * @param data dados da requisição configurada.
     */
    public static void put(final RequestControllerData data){
        if( data.isStaticUrl() )
            MAPA.put( ( data.getHttpMethod() + data.getUrl() ), data );
        else
            MAPA.put( ( data.getHttpMethod() + data.getUrlRegex() ), data );
    }

    /**
     * Lista todos os dados registrados.
     * @return lista completa dos dados configurados para todas as requisições.
     */
    public static List<RequestControllerData> listValues(){
        return MAPA.values().stream().toList();
    }
    
}