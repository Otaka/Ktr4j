package com.kotor4j.kotorconverter.original.erf;


import com.kotor4j.kotorconverter.original.bif.BiffEntry;
import com.kotor4j.kotorconverter.original.chitinkey.KeyResource;
import java.io.File;

/**
 * @author Dmitry
 */
public class Erf {

    private File file;
    private KeyResource[] keyResources;
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

    public void setKeyResources(KeyResource[] keyResources) {
        this.keyResources = keyResources;
    }

    public File getFile() {
        return file;
    }

    public KeyResource[] getKeyResources() {
        return keyResources;
    }

}
