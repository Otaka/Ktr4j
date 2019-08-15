package com.assimp4j.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry
 */
public class AiNode {

    private float[] position;
    private float[] quaternion;
    private float[] scale;

    private String name;
    private AiNode parent;
    private AiNode[] children;
    private int[] meshIndexes;
    private Map<String, Object> metadata = new HashMap<>();

    public float[] getPosition() {
        return position;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public float[] getQuaternion() {
        return quaternion;
    }

    public void setQuaternion(float[] quaternion) {
        this.quaternion = quaternion;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float[] scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AiNode getParent() {
        return parent;
    }

    public void setParent(AiNode parent) {
        this.parent = parent;
    }

    public AiNode[] getChildren() {
        return children;
    }

    public void setChildren(AiNode[] children) {
        this.children = children;
    }

    public int[] getMeshIndexes() {
        return meshIndexes;
    }

    public void setMeshIndexes(int[] meshIndexes) {
        this.meshIndexes = meshIndexes;
    }

    public void addMetadataBoolKey(String key, boolean value) {
        metadata.put(key, value);
    }

    public void addMetadataIntKey(String key, int value) {
        metadata.put(key, value);
    }

    public void addMetadataLongKey(String key, long value) {
        metadata.put(key, value);
    }

    public void addMetadataFloatKey(String key, float value) {
        metadata.put(key, value);
    }

    public void addMetadataDoubleKey(String key, double value) {
        metadata.put(key, value);
    }

    public void addMetadataStringKey(String key, String value) {
        metadata.put(key, value);
    }

    public void addMetadataVectorKey(String key, float[] value) {
        metadata.put(key, new AiVector3D(value));
    }

    @Override
    public String toString() {
        return getName();
    }
}
