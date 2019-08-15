package com.ncsdcmp.analyzers;

import com.ncsdcmp.Line;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author sad
 */
public class FileParserAnalyzer extends AbstractAnalyzer {

    @Override
    public void analyze() {
        parse();
    }

    private void parse() {
        getContext().getLines().clear();
        try (Scanner scanner = new Scanner(getContext().getInputStream(), StandardCharsets.UTF_8.name())) {
            int lineIndex = -1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                lineIndex++;
                getContext().getLines().add(parseLine(line, lineIndex));
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
        String opcodesString = addressHexLine.substring(address.length()).replaceAll("\\s", "");
        int instructionLength = opcodesString.length() / 2;
        lineObject.setAddress(Integer.parseInt(address, 16));
        lineObject.setInstructionBytesLength(instructionLength);

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
                    args[i] = parseSignedHexInt(splittedArgs[i].trim());
                }

                lineObject.setArgs(args);
            }

            return lineObject;
        }
    }

}
