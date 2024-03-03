package br.com.gom.jarareflection.avaliacao.bdmock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.gom.jarareflection.avaliacao.model.Produto;


/**
 * Mock de um banco de dados em memória para testar o APP na execução dos testes.
 */
@SuppressWarnings( { "all" } )
public final class BD_MOCK{
    
    private BD_MOCK(){
        super();
    }

    public static final List<Produto> PRODUTOS = new ArrayList<>();


    static{
        PRODUTOS.add( new Produto( 1l, 
            "Smart TV 65\" 4K UHD OLED Evo LG OLED65C3", 
            "TV e Vídeo", 
            "Smart TV 65\" 4K UHD OLED Evo LG OLED65C3 - 120Hz Wi-Fi Bluetooth Alexa 4 HDMI G-Sync FreeSync", 
            9_689.05, "LG", 
            "LG", "OLED65C3PSA", 
            Arrays.asList( "dc7c73383fdb1c6190d542baaabf722e.jpg", "901d3e8fdc971fa2c5e73c0cc7251a1e.jpg" ) ) );
        PRODUTOS.add( new Produto( 2l, "PlayStation 5 Slim", "Games", 
            "PlayStation 5 Slim 1TB Edição Digital", 3_609.05, "Sony", 
            "Sony", "PlayStation5 Slim", 
            Arrays.asList( "a53c0e10a00ccc641d098fafc98385ea.jpg" ) ) );
    }

}