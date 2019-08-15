package com.ncsdcmp;

/**
 * @author sad
 */
public class FunctionInfo {

    private int address;
    private int length;
    private BasicBlock rootBasicBlock;

    public void setRootBasicBlock(BasicBlock rootBasicBlock) {
        this.rootBasicBlock = rootBasicBlock;
    }

    public BasicBlock getRootBasicBlock() {
        return rootBasicBlock;
    }

    public FunctionInfo(int address) {
        this.address = address;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public int getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.address;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FunctionInfo other = (FunctionInfo) obj;
        if (this.address != other.address) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Function "+Integer.toHexString(address)+":"+Integer.toHexString(address+length);
    }

}
