package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import java.io.IOException;
import com.kotor4j.kotorconverter.BaseReader;


/**
 * @author sad
 */
public class GffResRef extends GffFieldValue {

    private String value;

    public String getValue() {
        return value;
    }

    @Override
    public void load(GffLoadContext loadContext, int dataOrOffset) throws IOException {
        loadContext.getRawData().setPosition(dataOrOffset);
        int size = loadContext.getRawData().read() & 0xFF;
        value = BaseReader.readString(loadContext.getRawData(), size);
    }

    @Override
    public String getAsStringValue() {
        return value;
    }
    
}
