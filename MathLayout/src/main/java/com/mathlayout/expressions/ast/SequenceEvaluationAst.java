package com.mathlayout.expressions.ast;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.ast.base.BaseBinaryOperatorAst;

/**
 * @author Dmitry
 */
public class SequenceEvaluationAst extends BaseBinaryOperatorAst {

    /**
     * This ast handles cases like following:<br>
     * y=34;x=21
     * where we have just sequence of expressions. It returns just latest to caller
     */
    public SequenceEvaluationAst(ExpressionParser expressionParser) {
        super(expressionParser);
    }

    @Override
    public float processValues(float left, float right) {
        return right;
    }

    @Override
    public String toString() {
        return ""+leftOperand+" ; "+rightOperand;
    }

}
