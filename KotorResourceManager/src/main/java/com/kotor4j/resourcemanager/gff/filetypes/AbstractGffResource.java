package com.kotor4j.resourcemanager.gff.filetypes;

import com.kotor4j.resourcemanager.gff.Gff;

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
