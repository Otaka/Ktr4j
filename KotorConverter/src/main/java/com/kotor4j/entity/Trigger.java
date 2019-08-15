package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.TRIGGER)
public class Trigger extends BaseEntity {

    @FieldTag(fieldIndex = 10)
    private float x;
    @FieldTag(fieldIndex = 11)
    private float y;
    @FieldTag(fieldIndex = 12)
    private float z;
    @FieldTag(fieldIndex = 13)
    private float xOrientation;
    @FieldTag(fieldIndex = 14)
    private float yOrientation;
    @FieldTag(fieldIndex = 15)
    private float zOrientation;
    @FieldTag(fieldIndex = 16)
    private String scriptHeartbeatRefId;
    @FieldTag(fieldIndex = 17)
    private String scriptOnEnterRefId;
    @FieldTag(fieldIndex = 18)
    private String scriptOnExitRefId;
    @FieldTag(fieldIndex = 19)
    private float[] vertices;
    @FieldTag(fieldIndex = 20)
    private int[] indicies;

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    public void setIndicies(int[] indicies) {
        this.indicies = indicies;
    }

    public int[] getIndicies() {
        return indicies;
    }

    public float[] getVertices() {
        return vertices;
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

    public float getxOrientation() {
        return xOrientation;
    }

    public void setxOrientation(float xOrientation) {
        this.xOrientation = xOrientation;
    }

    public float getyOrientation() {
        return yOrientation;
    }

    public void setyOrientation(float yOrientation) {
        this.yOrientation = yOrientation;
    }

    public void setzOrientation(float zOrientation) {
        this.zOrientation = zOrientation;
    }

    public float getzOrientation() {
        return zOrientation;
    }

    public String getScriptHeartbeatRefId() {
        return scriptHeartbeatRefId;
    }

    public void setScriptHeartbeatRefId(String scriptHeartbeatRefId) {
        this.scriptHeartbeatRefId = scriptHeartbeatRefId;
    }

    public String getScriptOnEnterRefId() {
        return scriptOnEnterRefId;
    }

    public void setScriptOnEnterRefId(String scriptOnEnterRefId) {
        this.scriptOnEnterRefId = scriptOnEnterRefId;
    }

    public String getScriptOnExitRefId() {
        return scriptOnExitRefId;
    }

    public void setScriptOnExitRefId(String scriptOnExitRefId) {
        this.scriptOnExitRefId = scriptOnExitRefId;
    }

}
