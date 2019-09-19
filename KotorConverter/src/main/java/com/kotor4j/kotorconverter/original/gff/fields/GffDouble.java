package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.BaseReader;
import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import java.io.IOException;

/**
 * @author sad
 */
public class GffDouble extends GffFieldValue {

    private double value;

    public double getValue() {
        return value;
    }

    @Override
    public void load(GffLoadContext loadContext, int dataOrOffset) throws IOException {
        loadContext.getRawData().setPosition(dataOrOffset);
        value = Double.longBitsToDouble(BaseReader.readLong(loadContext.getRawData()));
    }

    @Override
    public double getAsDouble() {
        return value;
    }
    
}
