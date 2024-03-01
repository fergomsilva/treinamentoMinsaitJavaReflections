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
    private Class<?> paramClassFromMethod;
    private @Builder.Default int indexParameterMethod = -1;
    

    public boolean isParameter(){
        return this.getIndexParameterMethod() >= 0 && this.getParamClassFromMethod() != null;
    }
    
    @Override
    public String toString(){
        return this.isParameter() ? String.format( "path[%s];isParameter[%s];Class[%s];index[%d];", 
                this.getPath(), this.isParameter(), this.getParamClassFromMethod(), this.getIndexParameterMethod() )
            : String.format( "path[%s];isParameter[%s];", this.getPath(), this.isParameter() );
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