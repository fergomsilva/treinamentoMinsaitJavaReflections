package br.com.gom.jarareflection.avaliacao.model;

import java.util.ArrayList;
import java.util.List;

public class Produto{

    private Long id;
    private String nome;
    private String departamento;
    private String descricao;
    private double preco;
    private String fabricante;
    private String marca;
    private String modelo;
    private List<String> urlImagens;


    public Produto(){
        super();
    }

    @SuppressWarnings( { "all" } )
    public Produto(final Long id, final String nome, final String departamento, final String descricao, 
    final double preco, final String fabricante, final String marca, final String modelo, 
    final List<String> urlImagens){
        super();
        this.setId( id );
        this.setNome( nome );
        this.setPreco( preco );
        this.setMarca( marca );
        this.setModelo( modelo );
        this.setDescricao( descricao );
        this.setUrlImagens( urlImagens );
        this.setFabricante( fabricante );
        this.setDepartamento( departamento );
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

    public String getDepartamento(){
        return departamento;
    }
    public void setDepartamento(final String departamento){
        this.departamento = departamento;
    }

    public String getDescricao(){
        return descricao;
    }
    public void setDescricao(final String descricao){
        this.descricao = descricao;
    }

    public double getPreco(){
        return preco;
    }
    public void setPreco(final double preco){
        this.preco = preco;
    }

    public String getFabricante(){
        return fabricante;
    }
    public void setFabricante(final String fabricante){
        this.fabricante = fabricante;
    }

    public String getMarca(){
        return marca;
    }
    public void setMarca(final String marca){
        this.marca = marca;
    }

    public String getModelo(){
        return modelo;
    }
    public void setModelo(final String modelo){
        this.modelo = modelo;
    }

    public List<String> getUrlImagens(){
        if( this.urlImagens == null )
            this.urlImagens = new ArrayList<>();
        return this.urlImagens;
    }
    public void setUrlImagens(final List<String> urlImagens){
        this.urlImagens = urlImagens;
    }

    public Produto copy(){
        final Produto clone = new Produto( this.getId(), this.getNome(), this.getDepartamento(), 
            this.getDescricao(), this.getPreco(), this.getFabricante(), this.getMarca(), 
            this.getModelo(), null );
        this.getUrlImagens().forEach( img -> clone.getUrlImagens().add( img ) );
        return clone;
    }

    @Override
    public int hashCode(){
        return 31 * 1 + ( ( this.getId() == null) ? 0 : this.getId().hashCode() );
    }

    @Override
    public boolean equals(final Object obj){
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( this.getClass() != obj.getClass() )
            return false;
        final Produto other = (Produto) obj;
        if( this.getId() == null ){
            if( other.getId() != null )
                return false;
        }else if( !this.getId().equals( other.getId() ) )
            return false;
        return true;
    }

}