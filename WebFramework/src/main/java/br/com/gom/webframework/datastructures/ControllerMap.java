package br.com.gom.webframework.datastructures;

import java.util.HashMap;
import java.util.Map;


public class ControllerMap{

    private ControllerMap(){
        super();
    }

    public static final Map<String, RequestControllerData> values = new HashMap<>();
    
}