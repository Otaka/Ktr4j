package com.mathlayout.expressions.ast;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.ast.base.BaseBinaryOperatorAst;


/**
 * @author sad
 */
public class SubAst extends BaseBinaryOperatorAst {

    public SubAst(ExpressionParser expressionParser) {
        super(expressionParser);
    }

    @Override
    public float processValues(float left, float right) {
        return left - right;
    }

    @Override
    public String toString() {
        return "("+leftOperand+"-"+rightOperand+")";
    }
    
}
