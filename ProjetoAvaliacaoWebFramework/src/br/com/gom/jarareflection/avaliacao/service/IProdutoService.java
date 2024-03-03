package br.com.gom.jarareflection.avaliacao.service;

import java.util.List;
import java.util.Optional;

import br.com.gom.jarareflection.avaliacao.exception.RegistrationException;
import br.com.gom.jarareflection.avaliacao.exception.ValidationException;
import br.com.gom.jarareflection.avaliacao.model.Produto;


public interface IProdutoService{
    
    List<Produto> list();

    List<Produto> find(final String termo, final String departamento);

    Optional<Produto> get(final long id);

    Produto insert(final Produto obj) throws RegistrationException, ValidationException;

    Produto update(final Produto obj) throws RegistrationException, ValidationException;

    Produto update(final long id, final double price) throws RegistrationException, ValidationException;

    Produto delete(final long id) throws RegistrationException;

}