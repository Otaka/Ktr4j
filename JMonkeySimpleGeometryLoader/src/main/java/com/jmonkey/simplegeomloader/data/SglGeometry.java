package com.jmonkey.simplegeomloader.data;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

/**
 *
 * @author Dmitry
 */
public class SglGeometry extends SglNode {

    private boolean skin;
    private boolean visible;
    private String texture;
    private FloatBuffer vertices;
    private FloatBuffer normals;
    private IntBuffer indices;
    private FloatBuffer textureCoords;
    private float[] wireColor;
    private float[] specularColor;
    private float shininess;
    private List<SglSkinWeightElement[]> vertexWeights;
    private boolean wireframe;

    public void setWireframe(boolean wireframe) {
        this.wireframe = wireframe;
    }

    public boolean isWireframe() {
        return wireframe;
    }
    
    

    public void setVertexWeights(List<SglSkinWeightElement[]> vertexWeights) {
        this.vertexWeights = vertexWeights;
    }

    public List<SglSkinWeightElement[]> getVertexWeights() {
        return vertexWeights;
    }

    public float getShininess() {
        return shininess;
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public float[] getWireColor() {
        return wireColor;
    }

    public void setWireColor(float[] wireColor) {
        this.wireColor = wireColor;
    }

    public float[] getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(float[] specularColor) {
        this.specularColor = specularColor;
    }

    public void setSkin(boolean skin) {
        this.skin = skin;
    }

    public boolean isSkin() {
        return skin;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setTexture(String texture) {
        this.texture = texture;
    }

    public String getTexture() {
        return texture;
    }

    public IntBuffer getIndices() {
        return indices;
    }

    public FloatBuffer getNormals() {
        return normals;
    }

    public FloatBuffer getTextureCoords() {
        return textureCoords;
    }

    public FloatBuffer getVertices() {
        return vertices;
    }

    public void setIndices(IntBuffer indices) {
        this.indices = indices;
    }

    public void setNormals(FloatBuffer normals) {
        this.normals = normals;
    }

    public void setTextureCoords(FloatBuffer textureCoords) {
        this.textureCoords = textureCoords;
    }

    public void setVertices(FloatBuffer vertices) {
        this.vertices = vertices;
    }

    @Override
    public boolean hasGeometry() {
        return true;
    }
}
