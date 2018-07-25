package com.kotor4j.resourcemanager.bif;

import com.kotor4j.resourcemanager.BaseReader;
import com.kotor4j.resourcemanager.chitinkey.ChitinKey;
import com.kotor4j.resourcemanager.chitinkey.KeyResource;
import java.io.*;

/**
 * @author Dmitry
 */
public class FileReaderBiff extends BaseReader {

    public Biff readBiffHeader(FileInputStream stream, File file, ChitinKey keyFile) throws IOException {
        String magic = readString(stream, 4);
        if (!magic.equals("BIFF")) {
            throw new IllegalArgumentException("File " + file.getName() + " is not right BIFF file");
        }

        String version = readString(stream, 4);
        int variableResourcesCount = readInt(stream);
        int fixedResourcesCount = readInt(stream);
        int variableTableOffset = readInt(stream);
        setAbsolutePosition(stream, variableTableOffset);
        BiffEntry[] entries = loadBiffDescription(keyFile, stream, variableResourcesCount);
        return new Biff(version, entries, file, fixedResourcesCount);
    }

    private BiffEntry[] loadBiffDescription(ChitinKey key, FileInputStream stream, int count) throws IOException {
        BiffEntry[] biffEntries = new BiffEntry[count];
        for (int i = 0; i < count; i++) {
            int id = readInt(stream);
            int offset = readInt(stream);
            int fileSize = readInt(stream);
            int resourceType = readInt(stream);
            KeyResource kr = key.getResourceByResId(id);
            BiffEntry biffEntry = new BiffEntry(kr, offset, fileSize);
            biffEntries[i] = biffEntry;
        }
        return biffEntries;
    }
}
