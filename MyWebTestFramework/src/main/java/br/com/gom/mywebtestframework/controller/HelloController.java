package br.com.gom.mywebtestframework.controller;

import java.util.Arrays;
import java.util.List;

import br.com.gom.mywebtestframework.model.Produto;
import br.com.gom.mywebtestframework.service.IService;
import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathVariable;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParam;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPutMethod;


@WebFrameworkController
public class HelloController{

    @WebFrameworkInject
    private IService iservice;
    
    @WebFrameworkGetMethod( "/" )
    public String testeRaiz(){
        return "TESTE RAIZ";
    }

    @WebFrameworkGetMethod( "/hello" )
    public String returnHelloWorld(){
        return "RETURN Hello wordl !";
    }

    @WebFrameworkGetMethod( "/produto/lista" )
    public List<Produto> listarProdutos(){
        return Arrays.asList( new Produto( 1l, "Nome1", 2000.0, "teste.jpg" ) );
    }

    @WebFrameworkGetMethod( "/produto/{id}" )
    public Produto exibirProduto(final @WebFrameworkPathVariable( "id" ) Long idProduto){
        return new Produto( idProduto, ( "Nome" + idProduto ), 2000.0, "teste.jpg" );
    }

    @WebFrameworkGetMethod( "/produto" )
    public Produto exibirProduto2(final @WebFrameworkRequestParam( "idProduto" ) Long idProduto){
        return new Produto( idProduto, ( "Nome" + idProduto ), 2000.0, "teste.jpg" );
    }

    @WebFrameworkGetMethod( "/produto/lista/filtro" )
    public List<Produto> exibirProduto2(final @WebFrameworkRequestParam( "nome" ) String filtroNome){
        return Arrays.asList( new Produto( 1l, filtroNome, 2000.0, "teste.jpg" ) );
    }

    @WebFrameworkPutMethod( "/produto/{id}/nome/{nomeProduto}" )
    public Produto atualizarNomeProduto(final @WebFrameworkPathVariable( "nomeProduto" ) String nomeProduto, 
    final @WebFrameworkPathVariable( "id" ) Long idProduto){
        return new Produto( idProduto, nomeProduto, 2000.0, "teste.jpg" );
    }

    @WebFrameworkDeleteMethod( "/produto/{id}" )
    public Produto deleteProduto(final @WebFrameworkPathVariable( "id" ) Long idProduto){
        return new Produto( idProduto, ( "Nome" + idProduto ), 2000.0, "teste.jpg" );
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
        return this.iservice.chamadaCustomRepository( "Hello injected repository" );
    }

}