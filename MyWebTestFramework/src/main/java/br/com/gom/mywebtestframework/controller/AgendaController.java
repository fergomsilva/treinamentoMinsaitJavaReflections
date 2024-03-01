package br.com.gom.mywebtestframework.controller;

import java.util.List;

import br.com.gom.mywebtestframework.model.Agendamento;
import br.com.gom.mywebtestframework.service.IAgendaService;
import br.com.gom.webframework.annotations.WebFrameworkController;
import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkBody;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkPathParameter;
import br.com.gom.webframework.annotations.datarequests.WebFrameworkRequestParameter;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkDeleteMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkGetMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPostMethod;
import br.com.gom.webframework.annotations.httpmethods.WebFrameworkPutMethod;


@WebFrameworkController
public class AgendaController{
    
    @WebFrameworkInject
    private IAgendaService service;


    @WebFrameworkGetMethod( "/agenda/listar" )
    public List<Agendamento> listar(){
        return this.service.listar();
    }

    @WebFrameworkGetMethod( "/agenda/listar/{dia}" )
    public List<Agendamento> listarPorDia(final @WebFrameworkPathParameter( "dia" ) String data){
        return this.service.listarPorDia( data );
    }

    @WebFrameworkGetMethod( "/agenda" )
    public Agendamento obter(final @WebFrameworkRequestParameter( "dia" ) String data, 
    final @WebFrameworkRequestParameter( "hora" ) String hora) throws Exception{
        return this.service.obter( data, hora ).orElseGet( ()->null );
    }

    @WebFrameworkPostMethod( "/agenda" )
    public Agendamento inserir(final @WebFrameworkBody Agendamento agend) throws Exception{
        return this.service.inserir( agend ).orElseGet( ()->null );
    }

    @WebFrameworkPutMethod( "/agenda" )
    public Agendamento atualizar(final @WebFrameworkBody Agendamento agend) throws Exception{
        return this.service.atualizar( agend ).orElseGet( ()->null );
    }

    @WebFrameworkDeleteMethod( "/agenda" )
    public void excluir(final @WebFrameworkRequestParameter( "dia" ) String data, 
    final @WebFrameworkRequestParameter( "hora" ) String hora) throws Exception{
        this.service.excluir( data, hora );
    }

}