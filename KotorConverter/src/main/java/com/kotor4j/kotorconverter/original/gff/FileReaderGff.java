package com.kotor4j.kotorconverter.original.gff;

import com.kotor4j.io.NwnByteArrayInputStream;
import com.kotor4j.kotorconverter.original.gff.fields.GffField;
import com.kotor4j.kotorconverter.original.gff.fields.GffFieldValue;
import java.io.*;

public class FileReaderGff {

    public Gff loadFile(NwnByteArrayInputStream stream, String fileName) throws IOException {

        String type = stream.readString(4);
        String version = stream.readString(4);

        int structOffset = stream.readInt();
        int structCount = stream.readInt();
        int fieldOffset = stream.readInt();
        int fieldCount = stream.readInt();
        int labelOffset = stream.readInt();
        int labelCount = stream.readInt();
        int fieldDataOffset = stream.readInt();
        int fieldDataCount = stream.readInt();
        int fieldIndiciesOffset = stream.readInt();
        int fieldIndiciesCount = stream.readInt() / 4;
        int listIndiciesOffset = stream.readInt();
        int listIndiciesCount = stream.readInt() / 4;

        stream.setPosition(labelOffset);
        String[] labels = loadLabels(stream, labelCount);

        byte[] rawData = new byte[fieldDataCount];
        stream.setPosition(fieldDataOffset);
        stream.read(rawData);
        NwnByteArrayInputStream rawDataStream = new NwnByteArrayInputStream(rawData);

        stream.setPosition(fieldIndiciesOffset);
        int[] fieldIndicies = loadFieldIndicies(stream, fieldIndiciesCount);

        stream.setPosition(listIndiciesOffset);
        int[] listIndicies = loadListIndicies(stream, listIndiciesCount);
        GffStructure[] structures = new GffStructure[structCount];
        for (int i = 0; i < structCount; i++) {
            structures[i] = new GffStructure();
        }

        GffLoadContext loadContext = new GffLoadContext(structures, fieldIndicies, listIndicies, labels, rawDataStream);
        stream.setPosition(fieldOffset);
        loadFields(stream, fieldCount, loadContext);

        stream.setPosition(structOffset);
        loadStructures(stream, structCount, loadContext);
        Gff gff = new Gff(type, version, loadContext.getStructs()[0]);
        return gff;
    }

    private void loadStructures(NwnByteArrayInputStream stream, int structCount, GffLoadContext loadContext) throws IOException {
        for (int i = 0; i < structCount; i++) {
            GffStructure structure = loadContext.getStructs()[i];
            int type = stream.readInt();
            int dataOrOffset = stream.readInt();
            int fieldsCount = stream.readInt();
            structure.setType(type);
            GffField[] fields;
            if (fieldsCount == 1) {
                fields = new GffField[]{loadContext.getFields()[dataOrOffset]};
            } else {
                int fieldIndiciesOffset = dataOrOffset / 4;
                fields = new GffField[fieldsCount];
                for (int j = 0; j < fieldsCount; j++) {
                    int index = loadContext.getFieldIndicies()[fieldIndiciesOffset + j];
                    GffField field = loadContext.getFields()[index];
                    field.toString();
                    fields[j] = field;
                }
            }

            structure.setFields(fields);
        }
    }

    private int[] loadFieldIndicies(NwnByteArrayInputStream stream, int indiciesCount) throws IOException {
        int[] result = new int[indiciesCount];
        for (int i = 0; i < indiciesCount; i++) {
            result[i] = stream.readInt();
        }
        return result;
    }

    private int[] loadListIndicies(NwnByteArrayInputStream stream, int indiciesCount) throws IOException {
        int[] rawList = new int[indiciesCount];
        for (int i = 0; i < indiciesCount; i++) {
            rawList[i] = stream.readInt();
        }

        int listCount = 0;
        for (int i = 0; i < indiciesCount; i++) {
            int n = rawList[i];
            i += n;
            listCount++;
        }

        int[] list = new int[listCount];

        return rawList;
    }

    private void loadFields(NwnByteArrayInputStream stream, int fieldsCount, GffLoadContext gffLoadContext) throws IOException {
        GffField[] fields = new GffField[fieldsCount];
        for (int i = 0; i < fieldsCount; i++) {
            fields[i] = new GffField();
        }

        gffLoadContext.setFields(fields);
        for (int i = 0; i < fieldsCount; i++) {
            int dataType = stream.readInt();
            int labelIndex = stream.readInt();
            String label = gffLoadContext.getLabels()[labelIndex];
            int dataOrOffset = stream.readInt();

            GffField field = fields[i];
            field.setLabel(label);
            GffFieldType fieldType = GffFieldType.getByType(dataType);
            if (fieldType == null) {
                throw new IllegalArgumentException("Cannot recognize GFF field type [" + dataType + "]");
            }
            GffFieldValue value = fieldType.loadGffFieldValue(gffLoadContext, dataOrOffset);

            field.setValue(value);

        }
    }

    private String[] loadLabels(NwnByteArrayInputStream stream, int labelsCount) throws IOException {
        String[] labels = new String[labelsCount];
        for (int i = 0; i < labelsCount; i++) {
            String label = stream.readString(16);
            labels[i] = label;
        }
        return labels;
    }
}
