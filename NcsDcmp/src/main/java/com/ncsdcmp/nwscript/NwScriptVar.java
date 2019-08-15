package com.ncsdcmp.nwscript;

/**
 * @author sad
 */
public class NwScriptVar {

    private String type;
    private String name;
    private Object initializationVariable;

    public NwScriptVar(String type, String name, Object initializationVariable) {
        this.type = type;
        this.name = name;
        this.initializationVariable = initializationVariable;
    }

    public Object getInitializationVariable() {
        return initializationVariable;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " " + name;
    }
}
