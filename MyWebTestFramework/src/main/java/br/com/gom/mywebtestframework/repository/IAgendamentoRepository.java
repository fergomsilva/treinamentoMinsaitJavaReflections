package br.com.gom.mywebtestframework.repository;

import java.util.List;
import java.util.Optional;

import br.com.gom.mywebtestframework.model.Agendamento;


public interface IAgendamentoRepository{
    
    List<Agendamento> listAll();

    List<Agendamento> listByData(final String data);

    Optional<Agendamento> findOne(final String data, final String hora);

    Optional<Agendamento> insert(final Agendamento agend) throws Exception;

    Optional<Agendamento> update(final Agendamento agend) throws Exception;

    Optional<Agendamento> delete(final String data, final String hora) throws Exception;

}