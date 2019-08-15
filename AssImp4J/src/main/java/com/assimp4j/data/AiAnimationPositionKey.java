package com.assimp4j.data;

/**
 * @author Dmitry
 */
public class AiAnimationPositionKey {

    private float[] vector;
    private double time;

    public void setTime(double time) {
        this.time = time;
    }

    public void setVector(float[] vector) {
        this.vector = vector;
    }

    public double getTime() {
        return time;
    }

    public float[] getVector() {
        return vector;
    }
}
