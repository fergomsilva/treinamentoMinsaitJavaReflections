package br.com.gom.mywebtestframework.service;

import br.com.gom.mywebtestframework.repository.IRepository;
import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.WebFrameworkService;


@WebFrameworkService
public class ServiceImplementation implements IService{

    @WebFrameworkInject
    private IRepository repository;

    @Override
    public String chamadaCustom(String mensagem){
        return "Teste: " + mensagem;
    }

    @Override
    public String chamadaCustomRepository(final String mensagem){
        return this.repository.chamadaCustomRepository( mensagem );
    }
    
}