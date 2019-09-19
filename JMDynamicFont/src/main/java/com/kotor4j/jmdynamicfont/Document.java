package com.kotor4j.jmdynamicfont;

import gnu.trove.list.TCharList;
import gnu.trove.list.TIntList;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableInt;

/**
 * @author Dmitry
 */
public class Document {

    private List<Line> lines;
    private int textVerticalOffset;
    private int maximumWidth;
    
    public Document(List<Line> lines) {
        this.lines = lines;
    }

    public void setMaximumWidth(int maximumWidth) {
        this.maximumWidth = maximumWidth;
    }

    public int getMaximumWidth() {
        return maximumWidth;
    }
    
    

    public void setTextVerticalOffset(int textVerticalOffset) {
        this.textVerticalOffset = textVerticalOffset;
    }

    public int getTextVerticalOffset() {
        return textVerticalOffset;
    }

    public List<Line> getLines() {
        return lines;
    }

    public static class Line {

        public int lineOffset;
        public int lineWidth;
        public TCharList chars;
        public TIntList widths;

        public Line(TCharList chars, TIntList widths) {
            this.chars = chars;
            this.widths = widths;
        }
    }

    public static String tokenize(String str, int fromIndex, MutableInt newInt, MutableInt width, WidthCalculator widthCalculator, StringBuilder tempStringBuilder) {
        if (fromIndex >= str.length()) {
            return null;
        }

        char c = str.charAt(fromIndex);
        if (c == '\n') {
            newInt.setValue(fromIndex + 1);
            width.setValue(0);
            return "\n";
        } else if (c == '\r') {
            newInt.setValue(fromIndex + 1);
            width.setValue(0);
            return " ";
        } else if (Character.isSpaceChar(c) || c == '\t'||c=='.'||c==','||c=='-'||c==';'||c=='/'||c=='\\') {
            newInt.setValue(fromIndex + 1);
            width.setValue(widthCalculator.getCharWidth(c));
            return String.valueOf(c);
        } else {
            int strWidth = 0;
            tempStringBuilder.setLength(0);
            for (int i = fromIndex; i < str.length(); i++) {
                c = str.charAt(i);
                if (c != '\n' && c != '\r' && !(Character.isSpaceChar(c) || c == '\t'||c=='.'||c==',')) {
                    strWidth += widthCalculator.getCharWidth(c);
                    tempStringBuilder.append(c);
                } else {
                    width.setValue(strWidth);
                    newInt.setValue(i);
                    return tempStringBuilder.toString();
                }
            }

            width.setValue(strWidth);
            newInt.setValue(str.length());
            return tempStringBuilder.toString();
        }
    }
}
