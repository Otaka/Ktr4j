package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.BaseReader;
import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import java.io.IOException;

/**
 * @author sad
 */
public class GffDWord64 extends GffFieldValue {

    private long value;

    public long getValue() {
        return value;
    }

    @Override

    public void load(GffLoadContext loadContext, int dataOrOffset) throws IOException {
        loadContext.getRawData().setPosition(dataOrOffset);
        value = BaseReader.readLong(loadContext.getRawData());
    }

    @Override
    public long getAsLong() {
        return value;
    }
    
}
