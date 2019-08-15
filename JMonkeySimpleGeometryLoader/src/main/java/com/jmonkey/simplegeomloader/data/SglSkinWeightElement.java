package com.jmonkey.simplegeomloader.data;

/**
 *
 * @author Dmitry
 */
public class SglSkinWeightElement {

    private final String boneName;
    private final float weight;

    public SglSkinWeightElement(String boneName, float weight) {
        this.boneName = boneName;
        this.weight = weight;
    }

    public String getBoneName() {
        return boneName;
    }

    public float getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return boneName + ":" + weight;
    }
}
