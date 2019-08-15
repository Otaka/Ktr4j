package com.kotor4j.kotorconverter.original.rim;

import com.kotor4j.kotorconverter.original.bif.BiffEntry;
import java.io.File;

/**
 * @author Dmitry
 */
public class Rim {

    private File file;
    private BiffEntry[] resourceEntries;

    public BiffEntry[] getResourceEntries() {
        return resourceEntries;
    }

    public void setResourceEntries(BiffEntry[] resourceEntries) {
        this.resourceEntries = resourceEntries;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
