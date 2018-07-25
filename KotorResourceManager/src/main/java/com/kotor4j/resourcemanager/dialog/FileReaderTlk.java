package com.kotor4j.resourcemanager.dialog;

import com.kotor4j.configs.Configuration;
import com.kotor4j.io.NwnByteArrayInputStream;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileReaderTlk {

    private Charset fileCharset;

    public FileReaderTlk(Configuration configuration) {
        fileCharset = Charset.forName(configuration.getString("tlkFileCharset"));
    }

    public Tlk loadFile(NwnByteArrayInputStream stream, String fileName) throws IOException {
        String type = stream.readString(4);
        if (!type.equals("TLK ")) {
            throw new IllegalArgumentException("File " + fileName + " is not a valid tlk file");
        }

        String version = stream.readString(4);
        int languageId = stream.readInt();
        int stringCount = stream.readInt();
        int stringEntriesOffset = stream.readInt();
        StringEntry[] entries = loadStringDataTable(stream, stringCount, stringEntriesOffset);
        Tlk tlk = new Tlk(languageId, entries);
        return tlk;
    }

    private StringEntry[] loadStringDataTable(NwnByteArrayInputStream stream, int stringCount, int stringArrayOffset) throws IOException {
        StringEntry[] strings = new StringEntry[stringCount];
        for (int i = 0; i < stringCount; i++) {
            int stringRef = i;
            int flags = stream.readInt();
            String soundResRef = stream.readString(16);
            int volumeVariance = stream.readInt();
            int pitchVariance = stream.readInt();
            int offsetToString = stream.readInt();
            int stringSize = stream.readInt();
            float soundLength = Float.intBitsToFloat(stream.readInt());
            int position = stream.getPosition();
            stream.setPosition(stringArrayOffset + offsetToString);
            String stringValue = stream.readString(stringSize);
            stringValue = new String(stringValue.getBytes("cp1252"), fileCharset);
            stream.setPosition(position);
            StringEntry string = new StringEntry(stringRef, stringValue, soundResRef, soundLength, flags);
            strings[i] = string;
        }
        return strings;
    }
}
