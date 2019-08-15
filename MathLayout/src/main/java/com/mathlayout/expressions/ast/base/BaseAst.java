package com.mathlayout.expressions.ast.base;

import com.mathlayout.expressions.ExpressionParser;

/**
 * @author sad
 */
public abstract class BaseAst {

    protected ExpressionParser expressionParser;

    public BaseAst(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    public abstract float process();
}
