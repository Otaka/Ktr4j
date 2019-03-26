package com.ncsdcmp;

/**
 * @author sad
 */
class FunctionInfo {

    private int address;

    public FunctionInfo(int address) {
        this.address = address;
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

}
