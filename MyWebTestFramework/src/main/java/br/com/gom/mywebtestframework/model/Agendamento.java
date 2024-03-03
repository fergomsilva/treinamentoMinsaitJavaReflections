package br.com.gom.mywebtestframework.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Agendamento implements Comparable<Agendamento>, Comparator<Agendamento>{
    
    private String data;
    private String hora;
    private String titulo;
    private String descricao;
    private List<String> participantes;
    private Date cadastro;
    private Date alteracao;


    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getParticipantes() {
        if( this.participantes == null )
            this.participantes = new ArrayList<>();
        return this.participantes;
    }
    public void setParticipantes(List<String> participantes) {
        this.participantes = participantes;
    }

    public Date getCadastro() {
        return cadastro;
    }
    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public Date getAlteracao() {
        return alteracao;
    }
    public void setAlteracao(Date alteracao) {
        this.alteracao = alteracao;
    }

    @Override
    public int compare(final Agendamento o1, final Agendamento o2){
        return o1.compareTo( o2 );
    }

    @Override
    public int compareTo(final Agendamento o){
        int c = this.getData().compareTo( o.getData() );
        if( c == 0 )
            c = this.getHora().compareTo( o.getHora() );
        return -( c );
    }

    @Override
    public boolean equals(final Object obj){
        if( this == obj )
            return true;
        if( obj == null )
            return false;
        if( this.getClass() != obj.getClass() )
            return false;
        final Agendamento other = (Agendamento)obj;
        return ( this.getData().equals( other.getData() ) ) 
            && ( this.getHora().equals( other.getHora() ) );
    }

    @Override
    public int hashCode(){
        return Objects.hash( this.getData(), this.getHora() );
    }

}