package br.com.gom.webframework.datastructures.controllers;

import java.util.Comparator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode( of={ "path" } )
public class SplitUrlControllerData implements Comparable<SplitUrlControllerData>, Comparator<SplitUrlControllerData>{
    
    private String path;
    private boolean parameter;
    private Class<?> typeToMethod;
    private @Builder.Default int indexParameterMethod = -1;

    
    @Override
    public String toString(){
        return String.format( "path[%s];isParameter[%s];Type[%s];Index[%d]", 
            this.getPath(), this.isParameter(), this.getTypeToMethod(), this.getIndexParameterMethod() );
    }

    @Override
    public int compare(final SplitUrlControllerData o1, final SplitUrlControllerData o2){
        return o1.compareTo( o2 );
    }
    @Override
    public int compareTo(final SplitUrlControllerData o){
        return Integer.compare( this.getIndexParameterMethod(), o.getIndexParameterMethod() );
    }

}