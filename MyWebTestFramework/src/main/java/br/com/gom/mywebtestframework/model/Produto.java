package br.com.gom.mywebtestframework.model;

import java.util.Objects;

public class Produto{
    
    private Long id;
    private String nome;
    private Double valor;
    private String linkFoto;


    public Produto(){
        super();
    }

    public Produto(final Long id, final String nome, final Double valor, final String linkFoto){
        this();
        this.setId( id );
        this.setNome( nome );
        this.setValor( valor );
        this.setLinkFoto( linkFoto );
    }

    public Long getId(){
        return id;
    }
    public void setId(final Long id){
        this.id = id;
    }

    public String getNome(){
        return nome;
    }
    public void setNome(final String nome){
        this.nome = nome;
    }
    
    public Double getValor(){
        return valor;
    }
    public void setValor(final Double valor){
        this.valor = valor;
    }

    public String getLinkFoto(){
        return linkFoto;
    }
    public void setLinkFoto(final String linkFoto){
        this.linkFoto = linkFoto;
    }

    @Override
    public boolean equals(final Object obj){
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( this.getClass() != obj.getClass() )
            return false;
        final Produto other = (Produto)obj;
        return ( this.getId() == other.getId() );
    }

    @Override
    public int hashCode(){
        return Objects.hash( this.getId() );
    }

}