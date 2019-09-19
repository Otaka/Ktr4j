package com.kotor4j.appstates.gui.widgets;

import com.kotor4j.appstates.gui.GuiAppState;

/**
 * @author Dmitry
 */
public class ButtonGuiWidget extends GuiWidget {

    private GuiBorder hilight;

    public ButtonGuiWidget(GuiAppState guiAppState) {
        super(guiAppState);
    }

    public GuiBorder getHilight() {
        return hilight;
    }

    public void setHilight(GuiBorder hilight) {
        this.hilight = hilight;
    }

}
