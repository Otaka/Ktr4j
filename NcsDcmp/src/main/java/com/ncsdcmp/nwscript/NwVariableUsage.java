package com.ncsdcmp.nwscript;

/**
 * @author sad
 */
public class NwVariableUsage {

    NwScriptVar scriptVar;

    public NwVariableUsage(NwScriptVar scriptVar) {
        this.scriptVar = scriptVar;
    }

    public NwScriptVar getScriptVar() {
        return scriptVar;
    }

    @Override
    public String toString() {
        if(scriptVar==null){
            return "Not initialized";
        }
        return scriptVar.toString();
    }

}
