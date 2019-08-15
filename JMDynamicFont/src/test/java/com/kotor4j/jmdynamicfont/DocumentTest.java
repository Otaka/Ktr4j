package com.kotor4j.jmdynamicfont;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.mutable.MutableInt;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Dmitry
 */
public class DocumentTest {

    @Test
    public void testTokenize() {
        String str = "My String\t value\r\nand next line\n abc";
        List<String> expected = new ArrayList<>();
        expected.add("My");
        expected.add(" ");
        expected.add("String");
        expected.add("\t");
        expected.add(" ");
        expected.add("value");
        expected.add(" ");
        expected.add("\n");
        expected.add("and");
        expected.add(" ");
        expected.add("next");
        expected.add(" ");
        expected.add("line");
        expected.add("\n");
        expected.add(" ");
        expected.add("abc");

        MutableInt tIndex = new MutableInt();
        MutableInt tokenWidth = new MutableInt();
        WidthCalculator wc = new WidthCalculator() {
            @Override
            public int getCharWidth(char c) {
                return 0;
            }
        };

        StringBuilder tempStringBuilder = new StringBuilder();
        List<String>resultList=new ArrayList<>();
        String token = null;
        while ((token = Document.tokenize(str, tIndex.getValue(), tIndex, tokenWidth, wc, tempStringBuilder)) != null) {
            resultList.add(token);
        }
        
        Assert.assertArrayEquals(expected.toArray(), resultList.toArray());
    }

}
