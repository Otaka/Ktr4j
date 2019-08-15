package com.kotor4j.kotorconverter.original.gff.filetypes;

import com.kotor4j.kotorconverter.original.gff.Gff;


/**
 * @author Dmitry
 */
public abstract class AbstractGffResource {

    protected Gff gff;

    public void setGff(Gff gff) {
        this.gff = gff;
    }

   

    public Gff getGff() {
        return gff;
    }

}
