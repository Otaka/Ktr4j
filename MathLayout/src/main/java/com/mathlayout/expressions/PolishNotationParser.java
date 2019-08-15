package com.mathlayout.expressions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class PolishNotationParser {

    // Associativity constants for operators
    private static final int LEFT_ASSOC = 0;
    private static final int RIGHT_ASSOC = 1;

    // Supported operators
    private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();

    static {
        // Map<"token", []{precendence, associativity}>
        OPERATORS.put(";", new int[]{4, LEFT_ASSOC});
        OPERATORS.put("=", new int[]{5, LEFT_ASSOC});
        OPERATORS.put("+", new int[]{10, LEFT_ASSOC});
        OPERATORS.put("-", new int[]{10, LEFT_ASSOC});
        OPERATORS.put("*", new int[]{30, LEFT_ASSOC});
        OPERATORS.put("/", new int[]{30, LEFT_ASSOC});
        OPERATORS.put("%", new int[]{60, LEFT_ASSOC});
    }

    /**
     * Test if a certain is an operator .
     * @param token The token to be tested .
     * @return True if token is an operator . Otherwise False .
     */
    private static boolean isOperator(Token token) {
        return token.getTokenType() == TokenType.operator;
    }

    /**
     * Test the associativity of a certain operator token .
     * @param token The token to be tested (needs to operator).
     * @param type LEFT_ASSOC or RIGHT_ASSOC
     * @return True if the tokenType equals the input parameter type .
     */
    private static boolean isAssociative(Token token, int type) {
        if (!isOperator(token)) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
        return OPERATORS.get(token.getValue())[1] == type;
    }

    /**
     * Compare precendece of two operators.
     * @param token1 The first operator .
     * @param token2 The second operator .
     * @return A negative number if token1 has a smaller precedence than token2,
     * 0 if the precendences of the two tokens are equal, a positive number
     * otherwise.
     */
    private static int cmpPrecedence(Token token1, Token token2) {
        if (!isOperator(token1) || !isOperator(token2)) {
            throw new IllegalArgumentException("Invalied tokens: " + token1
                    + " " + token2);
        }
        return OPERATORS.get(token1.getValue())[0] - OPERATORS.get(token2.getValue())[0];
    }

    public static List<Token> infixToRPN(List<Token> inputTokens) {
        ArrayList<Token> out = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        // For all the input tokens read the next token
        for (Token token : inputTokens) {
            if (token.getValue().equals("(")) {
                stack.push(token);
            } else if (token.getValue().equals(")")) {

                while (!stack.empty() && !stack.peek().getValue().equals("(")) {
                    out.add(stack.pop());
                }

                stack.pop();
            }else
            if (token.getTokenType() == TokenType.operator) {
                // If token is an operator (x)
                while (!stack.empty() && isOperator(stack.peek())) {
                    // [S4]
                    if ((isAssociative(token, LEFT_ASSOC)  && cmpPrecedence(token, stack.peek()) <= 0)
                            || (isAssociative(token, RIGHT_ASSOC) && cmpPrecedence(token, stack.peek()) < 0)) {
                        out.add(stack.pop());
                        continue;
                    }
                    break;
                }
                // Push the new operator on the stack 
                stack.push(token);
            } else {
                out.add(token);
            }
        }

        while (!stack.empty()) {
            out.add(stack.pop());
        }

        List<Token> output = new LinkedList<>();
        output.addAll(out);
        return output;
    }

    public List<Token> parseToPolishNotation(List<Token> tokens) {
        return infixToRPN(tokens);
    }
}
