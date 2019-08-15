package com.kotor4j.jmdynamicfont;

import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.kotor4j.jmdynamicfont.ShelfRectPacker.Rect;
import com.kotor4j.jmdynamicfont.exceptions.NotEnoughSpaceInField;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class DynamicFont implements WidthCalculator {

    private List<SoftReference<DynamicFontLabel>> labels = new ArrayList<>();
    private ShelfRectPacker rectPacker;
    private BufferedImage image;
    private Graphics2D graphics;
    private Texture2D texture;
    private int currentTextureSize = 512;
    private FontMetrics fontMetrics;
    private int fontSize;
    private String fontName;
    private int fontHeightInPixels;
    private Map<Character, CharInfo> char2CharInfo = new HashMap<>();
    private boolean dirty = false;

    public DynamicFont(int fontSize) {
        this.fontSize = fontSize;
        createBackImage(currentTextureSize);
    }

    public DynamicFont(String fontName, int fontSize) {
        this.fontSize = fontSize;
        this.fontName = fontName;
        createBackImage(currentTextureSize);
    }

    private void createBackImage(int size) {
        image = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
        graphics = (Graphics2D) image.getGraphics();
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        String tFontName = fontName;
        if (tFontName == null) {
            tFontName = graphics.getFont().getName();
        }

        Font font = new Font(tFontName, 0, fontSize);
        graphics.setFont(font);

        fontMetrics = graphics.getFontMetrics();
        fontHeightInPixels = fontMetrics.getHeight();
        rectPacker = new ShelfRectPacker(fontHeightInPixels, size, size);
        Image jmeImage = new AWTLoader().load(image, false);
        texture = new Texture2D();
        texture.setImage(jmeImage);
    }

    public BufferedImage getImage() {
        return image;
    }

    public Texture2D getTexture() {
        return texture;
    }

    public int getFontHeightInPixels() {
        return fontHeightInPixels;
    }

    @Override
    public int getCharWidth(char c) {
        return fontMetrics.charWidth(c);
    }

    public Rect getCharRectangleInTexture(char c) {
        return char2CharInfo.get(c).getPositionInTexture();
    }

    public int getCurrentTextureSize() {
        return currentTextureSize;
    }

    public void offerChars(char[] chars) throws NotEnoughSpaceInField {
        for (char c : chars) {
            if (!char2CharInfo.containsKey(c)) {
                int width = fontMetrics.charWidth(c);
                Rect rect = rectPacker.proposeRectangle(width);
                CharInfo charInfo = new CharInfo(c, rect);
                char2CharInfo.put(c, charInfo);
                graphics.setColor(Color.WHITE);
                graphics.drawString(String.valueOf(c), rect.getX(), rect.getY() + fontHeightInPixels - fontMetrics.getDescent());
                dirty = true;
            }
        }
    }

    public void updateTexture() {
        if (dirty) {
            Image jmeImage = new AWTLoader().load(image, false);
            texture.setImage(jmeImage);
            dirty = false;
            /*try {
                ImageIO.write(image, "png", new File("f:/2.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }*/
        }
    }
}
