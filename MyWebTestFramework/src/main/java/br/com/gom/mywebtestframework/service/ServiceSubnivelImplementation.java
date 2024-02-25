package br.com.gom.mywebtestframework.service;

import br.com.gom.webframework.annotations.WebFrameworkService;


@WebFrameworkService
public class ServiceSubnivelImplementation implements IServiceSubnivel{

    @Override
    public String chamadaCustomNivel2(String mensagem){
        return "Teste NIVEL DOIS: " + mensagem;
    }
    
}