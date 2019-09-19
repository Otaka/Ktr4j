package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import com.kotor4j.kotorconverter.original.gff.GffStructure;

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

    @Override
    public GffStructure getAsStructure() {
        return value;
    }

}
