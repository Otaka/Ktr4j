package com.kotor4j.kotorgui.components;

import com.jme3.scene.Spatial;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.kotor4j.kotorgui.GuiManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class PictureComponent extends AbstractComponent {

    private Picture picture;
    private float width;
    private float height;

    public PictureComponent(GuiManager guiManager, String texturePath, float width, float height) {
        this(guiManager, (Texture2D) guiManager.getApplication().getAssetManager().loadTexture(texturePath), width, height);
    }

    public PictureComponent(GuiManager guiManager, Texture2D texture, float width, float height) {
        super(guiManager);

        picture = new Picture("Picture component");
        /*Material mat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md") {
            @Override
            public void render(Geometry geometry, LightList lights, RenderManager rm) {
                AbstractComponent par = PictureComponent.this.getParent();

                if (par != null) {
                    int w = (int) par.getWidth();
                    int h = (int) par.getHeight();
                    Vector3f worldPosition = par.getComponentNode().getWorldTranslation();
                    rm.getRenderer().setClipRect((int) worldPosition.getX(), (int) worldPosition.getY(), w, h);
                }
                super.render(geometry, lights, rm);
                if (par != null) {
                    rm.getRenderer().setClipRect(0, 0, guiManager.getScreenWidth(), guiManager.getScreenHeight());
                }
            }
        };*/
        //mat.setColor("Color", ColorRGBA.White);
        //picture.setMaterial(mat);
        this.width = width;
        this.height = height;
        picture.setWidth(width);
        picture.setHeight(height);
        picture.setTexture(assetManager, texture, true);
        picture.setPosition(0, 0);
        getComponentNode().attachChild(picture);
        
        List<Spatial> guiChildren = new ArrayList<>();
        guiChildren.add(picture);
        getComponentNode().setUserData("guiChildren", guiChildren);
    }

    @Override
    public void onPaddingsChanged() {
        picture.setPosition(getClientX(), getClientY());
        picture.setWidth(getClientWidth());
        picture.setHeight(getClientHeight());
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setWidth(float width) {
        this.width = width;
        picture.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        picture.setHeight(height);
    }
}
