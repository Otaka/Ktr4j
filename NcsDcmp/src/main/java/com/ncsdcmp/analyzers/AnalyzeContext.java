package com.ncsdcmp.analyzers;

import com.ncsdcmp.BasicBlock;
import com.ncsdcmp.FunctionInfo;
import com.ncsdcmp.Line;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author sad
 */
public class AnalyzeContext {

    private InputStream inputStream;
    private List<Line> lines = new ArrayList<>();
    private Map<Integer, BasicBlock> address2BasicBlock = new HashMap<>();
    private List<FunctionInfo> functions = new ArrayList<>();
    private FunctionInfo rootFunction;
    private Set<Integer> unusedAddresses;
    private Set<BasicBlock> unusedBasicBlocks;

    public void setUnusedAddresses(Set<Integer> unusedAddresses) {
        this.unusedAddresses = unusedAddresses;
    }

    public void setUnusedBasicBlocks(Set<BasicBlock> unusedBasicBlocks) {
        this.unusedBasicBlocks = unusedBasicBlocks;
    }

    public Set<Integer> getUnusedAddresses() {
        return unusedAddresses;
    }

    public Set<BasicBlock> getUnusedBasicBlocks() {
        return unusedBasicBlocks;
    }

    public void setRootFunction(FunctionInfo rootFunction) {
        this.rootFunction = rootFunction;
    }

    public FunctionInfo getRootFunction() {
        return rootFunction;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Map<Integer, BasicBlock> getAddress2BasicBlock() {
        return address2BasicBlock;
    }

    public List<FunctionInfo> getFunctions() {
        return functions;
    }

    public List<Line> getLines() {
        return lines;
    }

}
