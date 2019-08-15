package com.mathlayout.expressions;

import com.mathlayout.expressions.ast.*;
import com.mathlayout.expressions.ast.base.BaseAst;
import com.mathlayout.expressions.ast.base.BaseBinaryOperatorAst;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class ExpressionParser {

    private Map<String, Float> variableToValueMap = new HashMap<>();
    private List<VariableValueHandler> variableValueHandlers = new ArrayList<>();

    public void addVariableValueHandler(VariableValueHandler vvh) {
        variableValueHandlers.add(vvh);
    }

    public boolean hasVariable(String varName) {
        if (variableToValueMap.containsKey(varName)) {
            return true;
        }
        MutableFloat result = new MutableFloat();
        for (VariableValueHandler vvh : variableValueHandlers) {
            if (vvh.getVariableValue(varName, result)) {
                return true;
            }
        }

        return false;
    }

    public float getVariable(String varName) {
        if (variableToValueMap.containsKey(varName)) {
            return variableToValueMap.get(varName);
        }
        MutableFloat result = new MutableFloat();
        for (VariableValueHandler vvh : variableValueHandlers) {
            if (vvh.getVariableValue(varName, result)) {
                return result.getValue();

            }
        }

        throw new IllegalArgumentException(
                "Do not have defined variable [" + varName + "]");
    }

    public void createAndSetSimpleVariable(String varName, float value) {
        variableToValueMap.put(varName, value);
    }

    public void setVariable(String varName, float value) {
        if (variableToValueMap.containsKey(varName)) {
            variableToValueMap.put(varName, value);
        }
        for (VariableValueHandler vvh : variableValueHandlers) {
            if (vvh.setVariableValue(varName, value)) {
                return;
            }
        }

        throw new IllegalArgumentException("Do not have defined variable [" + varName + "]");
    }

    private List<Token> parseStringToTokens(String string) {
        List<Token> tokens = new ArrayList<>();
        StringParserStream stream = new StringParserStream(string.toCharArray());
        Token token = stream.getToken();
        while (token != null) {
            tokens.add(token);
            token = stream.getToken();
        }
        return tokens;
    }

    public BaseAst parseString(String stringToParse) throws Exception {
        List<Token> tokens = parseStringToTokens(stringToParse);
        PolishNotationParser parser = new PolishNotationParser();
        List<Token> polishTokens = parser.parseToPolishNotation(tokens);
        BaseAst ast = convertToAst(polishTokens);
        return ast;
    }

    private BaseAst parseDigit(Token token) {
        String value = token.getValue();
        try {
            return new NumberAst(Float.parseFloat(value), this);
        } catch (NumberFormatException ex) {
            throw new ExpressionException("Digit should be a parseable float value [position:" + token.getStart() + "]");
        }
    }

    private BaseAst parseVariable(Token token) {
        return new VariableAst(this, token.getValue());
    }

    private BaseAst convertToAst(List<Token> polishNotation) throws Exception {
        List res = new LinkedList();
        res.addAll(polishNotation);
        while (true) {
            boolean processed = false;
            for (int i = 0; i < res.size(); i++) {
                Object obj = res.get(i);
                if (obj instanceof Token) {
                    processed = true;
                    Token t = (Token) obj;
                    if (null != t.getTokenType()) {
                        switch (t.getTokenType()) {
                            case digit:
                                res.set(i, parseDigit(t));
                                break;
                            case var:
                                res.set(i, parseVariable(t));
                                break;
                            default:
                                parseOperator(t, i, res);
                                break;
                        }
                    }
                    break;
                }
            }
            if (processed == false) {
                break;
            }
        }
        return (BaseAst) res.get(0);
    }

    private static final Map<String, Constructor> binaryOperatorToConstructors = new HashMap<String, Constructor>() {
        {
            try {
                put("+", AddAst.class
                        .getConstructor(ExpressionParser.class
                        ));
                put("-", SubAst.class
                        .getConstructor(ExpressionParser.class
                        ));
                put("*", MultiplyAst.class
                        .getConstructor(ExpressionParser.class
                        ));
                put("/", DivideAst.class
                        .getConstructor(ExpressionParser.class
                        ));
                put("%", PercentAst.class
                        .getConstructor(ExpressionParser.class
                        ));
                put("=", AssignAst.class
                        .getConstructor(ExpressionParser.class
                        ));
                put(";", SequenceEvaluationAst.class
                        .getConstructor(ExpressionParser.class
                        ));
            } catch (NoSuchMethodException | SecurityException ex) {
                throw new IllegalStateException(ex);
            }
        }
    };

    private BaseAst parseOperator(Token token, int currentIndex, List tokens) throws Exception {
        String operator = token.getValue();
        if (binaryOperatorToConstructors.containsKey(operator)) {
            BaseBinaryOperatorAst ast = (BaseBinaryOperatorAst) binaryOperatorToConstructors.get(operator).newInstance(this);
            ast.setLeftOperand((BaseAst) tokens.get(currentIndex - 2));
            ast.setRightOperand((BaseAst) tokens.get(currentIndex - 1));
            tokens.set(currentIndex, ast);
            tokens.remove(currentIndex - 2);
            tokens.remove(currentIndex - 2);
            return ast;
        }

        throw new RuntimeException("Cannot process token " + token.toString() + " as operator");
    }
}
