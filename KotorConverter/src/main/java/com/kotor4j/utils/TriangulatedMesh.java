package com.kotor4j.utils;

/**
 * @author Dmitry
 */
public class TriangulatedMesh {

    private float[] data;
    private int[] indicies;

    public TriangulatedMesh(float[] data, int[] indicies) {
        this.data = data;
        this.indicies = indicies;
    }

    public float[] getData() {
        return data;
    }

    public int[] getIndicies() {
        return indicies;
    }

}
