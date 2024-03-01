package br.com.gom.mywebtestframework.repository;

import br.com.gom.webframework.annotations.WebFrameworkRepository;


@WebFrameworkRepository
public class RepositoryImplementation implements IRepository{

    @Override
    public String chamadaCustomRepository(String mensagem){
        return "Teste Repository: " + mensagem;
    }
    
}