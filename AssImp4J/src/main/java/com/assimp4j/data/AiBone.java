package com.assimp4j.data;

/**
 * @author Dmitry
 */
public class AiBone {

    private String name;
    private float[] offsetMatrix;
    private int[] vertexWeights_VertexIndexArray;
    private int[] vertexWeights_VertexWeightArray;

    public void setVertexWeights_VertexIndexArray(int[] vertexWeights_VertexIndexArray) {
        this.vertexWeights_VertexIndexArray = vertexWeights_VertexIndexArray;
    }

    public void setVertexWeights_VertexWeightArray(int[] vertexWeights_VertexWeightArray) {
        this.vertexWeights_VertexWeightArray = vertexWeights_VertexWeightArray;
    }

    public String getName() {
        return name;
    }

    public float[] getOffsetMatrix() {
        return offsetMatrix;
    }

    public int[] getVertexWeights_VertexIndexArray() {
        return vertexWeights_VertexIndexArray;
    }

    public int[] getVertexWeights_VertexWeightArray() {
        return vertexWeights_VertexWeightArray;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOffsetMatrix(float[] offsetMatrix) {
        this.offsetMatrix = offsetMatrix;
    }

    @Override
    public String toString() {
        return getName();
    }
    
}
