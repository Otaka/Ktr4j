package com.assimp4j.data;

/**
 * @author Dmitry
 */
public class AiAnimationRotationKey {

    private float[] quaternion;
    private double time;

    public void setTime(double time) {
        this.time = time;
    }

    public void setQuaternion(float[] quaternion) {
        this.quaternion = quaternion;
    }

    public double getTime() {
        return time;
    }

    public float[] getQuaternion() {
        return quaternion;
    }
}
