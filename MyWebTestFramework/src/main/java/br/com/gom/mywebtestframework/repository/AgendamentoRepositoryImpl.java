package br.com.gom.mywebtestframework.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import br.com.gom.mywebtestframework.model.Agendamento;
import br.com.gom.webframework.annotations.WebFrameworkRepository;


@WebFrameworkRepository
public class AgendamentoRepositoryImpl implements IAgendamentoRepository{
    
    private static final List<Agendamento> DADOS = new ArrayList<>();

    @Override
    public List<Agendamento> listAll(){
        return Collections.unmodifiableList( DADOS );
    }

    @Override
    public List<Agendamento> listByData(final String data){
        if( data == null || data.strip().isEmpty() )
            return Collections.unmodifiableList( new ArrayList<>() );
        return DADOS.stream()
            .filter( item -> data.equals( item.getData() ) )
            .toList();
    }

    @Override
    public Optional<Agendamento> findOne(final String data, final String hora){
        if( data == null || data.strip().isEmpty() || hora == null || hora.strip().isEmpty() )
            return Optional.empty();
        return DADOS.stream()
            .filter( item -> data.equals( item.getData() ) && hora.equals( item.getHora() ) )
            .findAny();
    }

    @Override
    public Optional<Agendamento> insert(final Agendamento agend) throws Exception{
        if( DADOS.contains( agend ) )
            throw new Exception( "ERRO: Agendamento já existente!" );
        agend.setCadastro( new Date() );
        DADOS.add( agend );
        return Optional.of( agend );
    }

    @Override
    public Optional<Agendamento> update(final Agendamento agend) throws Exception{
        final int index = DADOS.indexOf( agend );
        if( index < 0 )
            throw new Exception( "ERRO: Agendamento não existente para atualização!" );
        agend.setCadastro( DADOS.get( index ).getCadastro() );
        agend.setAlteracao( new Date() );
        DADOS.set( index, agend );
        return Optional.of( agend );
    }

    @Override
    public Optional<Agendamento> delete(final String data, final String hora) throws Exception{
        final Optional<Agendamento> agend = this.findOne( data, hora );
        if( agend.isEmpty() )
            throw new Exception( "ERRO: Agendamento não existente para exclusão!" );
        DADOS.remove( agend.get() );
        return agend;
    }

}