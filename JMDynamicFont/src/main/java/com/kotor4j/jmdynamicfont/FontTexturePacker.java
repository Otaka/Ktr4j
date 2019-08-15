package com.kotor4j.jmdynamicfont;

import com.kotor4j.jmdynamicfont.ShelfRectPacker.Rect;
import com.kotor4j.jmdynamicfont.exceptions.NotEnoughSpaceInField;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sad
 */
public class FontTexturePacker {

    private String fontName;
    private int fontSize;
    private int textureAtlasWidth;
    private int textureAtlasHeight;
    private ShelfRectPacker rectPacker;
    private BufferedImage image;
    private Graphics2D g2d;
    private FontMetrics fontMetrics;
    private Map<Character, CharInfo> char2CharInfo = new HashMap<>();
    private int fontHeight;

    public FontTexturePacker(String fontName, int fontSize, int textureAtlasWidth, int textureAtlasHeight) {
        this.fontName = fontName;
        this.fontSize = fontSize;
        this.textureAtlasWidth = textureAtlasWidth;
        this.textureAtlasHeight = textureAtlasHeight;
        image = new BufferedImage(textureAtlasWidth, textureAtlasHeight, BufferedImage.TYPE_4BYTE_ABGR);
        g2d = (Graphics2D) image.getGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        Font font = new Font(fontName, 0, fontSize);
        g2d.setFont(font);
        fontMetrics = g2d.getFontMetrics();
        fontHeight = fontMetrics.getHeight();
        rectPacker = new ShelfRectPacker(fontMetrics.getHeight(), textureAtlasWidth, textureAtlasHeight);
    }

    public BufferedImage getImage() {
        g2d.dispose();
        g2d = (Graphics2D) image.getGraphics();
        return image;
    }

    public void proposeLetter(char c) throws NotEnoughSpaceInField {
        if (!char2CharInfo.containsKey(c)) {
            int width = fontMetrics.charWidth(c);
            if (width > 0) {
                Rect rect = rectPacker.proposeRectangle(width);
                CharInfo ci = new CharInfo(c, rect);
                char2CharInfo.put(c, ci);
                // g2d.setColor(Color.LIGHT_GRAY);
                // g2d.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                g2d.setColor(Color.BLACK);
                g2d.drawString(String.valueOf(c), rect.getX(), rect.getY() + fontHeight - fontMetrics.getDescent());
            }
        }
    }
}
