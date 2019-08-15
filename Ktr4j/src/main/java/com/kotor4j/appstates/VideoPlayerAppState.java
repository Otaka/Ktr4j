package com.kotor4j.appstates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

/**
 * @author Dmitry
 */
public class VideoPlayerAppState extends BaseAppState {

    private Geometry geom;
    private SimpleApplication app;

    @Override
    protected void initialize(Application app) {
        Box box = new Box(1, 1, 1);
        geom = new Geometry("mybox", box);
        Material mat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);
        this.app = (SimpleApplication) app;
    }

    @Override
    protected void cleanup(Application app) {
        System.out.println("Video Player state cleanup");
    }

    @Override
    protected void onEnable() {
        System.out.println("Enabled");
        app.getRootNode().attachChild(geom);
        geom.setLocalTranslation(1, 3, 1);
    }

    @Override
    protected void onDisable() {
        System.out.println("Disabled");
        app.getRootNode().detachChild(geom);
    }

    @Override
    public void update(float tpf) {

    }
}