package com.luxoft.bankapp.utils;

public class Arguments {
    private String[] arguments;
    
    public Arguments(String[] arguments) {
        this.arguments = arguments;
    }
    
    public String get(String key){
        for (int i = 0; i < arguments.length;i++)
            if (arguments[i].equals(key))
                return arguments[i+1];
        return null;
    }

    public boolean hasKey(String key){
        for (String arg: arguments) {
            if (arg.equals(key)) {
                return true;
            }
        }
        return false;
    }
}