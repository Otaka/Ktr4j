package com.mathlayout;

import com.mathlayout.expressions.ast.base.BaseAst;

/**
 * @author Dmitry
 */
public class LayoutExpression {

    private String destination;
    private String ruleText;
    private BaseAst computationAst;

    public LayoutExpression(String destination, String ruleText, BaseAst computationAst) {
        this.destination = destination;
        this.ruleText = ruleText;
        this.computationAst = computationAst;
    }

    public String getRuleText() {
        return ruleText;
    }

    
    public BaseAst getComputationAst() {
        return computationAst;
    }

    public String getDestination() {
        return destination;
    }

}
