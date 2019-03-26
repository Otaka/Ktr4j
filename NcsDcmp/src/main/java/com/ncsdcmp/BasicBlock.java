package com.ncsdcmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sad
 */
class BasicBlock {

    private List<Line> lines = new ArrayList<>();
    private int enterAddress;
    private int exitAddress;
    private List<BasicBlock> referencedFrom = new ArrayList<>();
    private BasicBlock referencedTo;
    private int referencedToAddress;
    private Map<String,Object>metadata=new HashMap<>();

    public Map<String, Object> getMetadata() {
        return metadata;
    }
    
    

    public void setReferencedToAddress(int referencedToAddress) {
        this.referencedToAddress = referencedToAddress;
    }

    public int getReferencedToAddress() {
        return referencedToAddress;
    }

    public void setEnterAddress(int enterAddress) {
        this.enterAddress = enterAddress;
    }

    public void setExitAddress(int exitAddress) {
        this.exitAddress = exitAddress;
    }

    public int getExitAddress() {
        return exitAddress;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<BasicBlock> getReferencedFrom() {
        return referencedFrom;
    }

    public BasicBlock getReferencedTo() {
        return referencedTo;
    }

    public int getEnterAddress() {
        return enterAddress;
    }

    public void setReferencedTo(BasicBlock referencedTo) {
        this.referencedTo = referencedTo;
    }

    @Override
    public String toString() {
        return Integer.toHexString(enterAddress)+":"+Integer.toHexString(exitAddress)+"["+lines.size()+" commands]";
    }

}
