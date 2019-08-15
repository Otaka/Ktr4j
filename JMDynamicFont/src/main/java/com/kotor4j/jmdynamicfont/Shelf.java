package com.kotor4j.jmdynamicfont;

/**
 * @author sad
 */
public class Shelf {

    /*
    empty width left in this shelf
     */
    private int leftSpace;
    private int shelfYPosition;

    public Shelf(int leftSpace, int shelfYPosition) {
        this.leftSpace = leftSpace;
        this.shelfYPosition = shelfYPosition;
    }

    public int getShelfYPosition() {
        return shelfYPosition;
    }

    public int getLeftSpace() {
        return leftSpace;
    }

    public void setLeftSpace(int leftSpace) {
        this.leftSpace = leftSpace;
    }

    public void decreaseEmptySpace(int w) {
        leftSpace -= (w+1);
    }
}
