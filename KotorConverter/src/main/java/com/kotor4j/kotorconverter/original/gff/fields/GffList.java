package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import com.kotor4j.kotorconverter.original.gff.GffStructure;
import java.io.IOException;

/**
 * @author sad
 */
public class GffList extends GffFieldValue {

    private GffStructure[] structs;

    public GffStructure[] getValue() {
        return structs;
    }

    public int size() {
        return structs.length;
    }

    @Override
    public void load(GffLoadContext loadContext, int dataOrOffset) throws IOException {
        int[] indicies = loadContext.getListIndicies();
        int offset = dataOrOffset / 4;
        int count = indicies[offset];
        structs = new GffStructure[count];
        for (int i = 0; i < count; i++) {
            structs[i] = loadContext.getStructs()[indicies[offset + i + 1]];
        }
    }
}
