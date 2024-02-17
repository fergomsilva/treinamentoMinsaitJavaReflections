package br.com.gom.mywebtestframework.controller;

import br.com.gom.mywebtestframework.model.Produto;
import br.com.gom.webframework.annotations.WebFrameworkBody;
import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.WebFrameworkPostMethod;


@WebFrameworkController
public class HelloController{
    
    @WebFrameworkGetMethod( "/hello" )
    public String returnHelloWorld(){
        return "Hello wordl !";
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
    public String cadastrarProduto(final @WebFrameworkBody Produto novo){
        System.out.println( novo );
        return "Produto cadastrado";
    }

    @WebFrameworkGetMethod( "/teste" )
    public String teste(){
        return "Teste";
    }

}