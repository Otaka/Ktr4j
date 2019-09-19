package com.mathlayout.expressions.ast;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.ast.base.BaseBinaryOperatorAst;

/**
 * @author Dmitry
 */
public class AssignAst extends BaseBinaryOperatorAst {

    public AssignAst(ExpressionParser expressionParser) {
        super(expressionParser);
    }

    @Override
    public float process() {
        if (!(leftOperand instanceof VariableAst)) {
            throw new IllegalArgumentException("You should put variable name at left of '=' but you have provided [" + leftOperand + "] of class ["+leftOperand.getClass().getSimpleName()+"]");
        }
        VariableAst varAst = (VariableAst) leftOperand;
        float result=rightOperand.process();
        expressionParser.setVariable(varAst.getVariableName(), result);
        return result;
    }

    @Override
    public float processValues(float left, float right) {
        throw new UnsupportedOperationException("Should not be implemented");
    }

    @Override
    public String toString() {
        return "" + leftOperand + "=" + rightOperand;
    }

}
