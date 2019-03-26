package com.ncsdcmp;

import java.util.Arrays;

/**
 * @author sad
 */
class Line {

    private int address;
    private String opcode;
    private Object[] args;

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
