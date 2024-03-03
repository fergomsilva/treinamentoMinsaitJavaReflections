package br.com.gom.jarareflection.avaliacao.service;

import static br.com.gom.jarareflection.avaliacao.util.Utils.isEmpty;

import java.util.List;
import java.util.Optional;

import br.com.gom.jarareflection.avaliacao.exception.RegistrationException;
import br.com.gom.jarareflection.avaliacao.exception.ValidationException;
import br.com.gom.jarareflection.avaliacao.model.Produto;
import br.com.gom.jarareflection.avaliacao.repository.IProdutoRepository;
import br.com.gom.webframework.annotations.WebFrameworkInject;
import br.com.gom.webframework.annotations.WebFrameworkService;


@WebFrameworkService
public class ProdutoServiceImp implements IProdutoService{

    @WebFrameworkInject
    private IProdutoRepository repository;


    @Override
    public List<Produto> list(){
        return this.repository.listAll();
    }

    @Override
    public List<Produto> find(final String termo, final String departamento){
        return this.repository.find( termo, departamento );
    }

    @Override
    public Optional<Produto> get(final long id){
        return this.repository.findById( id );
    }

    private void isDadosValidos(final Produto obj, final boolean incluir) 
    throws ValidationException, RegistrationException{
        if( obj == null )
            throw new RegistrationException( ( incluir ? "incluir" : "alterar" ), 
                "produto", "produto não informado" );
        if( isEmpty( obj.getNome() ) )
            throw new ValidationException( "nome" );
        if( isEmpty( obj.getDepartamento() ) )
            throw new ValidationException( "departamento" );
        if( isEmpty( obj.getDescricao() ) )
            throw new ValidationException( "descrição" );
        if( obj.getPreco() == 0.0 )
            throw new ValidationException( "preço" );
        else if( obj.getPreco() < 0.0 )
            throw new ValidationException( "O %s não pode ser negativo.", "preço" );
        if( isEmpty( obj.getFabricante() ) )
            throw new ValidationException( "fabricante" );
        if( isEmpty( obj.getMarca() ) )
            throw new ValidationException( "marca" );
        if( isEmpty( obj.getModelo() ) )
            throw new ValidationException( "modelo" );
    }

    @Override
    public Produto insert(final Produto obj) throws RegistrationException, ValidationException{
        this.isDadosValidos( obj, true );
        return this.repository.insert( obj );
    }

    @Override
    public Produto update(final Produto obj) throws RegistrationException, ValidationException{
        this.isDadosValidos( obj, false );
        return this.repository.update( obj );
    }

    @Override
    public Produto update(final long id, final double price) throws RegistrationException, ValidationException{
        final Produto prod = this.get( id )
            .orElseThrow( () -> new RegistrationException( "atualizar", 
                "valor do produto", "produto não encontrado" ) )
            .copy();
        prod.setPreco( price );
        this.isDadosValidos( prod, false );
        return this.update( prod );
    }

    @Override
    public Produto delete(final long id) throws RegistrationException{
        return this.repository.delete( id );
    }
    
}