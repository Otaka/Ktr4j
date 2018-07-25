package com.kotor4j.resourcemanager._2da;

import com.kotor4j.utils.StringBuilderWithPadding;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author sad
 */
public class Array2da {

    private List<String> columnNames;
    private List<String> rowLabels;
    private List<String[]> rowData;
    private String fileName;

    public Array2da() {
    }

    public List<String> getRowLabels() {
        return rowLabels;
    }

    public void setRowLabels(List<String> rowLabels) {
        this.rowLabels = rowLabels;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String[]> getRowData() {
        return rowData;
    }

    public void setRowData(List<String[]> rowData) {
        this.rowData = rowData;
    }

    @Override
    public String toString() {
        return fileName + " Entries count:" + rowData.size();
    }

    public String toJson() {
        StringBuilderWithPadding sb = new StringBuilderWithPadding();
        sb.println("[");
        sb.incLevel();

        for (int j = 0; j < rowData.size(); j++) {
            if (j > 0) {
                sb.println(",");
            }

            sb.println("{");
            sb.incLevel();
            String[] rows = rowData.get(j);
            for (int i = 0; i < columnNames.size(); i++) {
                if (i > 0) {
                    sb.println(",");
                }

                String value = rows[i];
                value = StringEscapeUtils.escapeJson(value);
                sb.print("\"").print(columnNames.get(i)).print("\":\"").print(value).print("\"");
            }

            sb.decLevel();
            sb.println();
            sb.print("}");
        }

        sb.decLevel();
        sb.println();
        sb.print("]");
        return sb.toString();
    }
}
