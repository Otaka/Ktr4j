package com.kotor4j.kotorconverter.original._2da;

import com.kotor4j.io.NwnByteArrayInputStream;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sad
 */
public class FileReader2da {

    public Array2da loadFile(NwnByteArrayInputStream stream, String fileName) throws IOException {
        Array2da array = new Array2da();
        byte[] magicBuffer = new byte[8];
        stream.read(magicBuffer);
        String magic = new String(magicBuffer, "UTF8");
        if (!magic.equals("2DA V2.b")) {
            throw new IllegalArgumentException("File " + fileName + " is not proper 2daB file. Magic string is '" + magic + "'");
        }

        stream.read();//should be '\n'
        List<String> columns = readColumns(stream);
        array.setColumnNames(columns);
        int rowsCount = stream.readInt();
        int columnCount = columns.size();
        List<String> rowNames = readRowsNames(stream, rowsCount);
        array.setRowLabels(rowNames);
        int cellsCount = rowsCount * columns.size();
        short[] cellOffsets = new short[cellsCount + 1];
        for (int i = 0; i < cellsCount + 1; i++) {
            cellOffsets[i] = stream.readShort();
        }

        List<String[]> rows = new ArrayList<>();
        int index = 0;
        int pos = stream.getPosition();
        for (int j = 0; j < rowsCount; j++) {
            String[] row = new String[columnCount];
            for (int i = 0; i < columnCount; i++, index++) {
                stream.setPosition(pos + cellOffsets[index]);
                String value = stream.readNullTerminatedString();
                if(value.equals("")){
                    value="****";
                }
                row[i] = value;
            }

            rows.add(row);
        }

        array.setRowData(rows);
        array.setFileName(fileName);
        return array;
    }

    private List<String> readRowsNames(NwnByteArrayInputStream stream, int count) throws IOException {
        List<String> rowNames = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            rowNames.add(stream.readStringEndedWith('\t','\0'));
        }

        return rowNames;
    }

    private List<String> readColumns(NwnByteArrayInputStream stream) throws IOException {
        List<String> columns = new ArrayList<>();
        while (true) {
            String column = stream.readStringEndedWith('\t','\0');
            if (!column.equals("")) {
                columns.add(column);
            } else {
                break;
            }
        }
        return columns;
    }
}
