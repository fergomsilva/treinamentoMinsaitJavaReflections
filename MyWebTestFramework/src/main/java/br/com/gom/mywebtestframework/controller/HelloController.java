package br.com.gom.mywebtestframework.controller;

import br.com.gom.mywebtestframework.model.Produto;
import br.com.gom.mywebtestframework.service.IService;
import br.com.gom.webframework.annotations.WebFrameworkBody;
import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.WebFrameworkPathVariable;
import br.com.gom.webframework.annotations.WebFrameworkPostMethod;


@WebFrameworkController
public class HelloController{

    @WebFrameworkInject
    private IService iservice;
    
    @WebFrameworkGetMethod( "/hello" )
    public String returnHelloWorld(){
        return "RETURN Hello wordl !";
    }

    @WebFrameworkGetMethod( "/produto" )
    public Produto exibirProduto(){
        return Produto.builder()
            .id( 1l )
            .nome( "Nome1" )
            .valor( 2000.0 )
            .linkFoto( "teste.jpg" )
        .build();
    }

    @WebFrameworkPostMethod( "/produto" )
    public Produto cadastrarProduto(final @WebFrameworkBody Produto novo){
        System.out.println( novo );
        return novo;
    }

    @WebFrameworkGetMethod( "/teste" )
    public String teste(){
        return "Teste";
    }

    @WebFrameworkGetMethod( "/teste2" )
    private String teste2(){
        return "Teste2";
    }

    @WebFrameworkGetMethod( "/injected" )
    public String chamadaCusto(){
        return this.iservice.chamadaCustom( "Hello injected" );
    }

    //http://localhost:8080/retornavalor/22222 == Retornando o valor de parametro: 22222
    @WebFrameworkGetMethod( "/retornavalor/{valor}" )
    public String retornoValor(final @WebFrameworkPathVariable String valor){
        return "Retornando o valor de parametro: " + valor;
    }

}