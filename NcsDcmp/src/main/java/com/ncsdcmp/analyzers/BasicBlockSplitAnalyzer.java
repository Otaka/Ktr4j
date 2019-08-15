package com.ncsdcmp.analyzers;

import com.ncsdcmp.BasicBlock;
import com.ncsdcmp.FunctionInfo;
import com.ncsdcmp.Line;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author sad
 */
public class BasicBlockSplitAnalyzer extends AbstractAnalyzer {

    @Override
    public void analyze() {
        for (FunctionInfo function : getContext().getFunctions()) {
            processFunction(function);
        }
    }

    private void addReferenceFromToMap(Map<Integer, Set<Integer>> map, int referenceFrom, int address) {
        Set<Integer> references = map.get(address);
        if (references == null) {
            references = new HashSet<>();
            map.put(address, references);
        }
        references.add(referenceFrom);
    }

    private void addPointToReferenceFromJumpInstruction(Map<Integer, Set<Integer>> map, int pointAddress, int currentAddress, FunctionInfo function) {
        addReferenceFromToMap(map, currentAddress, pointAddress);

        //point to splits the block on two part, that is why we should combine them with xref if previous block is not finished with "blockstop" instruction
        int pointAddressInstructionIndex = getInstructionIndexByAddress(pointAddress);
        if (pointAddress > function.getAddress()) {
            Line previousInstruction = getContext().getLines().get(pointAddressInstructionIndex - 1);
            if (!isBlockStopInstruction(previousInstruction.getOpcode())) {
                addReferenceFromToMap(map, previousInstruction.getAddress(), pointAddress);
            }
        }
    }

    private void processFunction(FunctionInfo function) {
        Map<Integer, Set<Integer>> shouldStartNewBlockAndReferenceFrom = new HashMap<>();
        int instructionIndexStart = getInstructionIndexByAddress(function.getAddress());
        int instructionIndexEnd = getInstructionIndexByAddress(function.getAddress() + function.getLength());
        addReferenceFromToMap(shouldStartNewBlockAndReferenceFrom, -1, function.getAddress());
        //at first just run through instructions, and mark all places that split blocks
        for (int i = instructionIndexStart; i <= instructionIndexEnd; i++) {
            Line instruction = getContext().getLines().get(i);
            int currentAddress = instruction.getAddress();
            switch (instruction.getOpcode()) {
                case "JMP": {
                    int pointAddress = (int) instruction.getArgs()[0];
                    addReferenceFromToMap(shouldStartNewBlockAndReferenceFrom, -1, getContext().getLines().get(i + 1).getAddress());
                    addPointToReferenceFromJumpInstruction(shouldStartNewBlockAndReferenceFrom, pointAddress, currentAddress, function);
                    break;
                }
                case "JNZ":
                case "JZ": {
                    int pointAddress = (int) instruction.getArgs()[0];
                    addReferenceFromToMap(shouldStartNewBlockAndReferenceFrom, currentAddress, getContext().getLines().get(i + 1).getAddress());
                    addPointToReferenceFromJumpInstruction(shouldStartNewBlockAndReferenceFrom, pointAddress, currentAddress, function);
                    break;
                }
                case "RETN": {
                    if ((i + 1) < getContext().getLines().size()) {
                        addReferenceFromToMap(shouldStartNewBlockAndReferenceFrom, -1, getContext().getLines().get(i + 1).getAddress());
                    }
                    break;
                }
            }
        }

        Map<Integer, BasicBlock> addressToBasicBlock = new HashMap<>();
        //do actual splitting
        BasicBlock currentBasicBlock = null;
        for (int i = instructionIndexStart; i <= instructionIndexEnd; i++) {
            Line instruction = getContext().getLines().get(i);
            int currentAddress = instruction.getAddress();

            if (shouldStartNewBlockAndReferenceFrom.containsKey(currentAddress)) {
                if (currentBasicBlock != null) {
                    currentBasicBlock.setByteLength(currentAddress - currentBasicBlock.getEnterAddress());
                    Line previousInstruction = getContext().getLines().get(i - 1);
                    currentBasicBlock.setLastInstructionAddress(previousInstruction.getAddress());
                    currentBasicBlock.setInstructionsCount(i - getInstructionIndexByAddress(currentBasicBlock.getEnterAddress()));
                }

                BasicBlock newBasicBlock = new BasicBlock();
                newBasicBlock.setFirstInstructionIndex(i);
                newBasicBlock.setEnterAddress(currentAddress);
                newBasicBlock.setOwnerFunction(function);
                addressToBasicBlock.put(currentAddress, newBasicBlock);
                currentBasicBlock = newBasicBlock;
                getContext().getAddress2BasicBlock().put(currentAddress, currentBasicBlock);
            }

            instruction.setBasicBlock(currentBasicBlock);
        }
        //fix last block

        currentBasicBlock.setInstructionsCount(instructionIndexEnd - currentBasicBlock.getFirstInstructionIndex() + 1);
        Line lastBlockInstruction = getContext().getLines().get(instructionIndexEnd);
        currentBasicBlock.setLastInstructionAddress(lastBlockInstruction.getAddress());
        int lastBlockBytesLength = lastBlockInstruction.getAddress() - currentBasicBlock.getEnterAddress() + lastBlockInstruction.getInstructionBytesLength();
        currentBasicBlock.setByteLength(lastBlockBytesLength);

        //set cross references
        for (BasicBlock block : addressToBasicBlock.values()) {
            Set<Integer> referencedFrom = shouldStartNewBlockAndReferenceFrom.get(block.getEnterAddress());
            if (referencedFrom != null) {
                for (int referencedFromAddress : referencedFrom) {
                    if (referencedFromAddress != -1) {
                        int referencedFromInstructionIndex = getInstructionIndexByAddress(referencedFromAddress);
                        Line referencedFromInstruction = getContext().getLines().get(referencedFromInstructionIndex);
                        BasicBlock referencedFromBasicBlock = referencedFromInstruction.getBasicBlock();
                        block.getReferencedFrom().add(referencedFromBasicBlock);

                        if (block.getFirstInstructionIndex() - 1 == referencedFromInstructionIndex) {
                            referencedFromBasicBlock.setNextBlock(block);
                        } else {
                            referencedFromBasicBlock.setJumpBlock(block);
                        }
                    }
                }
            }
        }

        function.setRootBasicBlock(addressToBasicBlock.get(function.getAddress()));
    }

    private boolean isBlockStopInstruction(String opcode) {
        return opcode.equals("RETN") || opcode.equals("JMP");
    }
}
