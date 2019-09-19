package com.kotor4j.kotorconverter.original.gff;

import com.kotor4j.kotorconverter.original.gff.fields.GffField;
import gnu.trove.map.hash.TObjectIntHashMap;

/**
 * @author sad
 */
public class GffStructure {

    private int type;
    private GffField[] fields;
    private TObjectIntHashMap<String> columnIndexes = new TObjectIntHashMap<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setFields(GffField[] fields) {
        this.fields = fields;
        columnIndexes.clear();
        for (int i = 0; i < fields.length; i++) {
            columnIndexes.put(fields[i].getLabel(), i);
        }
    }
    
    public boolean containsField(String fieldName){
        return columnIndexes.containsKey(fieldName);
    }
    
    public GffField get(String fieldName){
        if(!columnIndexes.containsKey(fieldName)){
            throw new IllegalArgumentException("Gff structure does not have field ["+fieldName+"]");
        }
        return fields[columnIndexes.get(fieldName)];
    }

    public GffField[] getFields() {
        return fields;
    }

}
