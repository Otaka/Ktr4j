package com.jmonkey.simplegeomloader.data;

import java.util.Arrays;

/**
 *
 * @author Dmitry
 */
public class SglAnimationPosition {

    private final float time;
    private final float[] positions;

    public SglAnimationPosition(float time, float[] position) {
        this.time = time;
        this.positions = position;
    }

    public float[] getPositions() {
        return positions;
    }

    public float getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Time: "+time;
    }
}
