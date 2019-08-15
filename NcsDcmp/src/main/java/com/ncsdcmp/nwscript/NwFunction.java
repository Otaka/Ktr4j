package com.ncsdcmp.nwscript;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sad
 */
public class NwFunction {

    private int functionIndex;
    private String returnType;
    private String name;
    private List<NwScriptVar> args = new ArrayList<>();

    public void setFunctionIndex(int functionIndex) {
        this.functionIndex = functionIndex;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public List<NwScriptVar> getArgs() {
        return args;
    }

    public int getFunctionIndex() {
        return functionIndex;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }
}
