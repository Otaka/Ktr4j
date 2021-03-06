package com.kotor4j.kotorconverter.original.gff;

import com.kotor4j.io.NwnByteArrayInputStream;
import com.kotor4j.kotorconverter.original.gff.fields.GffField;



/**
 * @author sad
 */
public class GffLoadContext {

    private int[] fieldIndicies;
    private int[] listIndicies;
    private String[] labels;
    private GffField[] fields;
    private NwnByteArrayInputStream rawData;
    private GffStructure[] structs;

    public GffLoadContext(GffStructure[] structs, int[] fieldIndicies, int[] listIndicies, String[] labels, NwnByteArrayInputStream rawData) {
        this.fieldIndicies = fieldIndicies;
        this.listIndicies = listIndicies;
        this.labels = labels;
        this.rawData = rawData;
        this.structs = structs;
    }

    public GffStructure[] getStructs() {
        return structs;
    }

    public void setFields(GffField[] fields) {
        this.fields = fields;
    }

    public GffField[] getFields() {
        return fields;
    }

    public int[] getFieldIndicies() {
        return fieldIndicies;
    }

    public int[] getListIndicies() {
        return listIndicies;
    }

    public String[] getLabels() {
        return labels;
    }

    public NwnByteArrayInputStream getRawData() {
        return rawData;
    }

}
