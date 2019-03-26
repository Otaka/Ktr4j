package com.ncsdcmp;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

/**
 * @author sad
 */
public class Decompiler {

    List<Line> lines = new ArrayList<>();
    BasicBlock rootBasicBlock;
    Map<Integer, BasicBlock> address2BasicBlock = new HashMap<>();
    Queue<Integer> deferredAddresses = new ArrayDeque<>();
    //map that has key - destination address, values - list of addresses of commands that has reference to key address
    Map<Integer, List<Integer>> address2referencesFrom = new HashMap<>();
    List<FunctionInfo> functionReferences = new ArrayList<>();

    public String decompile(InputStream is) {
        parse(is);
        splitOnBasicBlocks();
        if(rootBasicBlock==null){
            throw new IllegalStateException("Cannot find root basic block");
        }

        FunctionDecompiler functionDecompiler = new FunctionDecompiler(this);
        return functionDecompiler.decompile();
    }

    public void splitOnBasicBlocks() {
        System.out.println("Split on basic blocks");
        BasicBlock basicBlock = processAddress(8);//every script starts from 0x8 address
        address2BasicBlock.put(basicBlock.getEnterAddress(), basicBlock);
        rootBasicBlock = basicBlock;
        while (!deferredAddresses.isEmpty()) {
            int address = deferredAddresses.poll();
            if (!address2BasicBlock.containsKey(address)) {
                BasicBlock otherBasicBlock = processAddress(address);
                address2BasicBlock.put(otherBasicBlock.getEnterAddress(), otherBasicBlock);
            }
        }

        System.out.println("Finished splitting on basic blocks");
    }

    private BasicBlock processAddress(int address) {
        int lineIndex = findLineByAddress(address);
        int startLineIndex = lineIndex;
        BasicBlock basicBlock = new BasicBlock();
        basicBlock.setEnterAddress(address);
        while (true) {
            Line line = lines.get(lineIndex);
            String opcode = line.getOpcode();
            boolean endLine = false;
            switch (opcode) {
                case "JMP":
                case "JZ":
                case "JNZ":
                    int exitAddress = (Integer) line.getArgs()[0];
                    requestAddressForProcess(exitAddress);
                    if (!opcode.equals("JMP")) {
                        requestAddressForProcess(lines.get(lineIndex + 1).getAddress());
                    }
                    basicBlock.setReferencedToAddress(exitAddress);
                    endLine = true;
                    break;
                case "RETN":
                    basicBlock.setReferencedToAddress(-1);
                    endLine = true;
                    break;
                case "JSR":
                    int functionAddress = (Integer) line.getArgs()[0];
                    requestAddressForProcess(functionAddress);
                    addReferenceFromTo(line.getAddress(), functionAddress);
                    addFunctionReference(functionAddress);
                    break;
                default:
                //do nothing with other commands
            }

            if (endLine) {
                break;
            } else {
                lineIndex++;
            }
        }

        Line endLine = lines.get(lineIndex);
        basicBlock.setExitAddress(endLine.getAddress());
        for (int i = startLineIndex; i <= lineIndex; i++) {
            basicBlock.getLines().add(lines.get(i));
        }

        return basicBlock;
    }

    private void addReferenceFromTo(int referenceFromAddress, int referenceToAddress) {
        List<Integer> destinationList = address2referencesFrom.get(referenceFromAddress);
        if (destinationList == null) {
            destinationList = new ArrayList<>();
            address2referencesFrom.put(referenceFromAddress, destinationList);
        }
        destinationList.add(referenceToAddress);
    }

    private void requestAddressForProcess(int address) {
        if (!deferredAddresses.contains(address)) {
            deferredAddresses.add(address);
        }
    }

    private void addFunctionReference(int address) {
        FunctionInfo functionInfo = new FunctionInfo(address);
        if (!functionReferences.contains(functionInfo)) {
            functionReferences.add(functionInfo);
        }
    }

    private int findLineByAddress(int address) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).getAddress() == address) {
                return i;
            }
        }
        throw new IllegalArgumentException("Cannot find line started from address [" + address + "]");
    }

    private void parse(InputStream is) {
        lines.clear();
        try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
            int lineIndex = -1;
            while (scanner.hasNextLine()) {
                lineIndex++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                lines.add(parseLine(line, lineIndex));
            }
        }
    }

    private Line parseLine(String line, int index) {
        if (!line.startsWith("/*")) {
            throw new IllegalArgumentException("Script line #" + index + " [" + line + "] does not start with [/*]");
        }
        if (!line.contains("*/")) {
            throw new IllegalArgumentException("Script line #" + index + " [" + line + "] does not have ADDRESS HEX block [/*ADDRESS HEX*/] OPCODE ARGS");
        }
        Line lineObject = new Line();
        String addressHexLine = line.substring(2, line.indexOf("*/"));
        String address = addressHexLine.substring(0, addressHexLine.indexOf(' '));

        lineObject.setAddress(Integer.parseInt(address, 16));

        String opcodeLine = line.substring(line.indexOf("*/") + 2);
        opcodeLine = opcodeLine.trim();
        if (!opcodeLine.contains(" ")) {
            lineObject.setOpcode(opcodeLine);
            return lineObject;
        } else {
            String opcode = opcodeLine.substring(0, opcodeLine.indexOf(' ')).trim();
            lineObject.setOpcode(opcode);
            String argsLine = opcodeLine.substring(opcode.length()).trim();
            if (opcode.equalsIgnoreCase("CONSTS")) {
                String strArg = argsLine.substring(1, argsLine.length() - 2);
                lineObject.setArgs(new Object[]{strArg});
            } else if (opcode.equalsIgnoreCase("CONSTF")) {
                float value = Float.parseFloat(argsLine);
                lineObject.setArgs(new Object[]{value});
            } else if (opcode.equalsIgnoreCase("ACTION")) {
                String functionName = argsLine.substring(0, argsLine.lastIndexOf(',')).trim();
                functionName = functionName.substring(0, functionName.indexOf('('));
                int argsCount = Integer.parseInt(argsLine.substring(argsLine.lastIndexOf(',') + 1).trim(), 16);
                lineObject.setArgs(new Object[]{functionName, argsCount});
            } else {
                String[] splittedArgs = argsLine.split(",");
                Object[] args = new Object[splittedArgs.length];
                for (int i = 0; i < splittedArgs.length; i++) {
                    args[i] = Integer.parseInt(splittedArgs[i].trim(), 16);
                }

                lineObject.setArgs(args);
            }

            return lineObject;
        }
    }
}
