package com.jmonkey.simplegeomloader.data;

import com.jme3.math.Quaternion;

/**
 *
 * @author Dmitry
 */
public class SglAnimationRotation {

    private final float time;
    private final Quaternion rotation;

    public SglAnimationRotation(float time,Quaternion rotation) {
        this.time = time;
        this.rotation = rotation;
    }

    public Quaternion getRotation() {
        return rotation;
    }

    public float getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Time: "+time+". Rotation: "+rotation;
    }
}
