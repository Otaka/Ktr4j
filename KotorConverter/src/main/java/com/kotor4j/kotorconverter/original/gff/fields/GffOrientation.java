package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import com.kotor4j.kotorconverter.BaseReader;
import java.io.IOException;


/**
 * @author sad
 */
public class GffOrientation extends GffFieldValue {

    private float[] value;

    public float[] getValue() {
        return value;
    }

    @Override
    public void load(GffLoadContext loadContext, int dataOrOffset) throws IOException {
        loadContext.getRawData().setPosition(dataOrOffset);
        value=new float[4];
        value[0]  = BaseReader.readFloat(loadContext.getRawData());
        value[1]  = BaseReader.readFloat(loadContext.getRawData());
        value[2]  = BaseReader.readFloat(loadContext.getRawData());
        value[3]  = BaseReader.readFloat(loadContext.getRawData());
        
    }
}
