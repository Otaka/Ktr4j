package com.kotor4j.resourcemanager.chitinkey;

import com.kotor4j.io.NwnByteArrayInputStream;
import com.kotor4j.resourcemanager.BaseReader;
import com.kotor4j.resourcemanager.exceptions.ParsingException;
import java.io.IOException;

/**
 * @author Dmitry
 */
public class ChitinKeyFileReader extends BaseReader {

    public ChitinKey loadFile(NwnByteArrayInputStream stream, String fileName) throws IOException {
        String magic = readString(stream, 4);
        if (!magic.equals("KEY ")) {
            throw new IllegalArgumentException("File " + fileName + " is not right KEY file");
        }

        String version = readString(stream, 4);
        int bifCount = readInt(stream);
        int keyCount = readInt(stream);
        int offsetToFileTable = readInt(stream);
        int offsetToKeyTable = readInt(stream);
        int buildYear = readInt(stream);
        int buildDay = readInt(stream);
        stream.skip(32);//reserved
        stream.setPosition(offsetToKeyTable);

        KeyResource[] keyResources = readKeyTable(stream, keyCount);
        stream.setPosition(offsetToFileTable);
        BifShort[] biffs = readBiffs(stream, bifCount);
        ChitinKey keyFile = new ChitinKey(keyResources, biffs);
        keyFile.setVersion(version);
        keyFile.setBuildDay(buildDay);
        keyFile.setBuildYear(buildYear);
        return keyFile;
    }

    private BifShort[] readBiffs(NwnByteArrayInputStream stream, int biffCount) throws IOException {
        BifShort[] biffs = new BifShort[biffCount];
        for (int i = 0; i < biffCount; i++) {
            int fileSize = readInt(stream);
            int fileNameOffset = readInt(stream);
            int fileNameSize = readShort(stream);
            int drive = readShort(stream);
            int pos = stream.getPosition();
            stream.setPosition(fileNameOffset);
            String name = readString(stream, fileNameSize);

            stream.setPosition(pos);
            biffs[i] = new BifShort(name, fileSize);
        }
        return biffs;
    }

    private KeyResource[] readKeyTable(NwnByteArrayInputStream stream, int keysCount) throws IOException {
        KeyResource[] keyResources = new KeyResource[keysCount];
        for (int i = 0; i < keysCount; i++) {
            String resRef = null;
            try {
                resRef = readString(stream, 16);
                int resourceTypeId = readShort(stream);
                ResourceType resourceType = ResourceType.getByType(resourceTypeId);
                if (resourceType == null) {
                    System.err.println("Cannot recognize type of the resource '" + resRef + "' with id = " + resourceTypeId);
                }
                int resId = readInt(stream);
                keyResources[i] = new KeyResource(resRef, resourceType, resId);
            } catch (ParsingException ex) {
                throw new ParsingException("Error while parsing record [" + resRef + "] from chitinKey file", ex);
            }
        }
        return keyResources;
    }
}
