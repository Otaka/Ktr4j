package com.mathlayout.expressions;

import com.mathlayout.expressions.ast.base.BaseAst;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dmitry
 */
public class ExpessionParserTest {

    @Test
    public void testSimpleEvaluation() throws Exception {
        ExpressionParser expressionParser = new ExpressionParser();
        BaseAst ast = expressionParser.parseString("1+2");
        assertEquals(3, ast.process(), 0.001);

        ast = expressionParser.parseString("10/3");
        assertEquals(3.333333, ast.process(), 0.001);

        ast = expressionParser.parseString("10*3");
        assertEquals(30, ast.process(), 0.001);

        ast = expressionParser.parseString("10-3");
        assertEquals(7, ast.process(), 0.001);

        ast = expressionParser.parseString("200%3");
        assertEquals(6, ast.process(), 0.001);
    }

    @Test
    public void testParenhesisEvaluation() throws Exception {
        ExpressionParser expressionParser = new ExpressionParser();
        BaseAst ast = expressionParser.parseString("2+2*2");
        assertEquals(6, ast.process(), 0.001);

        ast = expressionParser.parseString("(2+2)*2");
        assertEquals(8, ast.process(), 0.001);
    }

    @Test
    public void testVariableEvaluation() throws Exception {
        ExpressionParser expressionParser = new ExpressionParser();
        expressionParser.addVariableValueHandler(createTestVariableHandler());
        expressionParser.createAndSetSimpleVariable("v.x", 3);
        BaseAst ast = expressionParser.parseString("y+2*v.x");
        assertEquals(10, ast.process(), 0.001);
    }
    
    @Test
    public void testSetVariable() throws Exception {
        ExpressionParser expressionParser = new ExpressionParser();
        expressionParser.addVariableValueHandler(createTestVariableHandler());
        BaseAst ast = expressionParser.parseString("y=2+9");
        assertEquals(11, ast.process(), 0.001);
        assertEquals(11, expressionParser.getVariable("y"), 0.001);
    }
    
    @Test
    public void testSequenceVariable() throws Exception {
        ExpressionParser expressionParser = new ExpressionParser();
        expressionParser.addVariableValueHandler(createTestVariableHandler());
        BaseAst ast = expressionParser.parseString("y=15+4;z=98");
        assertEquals(98, ast.process(), 0.001);
        assertEquals(19, expressionParser.getVariable("y"), 0.001);
        assertEquals(98, expressionParser.getVariable("z"), 0.001);
    }

    private VariableValueHandler createTestVariableHandler() {
        return new VariableValueHandler() {

            private float yValue = 4;
            private float zValue = 0;

            @Override
            public boolean getVariableValue(String variableName, MutableFloat result) {
                if (variableName.equals("y")) {
                    result.setValue(yValue);
                    return true;
                }
                if (variableName.equals("z")) {
                    result.setValue(zValue);
                    return true;
                }
                return false;
            }

            @Override
            public boolean setVariableValue(String variableName, float variableValue) {
                if (variableName.equals("y")) {
                    yValue = variableValue;
                    return true;
                }
                if (variableName.equals("z")) {
                    zValue = variableValue;
                    return true;
                }
                return false;
            }
        };
    }
}
