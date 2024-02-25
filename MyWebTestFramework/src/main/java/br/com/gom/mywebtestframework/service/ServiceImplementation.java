package br.com.gom.mywebtestframework.service;

import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.WebFrameworkService;


@WebFrameworkService
public class ServiceImplementation implements IService{

    @WebFrameworkInject
    private IServiceSubnivel serviceSubnivel;

    @Override
    public String chamadaCustom(String mensagem){
        return "Teste: " + mensagem;
    }

    @Override
    public String chamadaCustomSubnivel(final String mensagem){
        return this.serviceSubnivel.chamadaCustomNivel2( mensagem );
    }
    
}