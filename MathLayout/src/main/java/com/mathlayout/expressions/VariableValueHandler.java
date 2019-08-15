package com.mathlayout.expressions;

/**
 * @author Dmitry
 */
public interface VariableValueHandler {
    public boolean getVariableValue(String variableName,MutableFloat result);
    public boolean setVariableValue(String variableName, float variableValue);
}
