package com.mathlayout.expressions.ast.base;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.ast.VariableAst;

/**
 * @author sad
 */
public abstract class BaseBinaryOperatorAst extends BaseAst {

    protected BaseAst leftOperand;
    protected BaseAst rightOperand;

    public BaseBinaryOperatorAst(ExpressionParser expressionParser) {
        super(expressionParser);
    }

    public BaseAst getLeftOperand() {
        return leftOperand;
    }

    public void setLeftOperand(BaseAst leftOperand) {
        this.leftOperand = leftOperand;
    }

    public BaseAst getRightOperand() {
        return rightOperand;
    }

    public void setRightOperand(BaseAst rightOperand) {
        this.rightOperand = rightOperand;
    }

    @Override
    public float process() {
        float left = leftOperand.process();
        float right = rightOperand.process();
        return processValues(left, right);
    }

    public abstract float processValues(float left, float right);

}
