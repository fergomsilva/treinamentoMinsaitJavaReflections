package br.com.gom.jarareflection.avaliacao.exception;

/**
 * Classe exclusiva para validação dos dados das operação de cadastro.
 */
public class ValidationException extends Exception{
    private static final String MSG_CAMPO_OBRIGATORIO = "Campo '%s' é obrigatório!";
    
    /**
     * Método construtor.<br>
     * <p>Apenas lança exceção de campos obrigatorios não informados.</p>
     * @param nomeCampo nome do campo obrigatório.
     */
    public ValidationException(final String nomeCampo){
        this( MSG_CAMPO_OBRIGATORIO, nomeCampo );
    }

    /**
     * Método construtor.
     * @param message mensagem complementar
     * @param param valores para prencher os parametros da mensagem, quando houver.
     */
    public ValidationException(final String message, final Object... param){
        super( String.format( message, param ) );
    }

}