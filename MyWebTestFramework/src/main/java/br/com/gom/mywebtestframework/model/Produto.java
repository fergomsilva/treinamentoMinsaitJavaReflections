package br.com.gom.mywebtestframework.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of={ "id" } )
public class Produto{
    
    public Long id;
    public String nome;
    public Double valor;
    public String linkFoto;

}