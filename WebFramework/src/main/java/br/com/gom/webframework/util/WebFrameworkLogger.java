package br.com.gom.webframework.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Classe utilitaria para geração de log no console.
 */
public class WebFrameworkLogger{

    private WebFrameworkLogger(){
        super();
    }
    
    // cores
    private static final String VERDE = "\u001B[32m";
    private static final String AMARELO = "\u001B[33m";
    private static final String BRANCO = "\u001B[37m";
    private static final String VERMELHO = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    private static final DateTimeFormatter WEB_FRAMEWORKDATE = DateTimeFormatter
        .ofPattern( "yyyy-MM-dd HH:mm:ss" );
    private static final String TXT_LOG = VERDE + "%15s" + AMARELO 
        + " %-30s: " + BRANCO + "%s " + RESET;
    private static final String TXT_LOG_ERROR = VERDE + "%15s" + AMARELO 
        + " %-30s: " + VERMELHO + "[ERROR] %s " + RESET;

    /**
     * Apresenta o banner 'WebFramework' no console.
     */
    @SuppressWarnings( { "all" } )
    public static void showBanner(){
        System.out.printf( VERDE ).println( " _       __     __    ______                                             __  " );
        System.out.printf( VERDE ).println( "| |     / /__  / /_  / ____/________ _____ ___  ___ _      ______  _____/ /__" );
        System.out.printf( VERDE ).println( "| | /| / / _ \\/ __ \\/ /_  / ___/ __ `/ __ `__ \\/ _ \\ | /| / / __ \\/ ___/ //_/" );
        System.out.printf( VERDE ).println( "| |/ |/ /  __/ /_/ / __/ / /  / /_/ / / / / / /  __/ |/ |/ / /_/ / /  / ,<   " );
        System.out.printf( VERDE ).println( "|__/|__/\\___/_.___/_/   /_/   \\__,_/_/ /_/ /_/\\___/|__/|__/\\____/_/  /_/|_|  " );
        System.out.printf( VERDE ).println( "                                                                             " );
        System.out.print( RESET );
    }

    /**
     * Loga uma mensagem no console nas cores padrão.
     * @param modulo nome do modulo da mensagem.
     * @param mensagem mensagem para apresentar no console, pode possuir argumentos.
     * @param params valores dos argumentos da mensagem.
     */
    @SuppressWarnings( { "all" } )
    public static void log(final String modulo, final String mensagem, final Object... params){
        if( params == null || params.length == 0 )
            System.out.printf( TXT_LOG, LocalDateTime.now().format( WEB_FRAMEWORKDATE ), 
                modulo, mensagem ).println();
        else
            System.out.printf( TXT_LOG, LocalDateTime.now().format( WEB_FRAMEWORKDATE ), 
                modulo, String.format( mensagem, params ) ).println();
    }

    /**
     * Loga uma mensagem de ERRO no console com a cor vermelho.
     * @param modulo nome do modulo da mensagem.
     * @param mensagem mensagem para apresentar no console, pode possuir argumentos.
     * @param params valores dos argumentos da mensagem.
     */
    @SuppressWarnings( { "all" } )
    public static void error(final String modulo, final String mensagem, final Object... params){
        if( params == null || params.length == 0 )
            System.out.printf( TXT_LOG_ERROR, LocalDateTime.now().format( WEB_FRAMEWORKDATE ), 
                modulo, mensagem ).println();
        else
            System.out.printf( TXT_LOG_ERROR, LocalDateTime.now().format( WEB_FRAMEWORKDATE ), 
                modulo, String.format( mensagem, params ) ).println();
    }

    /**
     * Loga uma mensagem de ERRO no console com a cor vermelho e 'printa' o stack track de uma exceção fornecida.
     * @param modulo nome do modulo da mensagem.
     * @param e exceção de erro.
     * @param mensagem mensagem para apresentar no console, pode possuir argumentos.
     * @param params valores dos argumentos da mensagem.
     */
    @SuppressWarnings( { "all" } )
    public static void error(final String modulo, final Exception e, final String mensagem, 
    final Object... params){
        error( modulo, mensagem, params );
        e.printStackTrace();
    }

}