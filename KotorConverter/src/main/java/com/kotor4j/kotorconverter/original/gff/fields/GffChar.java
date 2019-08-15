package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.gff.GffLoadContext;

/**
 * @author sad
 */
public class GffChar extends GffFieldValue {

    private char value;

    public char getValue() {
        return value;
    }

    @Override
    public void load(GffLoadContext loadContext, int dataOrOffset) {
        value = (char) dataOrOffset;
    }
}
