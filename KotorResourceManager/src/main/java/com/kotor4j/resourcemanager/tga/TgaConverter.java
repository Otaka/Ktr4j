package com.kotor4j.resourcemanager.tga;

import com.kotor4j.io.NwnByteArrayInputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author Dmitry
 */
public class TgaConverter {

    public static BufferedImage convertTga(NwnByteArrayInputStream stream) throws IOException {
        byte[] buffer = stream.getBuffer();
        int height = TgaReader.getHeight(buffer);
        int width = TgaReader.getWidth(buffer);
        int[] convertedImage = TgaReader.read(buffer, TgaReader.ARGB);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, convertedImage, 0, width);
        return image;
    }
}
