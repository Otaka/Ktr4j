package com.ncsdcmp.nwscript;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;

/**
 * @author sad
 */
public class NwScriptParser {

    private Map<String, NwScriptVar> globalVariables = new HashMap<>();
    private List<NwFunction> functions = new ArrayList<>();

    private Pattern functionPattern = Pattern.compile("(.+?)\\s+(.+?)\\s*\\((.*?)\\)\\s*;");
    private Pattern varPatternWithInitializer = Pattern.compile("(.+?)\\s+(.+?)\\s*=\\s*(.+?)$");
    private Pattern varPatternWithoutInitializer = Pattern.compile("(.+?)\\s+(.+?)$");

    public NwScriptParser() {
        NwScriptVar objectSelf = new NwScriptVar("object", "OBJECT_SELF", "0");
        globalVariables.put(objectSelf.getName(), objectSelf);
        NwScriptVar objectInvalid = new NwScriptVar("object", "OBJECT_INVALID", "-1");
        globalVariables.put(objectInvalid.getName(), objectInvalid);
    }

    private String readFileToString(InputStream stream) throws IOException {
        String value = IOUtils.toString(stream, StandardCharsets.UTF_8);
        return value;
    }

    protected static String removeMultilineComment(String line) {
        StringBuilder sb = new StringBuilder(line.length());
        int size = line.length();
        boolean inComment = false;
        for (int i = 0; i < size; i++) {
            char c = line.charAt(i);
            if (c == '/' && line.charAt(i + 1) == '*') {
                inComment = true;
            }
            if(inComment&&(c=='*'&&line.charAt(i+1)=='/')){
                sb.append("  ");
                i++;
                inComment=false;
                continue;
            }
            if (inComment && !(c == '\r' || c == '\n')) {
                c = ' ';
            }

            sb.append(c);
        }
        return sb.toString();
    }

    public BasicNwScriptNss parseNWScript(InputStream stream) throws IOException {
        String fileContent = readFileToString(stream);
        fileContent = removeMultilineComment(fileContent);
        String[] lines = fileContent.split("\\r?\\n");
        BasicNwScriptNss nwScriptNss = new BasicNwScriptNss();
        for (int i = 0; i < lines.length; i++) {

            String line = removeComment(lines[i]).trim();
            if (!line.isEmpty()) {
                parseLine(line, i);
            }
        }

        return nwScriptNss;
    }

    private String removeComment(String line) {
        int index = line.indexOf("//");
        if (index == -1) {
            return line;
        }
        return line.substring(0, index);
    }

    private void parseLine(String line, int index) {
        line = line.replaceAll("\\s\\s\\s*", " ");
        Matcher matcher = functionPattern.matcher(line);
        if (matcher.matches()) {
            parseFunction(line, matcher, index);
            return;
        }
        if (line.endsWith(";")) {
            line = line.substring(0, line.length() - 1);
        }

        matcher = varPatternWithInitializer.matcher(line);
        if (matcher.matches()) {
            parseGlobalVar(line, index, matcher);
            return;
        }

        if (line.startsWith("#define")) {//for now skip this elements
            return;
        }
        throw new RuntimeException("Cannot parse line #" + index + " [" + line + "]");
    }

    private Object parseInitValue(String initValueString, int lineIndex) {
        if (globalVariables.containsKey(initValueString)) {
            return new NwVariableUsage(globalVariables.get(initValueString));
        }

        if (initValueString.startsWith("\"") && initValueString.endsWith("\"")) {
            return initValueString.substring(1, initValueString.length() - 1);
        }
        if (initValueString.startsWith("[") && initValueString.endsWith("]")) {
            String vectorContentString = initValueString.substring(1, initValueString.length() - 1);
            String[] parts = vectorContentString.split(",");
            NwVector vector = new NwVector();
            for (String part : parts) {
                part = part.trim();
                vector.addValue(Float.parseFloat(part));
            }
            return vector;
        }

        if (initValueString.endsWith("f")) {
            try {
                initValueString = initValueString.substring(0, initValueString.length() - 1);
                return Float.parseFloat(initValueString);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse float value [" + initValueString + "] found at line #" + lineIndex, ex);
            }
        }
        if (initValueString.contains(".")) {
            try {
                return Float.parseFloat(initValueString);
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot parse float value [" + initValueString + "] found at line #" + lineIndex, ex);
            }
        }
        try {
            return Integer.parseInt(initValueString);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot parse integer value [" + initValueString + "] found at line #" + lineIndex, ex);
        }
    }

    private void checkIfVariableCompatibleWithReceiverType(NwVariableUsage var, String receiverType, int lineIndex) {
        String varType = var.getScriptVar().getType();
        if (var.getScriptVar().getType().equals("int") && receiverType.equals("object")) {
            return;//we can assign integer to object
        }
        if (!varType.equals(receiverType)) {
            throw new IllegalArgumentException("Error on line #" + lineIndex + ". Cannot assign variable [" + var + "] to variable with type [" + receiverType + "]");
        }
    }

    private NwScriptVar parseVariable(String varType, String varName, String varInitializerString, int lineIndex) {
        Object varInitValueObject = null;
        if (varInitializerString != null) {
            varInitValueObject = parseInitValue(varInitializerString, lineIndex);
            if (varInitValueObject instanceof NwVariableUsage) {
                checkIfVariableCompatibleWithReceiverType((NwVariableUsage) varInitValueObject, varType, lineIndex);
            } else if (varInitValueObject instanceof String) {
                if (!varType.equals("string")) {
                    throw new IllegalArgumentException("Cannot assign string [" + varInitValueObject + "] to variable [" + varType + " " + varName + "]");
                }
            } else if (varInitValueObject instanceof Integer) {
                if (!varType.equals("int")) {
                    throw new IllegalArgumentException("Cannot assign integer value [" + varInitValueObject + "] to variable [" + varType + " " + varName + "]");
                }
            } else if (varInitValueObject instanceof Float) {
                if (!varType.equals("float")) {
                    throw new IllegalArgumentException("Cannot assign float value [" + varInitValueObject + "] to variable [" + varType + " " + varName + "]");
                }
            } else if (varInitValueObject instanceof NwVector) {
                if (!varType.equals("vector")) {
                    throw new IllegalArgumentException("Cannot assign vector value [" + varInitValueObject + "] to variable [" + varType + " " + varName + "]");
                }
            }
        }
        NwScriptVar var = new NwScriptVar(varType, varName, varInitValueObject);
        return var;
    }

    private void parseGlobalVar(String line, int lineIndex, Matcher matcher) {
        String varType = matcher.group(1).trim();
        String varName = matcher.group(2).trim();
        String varInitializerString = matcher.group(3).trim();
        NwScriptVar var = parseVariable(varType, varName, varInitializerString, lineIndex);
        globalVariables.put(varName, var);
    }

    private NwScriptVar parseArgument(String line, int lineIndex, int argumentIndex) {
        String type;
        String name;
        String initializer = null;
        if (varPatternWithInitializer.matcher(line).matches()) {
            Matcher matcher = varPatternWithInitializer.matcher(line);
            matcher.matches();
            type = matcher.group(1).trim();
            name = matcher.group(2).trim();
            initializer = matcher.group(3).trim();
        } else if (varPatternWithoutInitializer.matcher(line).matches()) {
            Matcher matcher = varPatternWithoutInitializer.matcher(line);
            matcher.matches();
            type = matcher.group(1).trim();
            name = matcher.group(2).trim();
        } else {
            throw new IllegalArgumentException("Cannot parse argument [" + line + "] on line #" + lineIndex);
        }

        NwScriptVar var = parseVariable(type, name, initializer, lineIndex);
        return var;
    }

    private static String replaceWithPlaceholder(String originalString, int start, int end, String placeholder) {
        String partBefore = originalString.substring(0, start);
        String partEnd = originalString.substring(end);
        return partBefore + placeholder + partEnd;
    }

    protected static List<String> splitFunctionArgsString(String argString) {
        Map<String, String> placeholders = new HashMap<>();
        int counter = 0;
        //replace strings
        while (true) {
            int indexStart = argString.indexOf('"');
            if (indexStart != -1) {
                int indexEnd = argString.indexOf('"', indexStart + 1) + 1;
                String placeholder = "$_PLH_" + (counter++);
                String actualValue = argString.substring(indexStart, indexEnd);
                placeholders.put(placeholder, actualValue);
                argString = replaceWithPlaceholder(argString, indexStart, indexEnd, placeholder);
            } else {
                break;
            }
        }

        //replace strings
        while (true) {
            int indexStart = argString.indexOf('[');
            if (indexStart != -1) {
                int indexEnd = argString.indexOf(']', indexStart + 1) + 1;
                String placeholder = "$_PLH_" + (counter++);
                String actualValue = argString.substring(indexStart, indexEnd);
                placeholders.put(placeholder, actualValue);
                argString = replaceWithPlaceholder(argString, indexStart, indexEnd, placeholder);
            } else {
                break;
            }
        }

        List<String> result = new ArrayList<>();
        String[] splittedArguments = argString.split(",");
        for (String splittedArg : splittedArguments) {
            for (String key : placeholders.keySet()) {
                String placeholderValue = placeholders.get(key);
                splittedArg = splittedArg.replace(key, placeholderValue);
            }

            result.add(splittedArg.trim());
        }

        return result;
    }

    private void parseFunction(String line, Matcher matcher, int lineIndex) {
        String functionReturnType = matcher.group(1).trim();
        String functionName = matcher.group(2).trim();
        String functionArgumentsString = matcher.group(3).trim();

        List<String> functionArgumentStringList = Collections.EMPTY_LIST;
        if (!functionArgumentsString.isEmpty()) {
            functionArgumentStringList = splitFunctionArgsString(functionArgumentsString);
        }

        NwFunction function = new NwFunction();

        int argIndex = -1;
        for (String argString : functionArgumentStringList) {
            argIndex++;
            NwScriptVar argumentVariable = parseArgument(argString, lineIndex, argIndex);
            function.getArgs().add(argumentVariable);
        }

        function.setReturnType(functionReturnType);
        function.setName(functionName);
        function.setFunctionIndex(functions.size());
        functions.add(function);
    }
}
