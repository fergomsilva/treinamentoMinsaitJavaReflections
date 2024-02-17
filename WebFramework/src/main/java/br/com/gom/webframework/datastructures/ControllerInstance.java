package br.com.gom.webframework.datastructures;

import java.util.HashMap;
import java.util.Map;


public class ControllerInstance{

    private ControllerInstance(){
        super();
    }

    public static final Map<String, Object> instances = new HashMap<>();
    
}