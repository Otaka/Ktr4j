package com.kotor4j.resourcemanager.gff.fields;

import com.kotor4j.resourcemanager.gff.GffLoadContext;
import com.kotor4j.resourcemanager.gff.GffStructure;

/**
 * @author sad
 */
public class GffStruct extends GffFieldValue {

    private GffStructure value;

    public GffStructure getValue() {
        return value;
    }

    @Override
    public void load(GffLoadContext loadContext, int offset) {
        value = loadContext.getStructs()[offset];
    }
}
