package br.com.gom.mywebtestframework.service;

import java.util.List;
import java.util.Optional;

import br.com.gom.mywebtestframework.model.Agendamento;
import br.com.gom.mywebtestframework.repository.IAgendamentoRepository;
import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.WebFrameworkService;


@WebFrameworkService
public class AgendaServiceImpl implements IAgendaService{
    
    @WebFrameworkInject
    private IAgendamentoRepository repository;


    @Override
    public List<Agendamento> listar(){
        return this.repository.listAll();
    }

    @Override
    public List<Agendamento> listarPorDia(final String data){
        return ( data == null || data.strip().isEmpty() ) ? this.listar() 
            : this.repository.listByData( data );
    }

    @Override
    public Optional<Agendamento> obter(final String data, final String hora) throws Exception{
        if( data == null || data.strip().isEmpty() )
            throw new Exception( "Data deve ser informada." );
        if( hora == null || hora.strip().isEmpty() )
            throw new Exception( "Hora deve ser informada." );
        return this.repository.findOne( data, hora );
    }

    @Override
    public Optional<Agendamento> inserir(final Agendamento agend) throws Exception{
        if( agend == null )
            throw new Exception( "Agendamento deve ser informado." );
        return this.repository.insert( agend );
    }

    @Override
    public Optional<Agendamento> atualizar(final Agendamento agend) throws Exception{
        if( agend == null )
            throw new Exception( "Agendamento deve ser informado." );
        return this.repository.update( agend );
    }

    @Override
    public Optional<Agendamento> excluir(final String data, final String hora) throws Exception{
        if( data == null || data.strip().isEmpty() )
            throw new Exception( "Data deve ser informada." );
        if( hora == null || hora.strip().isEmpty() )
            throw new Exception( "Hora deve ser informada." );
        return this.repository.delete( data, hora );
    }
    
}