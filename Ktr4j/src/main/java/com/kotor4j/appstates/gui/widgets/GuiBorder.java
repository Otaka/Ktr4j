package com.kotor4j.appstates.gui.widgets;

/**
 * @author Dmitry
 */
public class GuiBorder {

    private String corner;
    private String edge;
    private String fill;
    private int fillStyle;
    private int dimension;
    private int innerOffset;
    private boolean pulsing;
    private GuiColor guiColor;

    public void setCorner(String corner) {
        this.corner = corner;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setEdge(String edge) {
        this.edge = edge;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

    public void setFillStyle(int fillStyle) {
        this.fillStyle = fillStyle;
    }

    public void setGuiColor(GuiColor guiColor) {
        this.guiColor = guiColor;
    }

    public void setInnerOffset(int innerOffset) {
        this.innerOffset = innerOffset;
    }

    public void setPulsing(boolean pulsing) {
        this.pulsing = pulsing;
    }

    public String getCorner() {
        return corner;
    }

    public String getEdge() {
        return edge;
    }

    public String getFill() {
        return fill;
    }

    public int getFillStyle() {
        return fillStyle;
    }

    public int getDimension() {
        return dimension;
    }

    public int getInnerOffset() {
        return innerOffset;
    }

    public boolean isPulsing() {
        return pulsing;
    }

    public GuiColor getGuiColor() {
        return guiColor;
    }

}
