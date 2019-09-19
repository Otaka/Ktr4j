package com.kotor4j.kotorconverter.original.walkmesh;

/**
 * @author Dmitry
 */
public class WalkmeshAABBBox {

    private float[] minVertex;
    private float[] maxVertex;
    private int leftFacePartNumber;
    private int LeftNodeArrayIndex;
    private int RightNodeArrayIndex;
    private int Always4;
    private int MostSignifPlane;

    public WalkmeshAABBBox(float[] minVertex, float[] maxVertex, int leftFacePartNumber, int LeftNodeArrayIndex, int RightNodeArrayIndex, int Always4, int MostSignifPlane) {
        this.minVertex = minVertex;
        this.maxVertex = maxVertex;
        this.leftFacePartNumber = leftFacePartNumber;
        this.LeftNodeArrayIndex = LeftNodeArrayIndex;
        this.RightNodeArrayIndex = RightNodeArrayIndex;
        this.Always4 = Always4;
        this.MostSignifPlane = MostSignifPlane;
    }

    public float[] getMinVertex() {
        return minVertex;
    }

    public float[] getMaxVertex() {
        return maxVertex;
    }

    public int getLeftFacePartNumber() {
        return leftFacePartNumber;
    }

    public int getLeftNodeArrayIndex() {
        return LeftNodeArrayIndex;
    }

    public int getRightNodeArrayIndex() {
        return RightNodeArrayIndex;
    }

    public int getAlways4() {
        return Always4;
    }

    public int getMostSignifPlane() {
        return MostSignifPlane;
    }

}
