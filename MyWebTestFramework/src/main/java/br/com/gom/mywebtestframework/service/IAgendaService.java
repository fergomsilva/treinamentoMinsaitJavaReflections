package br.com.gom.mywebtestframework.service;

import java.util.List;
import java.util.Optional;

import br.com.gom.mywebtestframework.model.Agendamento;


public interface IAgendaService{
    
    List<Agendamento> listar();

    List<Agendamento> listarPorDia(final String data);

    Optional<Agendamento> obter(final String data, final String hora) throws Exception;

    Optional<Agendamento> inserir(final Agendamento agend) throws Exception;

    Optional<Agendamento> atualizar(final Agendamento agend) throws Exception;

    Optional<Agendamento> excluir(final String data, final String hora) throws Exception;

}