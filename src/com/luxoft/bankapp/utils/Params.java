package com.luxoft.bankapp.utils;

import java.util.HashMap;
import java.util.Map;

public class Params {
    private Map<String, String> parametersMap = new HashMap<>();
    
    public Params(String[] parameters){
        for (String param: parameters) {
        	try {
               parametersMap.put(param.split("=")[0], param.split("=")[1]);
        	} catch (ArrayIndexOutOfBoundsException e) {
        		
        	}
        }
    }

    public String get(String key){
        return parametersMap.get(key);
    }

}
