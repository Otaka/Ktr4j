package com.mathlayout.expressions;

import java.util.Arrays;

/**
 * @author sad
 */
public class StringParserStream {

    private char[] values;
    private int currentIndex;
    static private final char[] operatorsArray = new char[]{'+', '-', '*', '/', '%',';','='};

    static {
        Arrays.sort(operatorsArray);
    }

    public StringParserStream(char[] values) {
        this.values = values;
        currentIndex=0;
    }
    

    public void reset() {
        currentIndex = 0;
    }

    public boolean eof() {
        return currentIndex >= values.length;
    }

    public char getChar() {
        if (eof()) {
            return (char) -1;
        }
        char c = values[currentIndex];
        currentIndex++;
        return c;
    }

    public void rewind() {
        currentIndex--;
    }

    private boolean isOperator(char c) {
        return Arrays.binarySearch(operatorsArray, 0, operatorsArray.length , c) >= 0;
    }

    public void skipBlank() {
        while (true) {
            if (eof()) {
                return;
            }
            char c = getChar();
            if (!(c == ' ' || c == '\t' || c == '\n' || c == '\r')) {
                rewind();
                break;
            }
        }
    }

    public String readDigit() {
        StringBuilder sb = new StringBuilder();
        boolean alreadyHasPoint = false;
        while (true) {
            if (eof()) {
                break;
            }
            char c = getChar();
            if (Character.isDigit(c)) {
                sb.append(c);
            } else if (c == '.') {
                if (alreadyHasPoint) {
                    throw new ExpressionException("Parsing error [index " + currentIndex + "] digit already has separator '.'");
                } else {
                    sb.append(".");
                    alreadyHasPoint = true;
                }
            } else {
                rewind();
                break;
            }
        }
        return sb.toString();
    }

    public String readVar() {
        StringBuilder sb = new StringBuilder();
        boolean alreadyHasPoint = false;
        while (true) {
            if (eof()) {
                break;
            }
            char c = getChar();
            if (Character.isJavaIdentifierPart(c)) {
                sb.append(c);
            } else if (c == '.') {
                if (alreadyHasPoint) {
                    throw new ExpressionException("Parsing error [index " + currentIndex + "] digit already has separator '.'");
                } else {
                    sb.append(".");
                    alreadyHasPoint = true;
                }
            } else {
                rewind();
                break;
            }
        }
        return sb.toString();
    }

    public Token getToken() {
        skipBlank();
        if (eof()) {
            return null;
        }
        char c = getChar();
        if (isOperator(c)) {
            Token token = new Token("" + c, TokenType.operator).setLength(1).setStart(currentIndex - 1);
            return token;
        }
        if(c=='('||c==')'){
            Token token=new Token(""+c, TokenType.parenthesis);
            return token;
        }
        if (Character.isDigit(c)) {
            rewind();
            int lastIndex = currentIndex;
            String digit = readDigit();
            Token token = new Token(digit, TokenType.digit).setLength((currentIndex ) - lastIndex).setStart(lastIndex);
            return token;
        }
        if (Character.isJavaIdentifierStart(c)) {
            rewind();
            int lastIndex = currentIndex;
            String digit = readVar();
            Token token = new Token(digit, TokenType.var).setLength((currentIndex ) - lastIndex).setStart(lastIndex);
            return token;
        }
        
        rewind();
        throw new ExpressionException("Unrecognized construction " + new String(values, currentIndex, values.length-currentIndex));
    }
}
