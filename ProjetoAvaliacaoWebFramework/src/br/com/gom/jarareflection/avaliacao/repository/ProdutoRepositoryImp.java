package br.com.gom.jarareflection.avaliacao.repository;

import static br.com.gom.jarareflection.avaliacao.bdmock.BD_MOCK.PRODUTOS;
import static br.com.gom.jarareflection.avaliacao.util.Utils.isNotEmpty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import br.com.gom.jarareflection.avaliacao.exception.RegistrationException;
import br.com.gom.jarareflection.avaliacao.model.Produto;
import br.com.gom.webframework.annotations.WebFrameworkRepository;


@WebFrameworkRepository
public class ProdutoRepositoryImp implements IProdutoRepository{
    public static final String TXT_ENTIDADE = "produto";
    private static final String MSG_PROD_NAO_EXISTE = "produto não existe";

    @Override
    public List<Produto> listAll(){
        return Collections.unmodifiableList( PRODUTOS );
    }

    @Override
    public List<Produto> find(final String termo, final String departamento){
        if( isNotEmpty( termo ) || isNotEmpty( departamento ) ){
            return PRODUTOS.stream()
                .filter( item -> {
                    boolean ok = isNotEmpty( departamento ) && item.getDepartamento()
                        .equalsIgnoreCase( departamento );
                    if( ok && isNotEmpty( termo ) ){
                        ok = String.join( "|", item.getNome().strip(), item.getDescricao().strip(), 
                            item.getMarca().strip(), item.getModelo().strip() ).toLowerCase().contains( 
                            termo.strip().toLowerCase() );
                    }
                    return ok;
                } ).toList();
        }
        return Collections.unmodifiableList( new ArrayList<>() );
    }

    @Override
    public Optional<Produto> findById(final long id) {
        return PRODUTOS.stream()
            .filter( item -> id == item.getId().longValue() )
            .findFirst();
    }

    @Override
    public Produto insert(final Produto obj) throws RegistrationException{
        obj.setId( PRODUTOS.stream()
            .map( Produto::getId )
            .max( Long::compare )
            .orElseGet( ()->0l ) + 1 );
        PRODUTOS.add( obj );
        return obj;
    }

    @Override
    public Produto update(final Produto obj) throws RegistrationException{
        final Produto prod = this.findById( obj.getId() )
            .orElseThrow( () -> new RegistrationException( "atualizar", 
                TXT_ENTIDADE, MSG_PROD_NAO_EXISTE ) )
            .copy();
        final int index = PRODUTOS.indexOf( prod );
        PRODUTOS.set( index, obj );
        return PRODUTOS.get( index );
    }

    @Override
    public Produto delete(final long id) throws RegistrationException{
        final Produto produto = this.findById( id )
            .orElseThrow( ()->new RegistrationException( "excluir", 
                TXT_ENTIDADE, MSG_PROD_NAO_EXISTE ) );
        PRODUTOS.remove( produto );
        return produto;
    }
    
}