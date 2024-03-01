package br.com.gom.mywebtestframework.model;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of={ "data", "hora" } )
public class Agendamento implements Comparable<Agendamento>, Comparator<Agendamento>{
    
    private String data;
    private String hora;
    private String titulo;
    private String descricao;
    private List<String> participantes;
    private Date cadastro;
    private Date alteracao;


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

}