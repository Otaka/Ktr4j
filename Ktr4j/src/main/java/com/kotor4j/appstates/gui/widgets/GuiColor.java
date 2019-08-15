package com.kotor4j.appstates.gui.widgets;

/**
 * @author Dmitry
 */
public class GuiColor {

    private float red;
    private float green;
    private float blue;

    public GuiColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float getRed() {
        return red;
    }

    public void setRed(float red) {
        this.red = red;
    }

    public float getGreen() {
        return green;
    }

    public void setGreen(float green) {
        this.green = green;
    }

    public float getBlue() {
        return blue;
    }

    public void setBlue(float blue) {
        this.blue = blue;
    }

    @Override
    public String toString() {
        return red+", "+green+", "+blue;
    }

}
