package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.gff.GffLoadContext;

/**
 * @author sad
 */
public class GffByte extends GffFieldValue {

    private int value;

    public int getValue() {
        return value;
    }

    @Override
    public void load(GffLoadContext loadContext, int dataOrOffset) {
        value = dataOrOffset;
    }

    @Override
    public int getAsInt() {
        return value;
    }

    @Override
    public boolean getAsBoolean() {
        return (value == 0) ? false : true;
    }

}
