package com.kotor4j;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.system.AppSettings;
import com.kotor4j.jmdynamicfont.Alignment;
import com.kotor4j.jmdynamicfont.DynamicFont;
import com.kotor4j.jmdynamicfont.DynamicFontLabel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class DynamicFontMain extends SimpleApplication {

    private DynamicFont smallFont;
    private DynamicFont font;
    private DynamicFontLabel singleLineLabel;
    private DynamicFontLabel singleLineLabel2;
    private DynamicFontLabel multilineLabel;
    private DynamicFontLabel mouseInfoLabel;
    private long time;
    private int alignIndex = 0;
    private List<AlignmentPair> testAlignments = new ArrayList<>();

    @Override
    public void simpleInitApp() {
        createTestAlignments();
        font = new DynamicFont(30);
        smallFont = new DynamicFont(15);
        getFlyByCamera().setDragToRotate(true);
        singleLineLabel = new DynamicFontLabel(getAssetManager(), font, false, 600, 100);
        singleLineLabel.setAlignment(Alignment.LEFT, Alignment.TOP);
        singleLineLabel.setBackgroundColor(ColorRGBA.Gray);
        singleLineLabel.setText("Привет мир");
        singleLineLabel.setLocalTranslation(10, 590, 0);
        guiNode.attachChild(singleLineLabel);
        
        singleLineLabel2 = new DynamicFontLabel(getAssetManager(), font, false, 300, -1);
        singleLineLabel2.setAlignment(Alignment.LEFT, Alignment.TOP);
        singleLineLabel2.setBackgroundColor(ColorRGBA.Brown);
        singleLineLabel2.setText("Привет мир");
        singleLineLabel2.setLocalTranslation(10, 450, 0);
        guiNode.attachChild(singleLineLabel2);
       
        multilineLabel = new DynamicFontLabel(getAssetManager(), font, true, 300,100);
        multilineLabel.setAlignment(Alignment.CENTER, Alignment.TOP);
        multilineLabel.setBackgroundColor(ColorRGBA.Brown);
        multilineLabel.setText("ネコ（猫）は、狭義には 食肉目ネコ科ネコ属に 分類されるヨ");
        multilineLabel.setLocalTranslation(10, 350, 0);
        guiNode.attachChild(multilineLabel);
        
        
        mouseInfoLabel = new DynamicFontLabel(getAssetManager(), smallFont, false,-1 , -1);
        mouseInfoLabel.setAlignment(Alignment.LEFT, Alignment.TOP);
        mouseInfoLabel.setLocalTranslation(610, 590, 0);
        guiNode.attachChild(mouseInfoLabel);
        
        
        time = System.currentTimeMillis();
    }

    @Override
    public void simpleUpdate(float tpf) {
        //set new alignment every second
        long currentTime = System.currentTimeMillis();
        if (currentTime - time > 1000) {
            time = currentTime;
            
            AlignmentPair alignmentPair=testAlignments.get(alignIndex%(testAlignments.size()-1));
            singleLineLabel.setAlignment(alignmentPair.horizontal, alignmentPair.vertical);
            alignIndex++;
        }
        //set text with tpf
        singleLineLabel.setText("Привет мир " + tpf);
        singleLineLabel2.setText("Привет мир " + tpf);
        
        //set mouse information label
        mouseInfoLabel.setText("X:"+inputManager.getCursorPosition().x+"\tY:"+inputManager.getCursorPosition().y);
        
//update font texture if needed
        font.updateTexture();
        smallFont.updateTexture();
    }
    
    private void createTestAlignments(){
        testAlignments.add(new AlignmentPair(Alignment.LEFT,Alignment.TOP));
        testAlignments.add(new AlignmentPair(Alignment.CENTER,Alignment.TOP));
        testAlignments.add(new AlignmentPair(Alignment.RIGHT,Alignment.TOP));
        testAlignments.add(new AlignmentPair(Alignment.RIGHT,Alignment.CENTER));
        testAlignments.add(new AlignmentPair(Alignment.CENTER,Alignment.CENTER));
        testAlignments.add(new AlignmentPair(Alignment.LEFT,Alignment.CENTER));
        testAlignments.add(new AlignmentPair(Alignment.LEFT,Alignment.BOTTOM));
        testAlignments.add(new AlignmentPair(Alignment.CENTER,Alignment.BOTTOM));
        testAlignments.add(new AlignmentPair(Alignment.RIGHT,Alignment.BOTTOM));
        testAlignments.add(new AlignmentPair(Alignment.RIGHT,Alignment.CENTER));
        testAlignments.add(new AlignmentPair(Alignment.CENTER,Alignment.CENTER));
        testAlignments.add(new AlignmentPair(Alignment.LEFT,Alignment.CENTER));
        testAlignments.add(new AlignmentPair(Alignment.LEFT,Alignment.CENTER));
    }

    public static void main(String[] args) {
        DynamicFontMain guiMain = new DynamicFontMain();
        guiMain.setShowSettings(false);
        AppSettings settings = new AppSettings(true);
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setVSync(true);
        guiMain.setSettings(settings);

        guiMain.start();
    }

    private static class AlignmentPair {

        Alignment horizontal;
        Alignment vertical;

        public AlignmentPair(Alignment horizontal, Alignment vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }
        
    }
}
