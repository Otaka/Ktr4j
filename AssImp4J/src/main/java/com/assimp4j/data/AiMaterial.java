package com.assimp4j.data;

/**
 * @author Dmitry
 */
public class AiMaterial {

    private String name;
    private String texture;
    private boolean isWireframe;
    private boolean twoSided;
    private float opacity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTexture() {
        return texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public boolean isIsWireframe() {
        return isWireframe;
    }

    public void setIsWireframe(boolean isWireframe) {
        this.isWireframe = isWireframe;
    }

    public boolean isTwoSided() {
        return twoSided;
    }

    public void setTwoSided(boolean twoSided) {
        this.twoSided = twoSided;
    }

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    @Override
    public String toString() {
        return ""+getName()+" ["+getTexture()+"]";
    }
    
}
