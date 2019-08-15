package com.ncsdcmp.analyzers;

import com.ncsdcmp.BasicBlock;
import com.ncsdcmp.FunctionInfo;
import com.ncsdcmp.Line;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sad
 */
public class MarkDeadCodeAnalyzer extends AbstractAnalyzer {

    private Set<Integer> unusedAddress = new HashSet<>();
    private Set<BasicBlock> unusedBasicBlocks = new HashSet<>();

    @Override
    public void analyze() {
        boolean continueProcessing = true;
        while (continueProcessing == true) {
            continueProcessing = false;
            for (BasicBlock basicBlock : getContext().getAddress2BasicBlock().values()) {
                if (!unusedBasicBlocks.contains(basicBlock)) {
                    if (basicBlock.getReferencedFrom().isEmpty()) {
                        if (!isFunctionStartBlock(basicBlock)) {
                            continueProcessing = true;
                            markEveryInstructionAsUnused(basicBlock);
                            unusedBasicBlocks.add(basicBlock);
                            //removeBlockFromReferencedList(basicBlock, basicBlock.getNextBlock());
                            //removeBlockFromReferencedList(basicBlock, basicBlock.getJumpBlock());
                        }
                    }
                }
            }
        }
        getContext().setUnusedAddresses(unusedAddress);
        getContext().setUnusedBasicBlocks(unusedBasicBlocks);
    }

   /* private void removeBlockFromReferencedList(BasicBlock blockToRemove, BasicBlock containerBlock) {
        if (containerBlock != null) {
            containerBlock.getReferencedFrom().remove(blockToRemove);
        }
    }
*/
    private void markEveryInstructionAsUnused(BasicBlock block) {
        int index = block.getFirstInstructionIndex();
        for (int i = 0; i < block.getInstructionsCount(); i++, index++) {
            Line line = getContext().getLines().get(index);
            unusedAddress.add(line.getAddress());
        }
    }

    private boolean isFunctionStartBlock(BasicBlock basicBlock) {
        for (FunctionInfo function : getContext().getFunctions()) {
            if (function.getAddress() == basicBlock.getEnterAddress()) {
                return true;
            }
        }

        return false;
    }
}
