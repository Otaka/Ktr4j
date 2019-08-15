package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import com.kotor4j.kotorconverter.BaseReader;

import java.io.IOException;

/**
 * @author sad
 */
public class GffExoString extends GffFieldValue {

    private String value;

    public String getValue() {
        return value;
    }

    @Override
    public String getAsStringValue() {
        return value;
    }

    @Override
    public void load(GffLoadContext loadContext, int dataOrOffset) throws IOException {
        loadContext.getRawData().setPosition(dataOrOffset);
        int size = BaseReader.readInt(loadContext.getRawData());
        value = BaseReader.readString(loadContext.getRawData(), size);
    }
}
