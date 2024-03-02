package br.com.gom.webframework.datastructures;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MethodParam{
    
    private String method;
    private String param;

}