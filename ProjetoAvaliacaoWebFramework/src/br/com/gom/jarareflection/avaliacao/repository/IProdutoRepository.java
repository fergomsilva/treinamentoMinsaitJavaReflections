package br.com.gom.jarareflection.avaliacao.repository;

import java.util.List;
import java.util.Optional;

import br.com.gom.jarareflection.avaliacao.exception.RegistrationException;
import br.com.gom.jarareflection.avaliacao.model.Produto;


public interface IProdutoRepository{
    
    List<Produto> listAll();

    List<Produto> find(final String termo, final String departamento);

    Optional<Produto> findById(final long id);

    Produto insert(final Produto obj) throws RegistrationException;

    Produto update(final Produto obj) throws RegistrationException;

    Produto delete(final long id) throws RegistrationException;

}