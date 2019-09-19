package com.kotor4j.kotorconverter.original.bif;

import java.io.File;

/**
 * @author Dmitry
 */
public class Biff {

    private final BiffEntry[] entries;
    private final File file;
    private String version;
    private int fixedResourcesCount;

    public Biff(String version, BiffEntry[] entries, File file, int fixedResourcesCount) {
        this.entries = entries;
        this.version = version;
        this.file = file;
        this.fixedResourcesCount = fixedResourcesCount;
    }

    public int getFixedResourcesCount() {
        return fixedResourcesCount;
    }

    public String getVersion() {
        return version;
    }

    public BiffEntry[] getEntries() {
        return entries;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return file.getName() + ". Entries count=" + entries.length;
    }
}
