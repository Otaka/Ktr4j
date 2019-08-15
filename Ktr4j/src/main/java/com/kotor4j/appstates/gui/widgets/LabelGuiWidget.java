package com.kotor4j.appstates.gui.widgets;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.kotor4j.appstates.gui.GuiAppState;
import com.kotor4j.resourcemanager.dialog.StringEntry;

/**
 * @author Dmitry
 */
public class LabelGuiWidget extends GuiWidget {

    private int alignment;
    private GuiColor color;
    private String fontName;
    private String text;
    private StringEntry strRef;
    private boolean pulsing;

    public LabelGuiWidget(GuiAppState guiAppState) {
        super(guiAppState);
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public GuiColor getColor() {
        return color;
    }

    public void setColor(GuiColor color) {
        this.color = color;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StringEntry getStrRef() {
        return strRef;
    }

    public void setStrRef(StringEntry strRef) {
        this.strRef = strRef;
    }

    public boolean isPulsing() {
        return pulsing;
    }

    public void setPulsing(boolean pulsing) {
        this.pulsing = pulsing;
    }

    @Override
    public void initializeComponent(GuiAppState appState) {
        super.initializeComponent(appState);
        BitmapFont myFont = appState.getApplication().getAssetManager().loadFont("Interface/Fonts/Console.fnt");
        BitmapText textObject=new BitmapText(myFont);
        String tText;
        if(strRef!=null){
            tText=strRef.getString();
        }else if(this.text!=null){
            tText=this.text;
        }else{
            tText="[EMPTY]";
        }
        textObject.setText(tText);
        getMainPicture().attachChild(textObject);
        
    }
}
