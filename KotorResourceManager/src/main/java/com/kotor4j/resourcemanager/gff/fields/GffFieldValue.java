package com.kotor4j.resourcemanager.gff.fields;

import com.kotor4j.resourcemanager.gff.GffLoadContext;
import java.io.IOException;


/**
 * @author sad
 */
public abstract class GffFieldValue {

    public abstract void load(GffLoadContext loadContext, int dataOrOffset) throws IOException;

    @Override
    public String toString() {
        try {
            return getClass().getMethod("getValue").invoke(this) + ":" + getClass().getSimpleName();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}
