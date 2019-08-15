package com.kotor4j.materials;

import com.jme3.asset.AssetManager;
import com.jme3.light.LightList;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;

/**
 * @author Dmitry
 */
public class MaterialWithClipping extends Material {
    int x=0;
    int y=0;
    int width = 8000;
    int height = 8000;

    public MaterialWithClipping(MaterialDef def) {
        super(def);
    }

    public MaterialWithClipping(AssetManager contentMan, String defName) {
        super(contentMan, defName);
    }

    
    
    @Override
    public void render(Geometry geometry, LightList lights, RenderManager renderManager) {
        renderManager.getRenderer().setClipRect(x, y, width,height);
        super.render(geometry, lights, renderManager);
        renderManager.getRenderer().clearClipRect();
    }
    
    public void setClipping(int x, int y, int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
}
