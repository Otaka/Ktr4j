package com.ncsdcmp;

import java.util.Arrays;

/**
 * @author sad
 */
public class Line {

    private int address;
    private String opcode;
    private Object[] args;
    private BasicBlock basicBlock;
    private int instructionBytesLength;

    public void setInstructionBytesLength(int instructionBytesLength) {
        this.instructionBytesLength = instructionBytesLength;
    }

    public int getInstructionBytesLength() {
        return instructionBytesLength;
    }

    public void setBasicBlock(BasicBlock basicBlock) {
        this.basicBlock = basicBlock;
    }

    public BasicBlock getBasicBlock() {
        return basicBlock;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        if (args != null) {
            return "" + address + ":" + opcode + Arrays.deepToString(args);
        } else {
            return "" + address + ":" + opcode;
        }
    }

}
