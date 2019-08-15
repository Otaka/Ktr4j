package com.kotor4j.kotorconverter.original.rim;


import com.kotor4j.kotorconverter.BaseReader;
import com.kotor4j.kotorconverter.original.bif.BiffEntry;
import com.kotor4j.kotorconverter.original.chitinkey.KeyResource;
import com.kotor4j.kotorconverter.original.chitinkey.ResourceType;
import com.kotor4j.kotorconverter.exceptions.ParsingException;
import java.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dmitry
 */
public class FileReaderRim extends BaseReader {

    private static final Logger logger = LoggerFactory.getLogger(FileReaderRim.class.getName());

    public Rim loadFile(FileInputStream stream, File file) throws IOException {
        String magic = readString(stream, 4);
        if (!magic.equals("RIM ")) {
            throw new IllegalArgumentException("File " + file.getName() + " is not right RIM file");
        }

        String version = readString(stream, 4);
        skip(stream, 4);//do not know
        int entryCount = readInt(stream);
        int offsetToData = readInt(stream);

        setAbsolutePosition(stream, offsetToData);
        BiffEntry[] entries = new BiffEntry[entryCount];
        for (int i = 0; i < entryCount; i++) {
            String resRef = readString(stream, 16);
            int resourceTypeId = readInt(stream);
            int resourceId = readShort(stream) & 0xff;
            ResourceType resourceType = null;
            try {
                resourceType = ResourceType.getByType(resourceTypeId);
            } catch (ParsingException ex) {
                //logger.error(ex.getMessage() + ". ResRef=[" + resRef + "]");
                throw new ParsingException(ex.getMessage()+". ResRef=["+resRef+"]");
            }

            skip(stream, 2);//reserved

            int offset = readInt(stream);
            int size = readInt(stream);
            KeyResource keyResource = new KeyResource(resRef, resourceType, resourceId);
            BiffEntry biffEntry = new BiffEntry(keyResource, offset, size);
            entries[i] = biffEntry;
        }

        Rim rim = new Rim();
        rim.setFile(file);
        rim.setResourceEntries(entries);
        return rim;
    }
}
