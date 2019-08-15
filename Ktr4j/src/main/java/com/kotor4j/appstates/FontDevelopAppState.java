package com.kotor4j.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.export.JmeImporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.font.BitmapCharacterSet;
import com.jme3.font.BitmapFont;
import java.io.FileInputStream;

/**
 * @author Dmitry
 */
public class FontDevelopAppState extends BaseAppState{

    @Override
    protected void initialize(Application app) {
        BitmapCharacterSet bcs=new BitmapCharacterSet();
        BitmapFont font=new BitmapFont();
        font.setCharSet(bcs);
    }

    @Override
    protected void cleanup(Application app) {
        
    }

    @Override
    protected void onEnable() {
        
    }

    @Override
    protected void onDisable() {
        
    }

}
