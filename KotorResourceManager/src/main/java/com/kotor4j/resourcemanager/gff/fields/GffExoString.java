package com.kotor4j.resourcemanager.gff.fields;

import com.kotor4j.resourcemanager.BaseReader;
import com.kotor4j.resourcemanager.gff.GffLoadContext;
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
    public void load(GffLoadContext loadContext, int dataOrOffset) throws IOException {
        loadContext.getRawData().setPosition(dataOrOffset);
        int size = BaseReader.readInt(loadContext.getRawData());
        value = BaseReader.readString(loadContext.getRawData(), size);
    }
}
