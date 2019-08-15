package com.ncsdcmp.nwscript;

import org.junit.Test;
import static org.junit.Assert.*;

public class NwScriptParserTest {

    @Test
    public void testParseNWScript() throws Exception {
        assertEquals("1\n2\n   \n \n   6\n7", NwScriptParser.removeMultilineComment("1\n2\n/*3\n4\n5*/6\n7"));
    }
}
