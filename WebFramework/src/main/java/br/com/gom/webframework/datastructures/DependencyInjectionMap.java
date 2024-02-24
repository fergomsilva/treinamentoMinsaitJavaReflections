package br.com.gom.webframework.datastructures;

import java.util.HashMap;
import java.util.Map;

public class DependencyInjectionMap{

    private DependencyInjectionMap(){
        super();
    }

    public static final Map<String, Object> objects = new HashMap<>();
}