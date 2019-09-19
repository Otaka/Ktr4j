package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.POSITIONAL_REFERENCE)
public class PositionalReferencedEntity extends ReferencedEntity {
    @FieldTag(fieldIndex = 10)
    private float[]vertices;
    @FieldTag(fieldIndex = 11)
    private float x;
    @FieldTag(fieldIndex = 12)
    private float y;
    @FieldTag(fieldIndex = 13)
    private float z;
    @FieldTag(fieldIndex = 14)
    private float qx;
    @FieldTag(fieldIndex = 15)
    private float qy;
    @FieldTag(fieldIndex = 16)
    private float qz;
    @FieldTag(fieldIndex = 17)
    private float qw;

    public PositionalReferencedEntity(String referenceId) {
        super(referenceId);
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setQuaternionRotation(float qx, float qy, float qz, float qw) {
        this.qx = qx;
        this.qy = qy;
        this.qz = qz;
        this.qw = qw;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getQx() {
        return qx;
    }

    public void setQx(float qx) {
        this.qx = qx;
    }

    public float getQy() {
        return qy;
    }

    public void setQy(float qy) {
        this.qy = qy;
    }

    public float getQz() {
        return qz;
    }

    public void setQz(float qz) {
        this.qz = qz;
    }

    public float getQw() {
        return qw;
    }

    public void setQw(float qw) {
        this.qw = qw;
    }

}
