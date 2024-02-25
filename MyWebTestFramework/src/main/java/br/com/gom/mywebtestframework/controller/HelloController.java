package br.com.gom.mywebtestframework.controller;

import br.com.gom.mywebtestframework.model.Produto;
import br.com.gom.mywebtestframework.service.IService;
import br.com.gom.webframework.annotations.WebFrameworkBody;
import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.WebFrameworkPutMethod;


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

    @WebFrameworkGetMethod( "/produto/{idProduto}" )
    public Produto exibirProduto(final Long idProduto){
        return Produto.builder()
            .id( idProduto )
            .nome( "Nome" + idProduto )
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
    public String chamadaCustom(){
        return this.iservice.chamadaCustom( "Hello injected" );
    }

    @WebFrameworkGetMethod( "/injected/subnivel" )
    public String chamadaCustomNivel2(){
        return this.iservice.chamadaCustomSubnivel( "Hello injected subnivel" );
    }

    @WebFrameworkPutMethod( "/produto/{idProduto}/nome/{nomeProduto}" )
    public Produto atualizarNomeProduto(final Long idProduto, final String nomeProduto){
        return Produto.builder()
            .id( idProduto )
            .nome( nomeProduto )
            .valor( 2000.0 )
            .linkFoto( "teste.jpg" )
        .build();
    }

    @WebFrameworkDeleteMethod( "/produto/{idProduto}" )
    public Produto deleteProduto(final Long idProduto){
        return Produto.builder()
            .id( idProduto )
            .nome( "Nome" + idProduto )
            .valor( 2000.0 )
            .linkFoto( "teste.jpg" )
        .build();
    }

    //PathParameter
        //PUT
        //DELeTE
        //GET
    //RequestParameter
}