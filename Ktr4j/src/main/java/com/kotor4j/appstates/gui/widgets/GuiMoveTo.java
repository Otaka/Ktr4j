package com.kotor4j.appstates.gui.widgets;

/**
 * @author Dmitry
 */
public class GuiMoveTo {

    private int down;
    private int left;
    private int right;
    private int up;

    public GuiMoveTo(int down, int left, int right, int up) {
        this.down = down;
        this.left = left;
        this.right = right;
        this.up = up;
    }

    public int getDown() {
        return down;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getUp() {
        return up;
    }

}
