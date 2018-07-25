package com.kotor4j.resourcemanager.gff.fields;

import com.kotor4j.resourcemanager.gff.GffLoadContext;



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
}
