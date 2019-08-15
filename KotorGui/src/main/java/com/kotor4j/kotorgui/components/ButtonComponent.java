package com.kotor4j.kotorgui.components;

import com.jme3.font.BitmapText;
import com.jme3.scene.Spatial;
import com.kotor4j.kotorgui.GuiManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class ButtonComponent extends AbstractComponent {

    private BitmapText bitmapText;
    private float width;
    private float height;

    public ButtonComponent(GuiManager guiManager, int width, int height) {
        super(guiManager);
        bitmapText = new BitmapText(assetManager.loadFont("Interface/Fonts/Default.fnt"));
        /*{
            @Override
            public void render(RenderManager rm, ColorRGBA color) {
                AbstractComponent par = ButtonComponent.this.getParent();

                if (par != null) {
                    int w = (int) par.getWidth();
                    int h = (int) par.getHeight();
                    Vector3f worldPosition = par.getComponentNode().getWorldTranslation();
                    rm.getRenderer().setClipRect((int) worldPosition.getX(), (int) worldPosition.getY(), w, h);
                }
                super.render(rm, color);
                if (par != null) {
                    rm.getRenderer().setClipRect(0, 0, guiManager.getScreenWidth(), guiManager.getScreenHeight());
                }
            }
            
        };
        /*Material mat = new Material(assetManager, "Common/MatDefs/Gui/Gui.j3md") {
            @Override
            public void render(Geometry geometry, LightList lights, RenderManager rm) {
                AbstractComponent par = ButtonComponent.this.getParent();

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
        // mat.setColor("Color", ColorRGBA.White);
        //bitmapText.setMaterial(mat);
        this.width = width;
        this.height = height;

        bitmapText.setLocalTranslation(0, 0, 0);
        getComponentNode().attachChild(bitmapText);

        List<Spatial> guiChildren = new ArrayList<>();
        guiChildren.add(bitmapText);
        getComponentNode().setUserData("guiChildren", guiChildren);
    }

    public ButtonComponent setText(String text) {
        bitmapText.setText(text);
        return this;
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
        // bitmapText.setWidth(width);
    }

    @Override
    public void setHeight(float height) {
        this.height = height;
        //  bitmapText.setHeight(height);
    }

    @Override
    public void onPaddingsChanged() {
        bitmapText.setLocalTranslation(getClientX(), getClientY(), 0);
        //picture.setWidth(getClientWidth());
        //picture.setHeight(getClientHeight());
    }
}
