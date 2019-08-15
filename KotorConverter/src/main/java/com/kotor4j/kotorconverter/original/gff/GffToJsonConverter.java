package com.kotor4j.kotorconverter.original.gff;

import com.kotor4j.kotorconverter.original.gff.fields.*;
import com.kotor4j.kotorconverter.original.gff.filetypes.AbstractGffResource;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Dmitry
 */
public class GffToJsonConverter {

    public String gffToJson(AbstractGffResource gffResource) {
        Gff gff = gffResource.getGff();
        StringBuilder sb = new StringBuilder();
        serializeGffStructure(gff.getRoot(), sb, 0);
        return sb.toString();
    }

    private void serializeGffStructure(GffStructure gffStructure, StringBuilder sb, int level) {
        sb.append(StringUtils.repeat("  ", level)).append("{\n");
        level++;
        String prefix = StringUtils.repeat("  ", level);
        boolean first = true;
        for (GffField field : gffStructure.getFields()) {
            if (first == false) {
                sb.append(",\n");
            }
            first = false;

            sb.append(prefix).append("\"").append(StringEscapeUtils.escapeJson(field.getLabel())).append("\":");
            GffFieldValue fieldValue = field.getValue();
            if (fieldValue instanceof GffByte) {
                sb.append(((GffByte) fieldValue).getValue());
            } else if (fieldValue instanceof GffChar) {
                sb.append("\"").append(((GffChar) fieldValue).getValue()).append("\"");
            } else if (fieldValue instanceof GffDWord) {
                sb.append(((GffDWord) fieldValue).getValue());
            } else if (fieldValue instanceof GffWord) {
                sb.append(((GffWord) fieldValue).getValue());
            } else if (fieldValue instanceof GffDWord64) {
                sb.append(((GffDWord64) fieldValue).getValue());
            } else if (fieldValue instanceof GffDouble) {
                sb.append(((GffDouble) fieldValue).getValue());
            } else if (fieldValue instanceof GffExoString) {
                String value = ((GffExoString) fieldValue).getValue();
                sb.append("\"").append(StringEscapeUtils.escapeJson(value)).append("\"");
            } else if (fieldValue instanceof GffResRef) {
                String value = ((GffResRef) fieldValue).getValue();
                sb.append("\"").append(StringEscapeUtils.escapeJson(value)).append("\"");
            } else if (fieldValue instanceof GffFloat) {
                sb.append(((GffFloat) fieldValue).getValue());
            } else if (fieldValue instanceof GffInt) {
                sb.append(((GffInt) fieldValue).getValue());
            } else if (fieldValue instanceof GffInt64) {
                sb.append(((GffInt64) fieldValue).getValue());
            } else if (fieldValue instanceof GffInt64) {
                sb.append(((GffInt64) fieldValue).getValue());
            } else if (fieldValue instanceof GffShort) {
                sb.append(((GffShort) fieldValue).getValue());
            } else if (fieldValue instanceof GffVector) {
                GffVector vector = (GffVector) fieldValue;
                sb.append("[")
                        .append(vector.getValue()[0]).append(",")
                        .append(vector.getValue()[1]).append(",")
                        .append(vector.getValue()[2])
                        .append("]");
            } else if (fieldValue instanceof GffOrientation) {
                GffOrientation orientation = (GffOrientation) fieldValue;
                sb.append("[")
                        .append(orientation.getValue()[0]).append(",")
                        .append(orientation.getValue()[1]).append(",")
                        .append(orientation.getValue()[2]).append(",")
                        .append(orientation.getValue()[3])
                        .append("]");
            } else if (fieldValue instanceof GffVoid) {
                byte[] byteArray = ((GffVoid) fieldValue).getValue();
                String value = new String(Base64.getEncoder().encode(byteArray), StandardCharsets.UTF_8);
                sb.append("\"").append(StringEscapeUtils.escapeJson(value)).append("\"");
            } else if (fieldValue instanceof GffStruct) {
                GffStruct struct = (GffStruct) fieldValue;
                serializeGffStructure(struct.getValue(), sb, level + 1);
            } else if (fieldValue instanceof GffExoLocString) {
                GffExoLocString locString = (GffExoLocString) fieldValue;
                int stringResRef=locString.getStringRef();
                sb.append("{\n");
                sb.append(prefix).append(prefix).append("\"stringRef\":").append(stringResRef).append(",\n");
                sb.append(prefix).append(prefix).append("\"variants\":");
                if (locString.getValue().length == 0) {
                    sb.append("[ ]");
                } else {
                    boolean first2 = true;
                    String subStringPrefix = StringUtils.repeat("  ", level + 1);
                    String innerSubStringPrefix = StringUtils.repeat("  ", level + 2);
                    sb.append("[\n");
                    for (GffExoLocString.GffExoLocSubString substring : locString.getValue()) {
                        if (first2 == false) {
                            sb.append(",\n");
                        }
                        first2 = false;
                        sb.append(subStringPrefix).append("{\n");
                        sb.append(innerSubStringPrefix).append("\"").append("id").append("\":").append(substring.getStringId()).append(",\n");
                        sb.append(innerSubStringPrefix).append("\"").append("id").append("\":\"").append(StringEscapeUtils.escapeJson(substring.getValue())).append("\"\n");
                        sb.append(subStringPrefix).append("}");
                    }

                    sb.append("\n");
                    sb.append(subStringPrefix).append("]");
                }
                sb.append("\n").append(prefix);
                sb.append("}");
            } else if (fieldValue instanceof GffList) {
                GffList list = (GffList) fieldValue;
                if (list.getValue().length == 0) {
                    sb.append("[ ]");
                } else {
                    sb.append("[\n");
                    boolean first2 = true;
                    for (GffStructure gffStruct : list.getValue()) {
                        if (first2 == false) {
                            sb.append(",\n");
                        }
                        first2 = false;
                        serializeGffStructure(gffStruct, sb, level + 2);
                    }
                    sb.append("\n");
                    sb.append(prefix).append("]");
                }
            } else {
                throw new IllegalStateException("Unimplemented gff field type [" + fieldValue.getClass().getSimpleName() + "]");
            }
        }
        sb.append("\n");
        level--;
        sb.append(StringUtils.repeat("  ", level)).append("}");

    }
}
