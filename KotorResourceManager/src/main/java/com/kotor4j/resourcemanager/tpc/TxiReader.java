package com.kotor4j.resourcemanager.tpc;


import com.kotor4j.io.NwnByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Dmitry
 */
public class TxiReader {
    private int size;

    public TxiReader(NwnByteArrayInputStream stream, int size) throws IOException {
        this.size = size;
    }

    public Txi load() {
        return null;
    }
}
