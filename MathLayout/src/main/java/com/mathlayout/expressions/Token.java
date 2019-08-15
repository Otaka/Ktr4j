package com.mathlayout.expressions;

/**
 * @author sad
 */
public class Token {

    private String value;
    private TokenType tokenType;
    private int length;
    private int start;

    public Token(String value, TokenType tokenType) {
        this.value = value;
        this.tokenType = tokenType;
    }

    public int getLength() {
        return length;
    }

    public Token setLength(int length) {
        this.length = length;
        return this;
    }

    public int getStart() {
        return start;
    }

    public Token setStart(int start) {
        this.start = start;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    public String toString() {
        return "Token{" + "value=" + value + ", tokenType=" + tokenType + '}';
    }
}
