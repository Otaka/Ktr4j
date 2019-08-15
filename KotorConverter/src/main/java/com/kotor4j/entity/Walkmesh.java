package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.WALKMESH)
public class Walkmesh {
    @FieldTag(fieldIndex = 10)
    private float[] points;
    @FieldTag(fieldIndex = 11)
    private int[] indicies;

    public Walkmesh(float[] points, int[] indicies) {
        this.points = points;
        this.indicies = indicies;
    }

    public float[] getPoints() {
        return points;
    }

    public int[] getIndicies() {
        return indicies;
    }

}
