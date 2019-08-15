package com.kotor4j.jmdynamicfont;

import com.kotor4j.jmdynamicfont.exceptions.NotEnoughSpaceInField;
import java.util.ArrayList;
import java.util.List;

/**
 * Shelf 2d rectangles(with the same height) packer<br/>
 * For example it can pack font glyphs to texture
 */
public class ShelfRectPacker {

    private int rectangleHeight;
    private int fieldWidth;
    private int fieldHeight;
    private List<Shelf> shelfs = new ArrayList<>();
    private int filledHeightByShelves = 0;

    public ShelfRectPacker(int rectanglesHeight, int fieldWidth, int fieldHeight) {
        this.rectangleHeight = rectanglesHeight;
        this.fieldWidth = fieldWidth;
        this.fieldHeight = fieldHeight;
    }

    /**
    Try to find appropriate place for rectangle. Rectangle has width, and height that you set in constructor
     */
    public Rect proposeRectangle(int width) throws NotEnoughSpaceInField {
        Shelf shelf = findEmptyShelfOrAllocateNew(width);

        int x = fieldWidth - shelf.getLeftSpace();
        int y = shelf.getShelfYPosition();
        Rect rect = new Rect(x, y, width, rectangleHeight);
        shelf.decreaseEmptySpace(width);
        return rect;
    }

    private Shelf findEmptyShelfOrAllocateNew(int width) throws NotEnoughSpaceInField {
        for (Shelf s : shelfs) {
            if (s.getLeftSpace() > width) {
                return s;
            }
        }
        if ((filledHeightByShelves + rectangleHeight) > fieldHeight) {
            throw new NotEnoughSpaceInField("Cannot allocate space for rectangle [" + width + ":" + rectangleHeight + "] in field");
        }

        Shelf newShelf = new Shelf(fieldWidth, filledHeightByShelves);
        shelfs.add(newShelf);
        filledHeightByShelves += rectangleHeight;
        return newShelf;
    }

    public static class Rect {

        private int x;
        private int y;
        private int width;
        private int height;

        public Rect(int x, int y, int width, int height) {
            setBounds(x, y, width, height);
        }

        public Rect setBounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }
        
        
        public int getX2(){
            return x+width;
        }
        public int getY2(){
            return y+height;
        }
    }
}
