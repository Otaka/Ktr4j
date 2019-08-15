package com.kotor4j.nodes;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;

/**
 * @author Dmitry
 */
public class PictureNode extends Node{
    private Picture picture;
    private float height;
    
    public PictureNode(String name) {
        super(name);
        picture=new Picture("PicNode");
        picture.setPosition(0, 0);
        attachChild(picture);
    }

    public float getHeight(){
        return height;
    }
    
    public void setWidth(float width) {
        picture.setWidth(width);
    }

    public void setHeight(float height) {
        picture.setHeight(height);
        this.height=height;
    }

    public void setTexture(AssetManager assetManager, Texture2D tex, boolean useAlpha) {
        picture.setTexture(assetManager, tex, useAlpha);
    }
    
    public void setPosition(float x, float y,float z){
        setLocalTranslation(x, y, z);
    }
    
    public void setInnerPicturePosition(int x, int y){
        picture.setPosition(x, y);
    }
}
