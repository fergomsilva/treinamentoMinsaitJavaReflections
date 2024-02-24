package br.com.gom.mywebtestframework.service;

import br.com.gom.webframework.annotations.WebFrameworkService;


@WebFrameworkService
public class ServiceImplementation implements IService{

    @Override
    public String chamadaCustom(String mensagem) {
        return "Teste: " + mensagem;
    }
    
}