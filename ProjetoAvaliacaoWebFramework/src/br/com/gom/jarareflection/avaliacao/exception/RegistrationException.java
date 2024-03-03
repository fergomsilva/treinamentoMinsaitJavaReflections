package br.com.gom.jarareflection.avaliacao.exception;

import static br.com.gom.jarareflection.avaliacao.util.Utils.isNotEmpty;


/**
 * Classe exclusiva para exceções ocorridas nas operação de incluir, alterar e excluir.
 */
public class RegistrationException extends Exception{
    private static final String MSG = "Erro em %s %s!";
    private static final String MSG2 = "Erro em %s %s: %s.";
    
    /**
     * Método construtor.
     * @param operation descrição da operação que ocorreu o erro: incluir, alterar ou excluir.
     * @param entity o nome negocial da entidade principal utilizada na operação.
     */
    public RegistrationException(final String operation, final String entity){
        super( String.format( MSG, 
            ( isNotEmpty( operation ) ? operation : "operar" ), 
            ( isNotEmpty( entity ) ? entity : "registro" ) 
        ) );
    }

    /**
     * Método construtor.
     * @param operation descrição da operação que ocorreu o erro: incluir, alterar ou excluir.
     * @param entity o nome negocial da entidade principal utilizada na operação.
     * @param message mensagem complementar
     */
    public RegistrationException(final String operation, final String entity, final String message){
        super( String.format( MSG2, 
            ( isNotEmpty( operation ) ? operation : "operar" ), 
            ( isNotEmpty( entity ) ? entity : "registro" ),
            ( isNotEmpty( message ) ? message : "erro não tratado." ) 
        ) );
    }

}