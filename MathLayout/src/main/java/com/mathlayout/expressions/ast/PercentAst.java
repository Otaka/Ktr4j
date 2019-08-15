package com.mathlayout.expressions.ast;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.ast.base.BaseBinaryOperatorAst;

/**
 * @author sad
 */
public class PercentAst extends BaseBinaryOperatorAst {

    public PercentAst(ExpressionParser expressionParser) {
        super(expressionParser);
    }

    @Override
    public float processValues(float left, float right) {
        return left / 100.0f * right;
    }
@Override
    public String toString() {
        return "("+leftOperand+"%"+rightOperand+")";
    }
}
