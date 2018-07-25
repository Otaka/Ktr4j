package com.kotorresearch.script.assembler;

import java.util.Map;

/**
 * @author Dmitry
 */
public class DisassembledLine {

    private String line;
    private int offset;
    private int byteLength;
    private String label;
    private String byteDumpString;
    private Map<String, Object> attributes;

    public DisassembledLine(String line, int offset, int byteLength) {
        this.line = line;
        this.offset = offset;
        this.byteLength = byteLength;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setByteDumpString(String byteDumpString) {
        this.byteDumpString = byteDumpString;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getLabel() {
        return label;
    }

    public String getByteDumpString() {
        return byteDumpString;
    }

    public int getBytesLength() {
        return byteLength;
    }

    public String getLine() {
        return line;
    }

    public int getOffset() {
        return offset;
    }

}
