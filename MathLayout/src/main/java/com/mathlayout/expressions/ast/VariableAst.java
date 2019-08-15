package com.mathlayout.expressions.ast;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.ast.base.BaseAst;

/**
 * @author Dmitry
 */
public class VariableAst extends BaseAst {

    private String variableName;

    public VariableAst(ExpressionParser expressionParser, String variableName) {
        super(expressionParser);
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    @Override
    public float process() {
        return expressionParser.getVariable(variableName);
    }

    @Override
    public String toString() {
        return variableName;
    }

}
