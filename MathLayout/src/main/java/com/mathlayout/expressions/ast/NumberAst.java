package com.mathlayout.expressions.ast;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.ast.base.BaseAst;


/**
 * @author sad
 */
public class NumberAst extends BaseAst {

    private final float value;

    public NumberAst(float value,ExpressionParser expressionParser) {
        super(expressionParser);
        this.value = value;
    }

    @Override
    public float process() {
        return value;
    }

    @Override
    public String toString() {
        return ""+value;
    }

}
