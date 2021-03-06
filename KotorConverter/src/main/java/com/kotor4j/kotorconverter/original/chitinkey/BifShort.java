package com.kotor4j.kotorconverter.original.chitinkey;

/**
 * @author Dmitry
 */
public class BifShort {
    private final String biffPath;
    private final int size;

    public BifShort(String bif, int size) {
        this.biffPath = bif;
        this.size = size;
    }

    public String getBifFile() {
        return biffPath;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return biffPath + " size:" + size;
    }

}
