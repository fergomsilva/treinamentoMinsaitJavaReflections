package br.com.gom.webframework.datastructures;

import java.util.HashMap;
import java.util.Map;

import br.com.gom.webframework.annotations.WebFrameworkRepository;
import br.com.gom.webframework.annotations.WebFrameworkService;

/**
 * Mapa que armazena o conjunto chave/valor dos nomes das interfaces e as respectivas 
 * implementações anotadas com {@link WebFrameworkService @WebFrameworkService} 
 * ou {@link WebFrameworkRepository @WebFrameworkRepository}.
 * @see WebFrameworkService
 * @see WebFrameworkRepository
 */
public class InterfaceImplementationMap{
    
    /**
     * Conjunto chave/valor dos nomes da interface e implementação mapeadas.
     */
    private static final Map<String, String> MAPA = new HashMap<>();

    
    private InterfaceImplementationMap(){
        super();
    }

    /**
     * Obtém o nome da classe de implementação a partir do nome da interface informada.
     * @param interfaceName nome da interface para a busca.
     * @return nome da classe de implementação encontada.
     */
    public static String getByKey(final String interfaceName){
        return MAPA.get( interfaceName );
    }

    /**
     * Acrescenta o conjunto chave/valor dos nomes da interface e implementação.
     * @param interfaceName nome da interface.
     * @param implClassName nome da classe de implementação.
     */
    public static void put(final String interfaceName, final String implClassName){
        MAPA.put( interfaceName, implClassName );
    }

}