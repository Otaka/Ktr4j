package com.kotor4j.kotorconverter.original.gff.fields;

import com.kotor4j.kotorconverter.original.dialog.Tlk;
import com.kotor4j.kotorconverter.original.gff.GffLoadContext;
import com.kotor4j.kotorconverter.original.gff.GffStructure;
import java.io.IOException;


/**
 * @author sad
 */
public abstract class GffFieldValue {

    public abstract void load(GffLoadContext loadContext, int dataOrOffset) throws IOException;

    @Override
    public String toString() {
        try {
            return getClass().getMethod("getValue").invoke(this) + ":" + getClass().getSimpleName();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    
    
    public boolean getAsBoolean(){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as boolean");
    }
    public int getAsInt(){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as int");
    }
    public long getAsLong(){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as long");
    }
    public float getAsFloat(){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as float");
    }
    public double getAsDouble(){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as double");
    }
    public String getAsString(Tlk strings){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as String");
    }
    public String getAsStringValue(){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as String");
    }
    
    public GffStructure getAsStructure(){
        throw new IllegalStateException("Cannot represent this GffField ["+this.getClass().getSimpleName()+"] as GffStructure");
    }
}
