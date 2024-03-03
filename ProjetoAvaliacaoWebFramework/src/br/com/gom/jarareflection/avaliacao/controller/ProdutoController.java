package br.com.gom.jarareflection.avaliacao.controller;

import java.util.List;

import br.com.gom.jarareflection.avaliacao.exception.RegistrationException;
import br.com.gom.jarareflection.avaliacao.exception.ValidationException;
import br.com.gom.jarareflection.avaliacao.model.Produto;
import br.com.gom.jarareflection.avaliacao.service.IProdutoService;
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
public class ProdutoController{

    @WebFrameworkInject
    private IProdutoService service;

    @WebFrameworkGetMethod( "/produto/listar" )
    public List<Produto> listarProdutos(){
        return this.service.list();
    }

    @WebFrameworkGetMethod( "/produto/listar/filtro" )
    public List<Produto> filtrarProduto(final @WebFrameworkRequestParam( "departamento" ) String depart, 
    final @WebFrameworkRequestParam( "termo" ) String termo){
        return this.service.find( termo, depart );
    }

    @WebFrameworkGetMethod( "/produto/{id}" )
    public Produto obterProduto(final @WebFrameworkPathVariable( "id" ) Long idProduto){
        return this.service.get( idProduto )
            .orElseGet( ()->null );
    }

    @WebFrameworkPostMethod( "/produto" )
    public Produto salvarProduto(final @WebFrameworkBody Produto produto) 
    throws RegistrationException, ValidationException{
        return this.service.insert( produto );
    }

    @WebFrameworkPutMethod( "/produto" )
    public Produto atualizarProduto(final @WebFrameworkBody Produto produto) 
    throws RegistrationException, ValidationException{
        return this.service.update( produto );
    }

    @WebFrameworkPutMethod( "/produto/{id}/novo-preco" )
    public Produto atualizarPrecoProduto(final @WebFrameworkRequestParam( "preco" ) double novoPreco, 
    final @WebFrameworkPathVariable( "id" ) long idProduto) 
    throws RegistrationException, ValidationException{
        return this.service.update( idProduto, novoPreco );
    }

    @WebFrameworkDeleteMethod( "/produto/{id}" )
    public Produto excluirProduto(final @WebFrameworkPathVariable( "id" ) long idProduto) 
    throws RegistrationException{
        return this.service.delete( idProduto );
    }

}