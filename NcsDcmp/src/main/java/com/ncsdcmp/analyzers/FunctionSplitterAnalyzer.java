package com.ncsdcmp.analyzers;

import com.ncsdcmp.FunctionInfo;
import com.ncsdcmp.Line;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author sad
 */
public class FunctionSplitterAnalyzer extends AbstractAnalyzer {

    private Map<Integer, FunctionInfo> address2Function = new HashMap<>();

    private Queue<Integer> functionsToProcess = new ArrayDeque<>();
    private Set<Integer> processedAddresses = new HashSet<>();

    @Override
    public void analyze() {
        FunctionInfo rootFunction = processFunction(8);
        getContext().setRootFunction(rootFunction);
        while (!functionsToProcess.isEmpty()) {
            Integer functionAddress = functionsToProcess.poll();
            if (!address2Function.containsKey(functionAddress)) {
                FunctionInfo function = processFunction(functionAddress);
            }
        }

        getContext().getFunctions().addAll(address2Function.values());
        sortFunctionsByAddress(getContext().getFunctions());
    }

    private void sortFunctionsByAddress(List<FunctionInfo> functions) {
        Collections.sort(functions, new Comparator<FunctionInfo>() {
            @Override
            public int compare(FunctionInfo o1, FunctionInfo o2) {
                return o1.getAddress() - o2.getAddress();
            }
        });
    }

    private FunctionInfo processFunction(int functionStartAddress) {
        FunctionInfo functionObject = new FunctionInfo(functionStartAddress);
        address2Function.put(functionStartAddress, functionObject);

        Queue<Integer> addressesToCheck = new ArrayDeque<>();
        addressesToCheck.add(functionStartAddress);
        int latestRetAddress = functionStartAddress;
        while (!addressesToCheck.isEmpty()) {
            int address = addressesToCheck.poll();
            if (processedAddresses.contains(address)) {
                continue;
            }

            processedAddresses.add(address);
            int lineIndex = getInstructionIndexByAddress(address);
            OUTER:
            while (true) {
                Line line = getContext().getLines().get(lineIndex);
                switch (line.getOpcode()) {
                    case "JSR": {
                        int functionAddress = (int) line.getArgs()[0];
                        functionsToProcess.add(functionAddress);
                        break;
                    }
                    case "RETN": {
                        if (line.getAddress() > latestRetAddress) {
                            latestRetAddress = line.getAddress();
                        }
                        break OUTER;
                    }
                    case "JMP": {
                        int jumpAddress = (int) line.getArgs()[0];
                        addressesToCheck.add(jumpAddress);
                        break;
                    }
                    case "JZ":
                    case "JNZ": {
                        int jumpAddress = (int) line.getArgs()[0];
                        addressesToCheck.add(jumpAddress);//check adress where instruction jumps
                        addressesToCheck.add(getContext().getLines().get(lineIndex + 1).getAddress());//check address right after instruction
                        break;
                    }
                    default:
                        break;
                }
                lineIndex++;
            }
        }

        functionObject.setLength(latestRetAddress - functionStartAddress);
        return functionObject;
    }
}
