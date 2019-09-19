package com.kotor4j.kotorconverter.lyt;

import com.kotor4j.kotorconverter.original.lyt.FileReaderLyt;
import com.kotor4j.kotorconverter.original.lyt.LytFile;
import java.io.File;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Dmitry
 */
public class FileReaderLytTest {

    @Test
    public void testLoadFile() throws Exception {
        LytFile lytFile = new FileReaderLyt().loadFile(FileReaderLytTest.class.getResourceAsStream("test_lyt.lyt"), "test_lyt.lyt");
        Assert.assertEquals("testfile.max", lytFile.getFileDependency());
        Assert.assertEquals(2, lytFile.getRooms().size());
        Assert.assertEquals("aaba", lytFile.getRooms().get(0).get("model"));
        Assert.assertEquals(1.0f, lytFile.getRooms().get(0).get("x"));
        Assert.assertEquals(2.0f, lytFile.getRooms().get(0).get("y"));
        Assert.assertEquals(3.0f, lytFile.getRooms().get(0).get("z"));
        Assert.assertEquals("aabb", lytFile.getRooms().get(1).get("model"));
        Assert.assertEquals(4.0f, lytFile.getRooms().get(1).get("x"));
        Assert.assertEquals(5.0f, lytFile.getRooms().get(1).get("y"));
        Assert.assertEquals(6.0f, lytFile.getRooms().get(1).get("z"));
        Assert.assertEquals(1, lytFile.getTrackObjects().size());
        Assert.assertEquals(1, lytFile.getObstacleObjects().size());
        Assert.assertEquals(2, lytFile.getDoorHooks().size());
    }

}
