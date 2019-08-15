package com.ncsdcmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sad
 */
public class BasicBlock {

    private int firstInstructionIndex;
    private int enterAddress;
    private int lastInstructionAddress;
    private int byteLength;
    private int instructionsCount;
    private FunctionInfo ownerFunction;
    private List<BasicBlock> referencedFrom = new ArrayList<>();
    private Map<String, Object> metadata = new HashMap<>();
    private BasicBlock nextBlock;
    private BasicBlock jumpBlock;

    public void setLastInstructionAddress(int lastInstructionAddress) {
        this.lastInstructionAddress = lastInstructionAddress;
    }

    public int getLastInstructionAddress() {
        return lastInstructionAddress;
    }

    public void setFirstInstructionIndex(int firstInstructionIndex) {
        this.firstInstructionIndex = firstInstructionIndex;
    }

    public int getFirstInstructionIndex() {
        return firstInstructionIndex;
    }

    public void setJumpBlock(BasicBlock jumpBlock) {
        this.jumpBlock = jumpBlock;
    }

    public void setNextBlock(BasicBlock nextBlock) {
        this.nextBlock = nextBlock;
    }

    public BasicBlock getJumpBlock() {
        return jumpBlock;
    }

    public BasicBlock getNextBlock() {
        return nextBlock;
    }

    public FunctionInfo getOwnerFunction() {
        return ownerFunction;
    }

    public void setOwnerFunction(FunctionInfo ownerFunction) {
        this.ownerFunction = ownerFunction;
    }

    public void setInstructionsCount(int instructionsCount) {
        this.instructionsCount = instructionsCount;
    }

    public int getInstructionsCount() {
        return instructionsCount;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setEnterAddress(int enterAddress) {
        this.enterAddress = enterAddress;
    }

    public List<BasicBlock> getReferencedFrom() {
        return referencedFrom;
    }

    public int getEnterAddress() {
        return enterAddress;
    }

    public void setByteLength(int byteLength) {
        this.byteLength = byteLength;
    }

    public int getByteLength() {
        return byteLength;
    }

    @Override
    public String toString() {
        return Integer.toHexString(enterAddress) + ":" + Integer.toHexString(lastInstructionAddress);
    }

}
