package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.WAYPOINT)
public class Waypoint extends BaseEntity {
    @FieldTag(fieldIndex = 5)
    private float x;
    @FieldTag(fieldIndex = 6)
    private float y;
    @FieldTag(fieldIndex = 7)
    private float z;
    @FieldTag(fieldIndex = 8)
    private float xOrientation;
    @FieldTag(fieldIndex = 9)
    private float yOrientation;

    @FieldTag(fieldIndex = 10)
    private String description;
    @FieldTag(fieldIndex = 11)
    private String mapNote;
    @FieldTag(fieldIndex = 12)
    private boolean mapNoteEnabled;
    @FieldTag(fieldIndex = 13)
    private String tag;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMapNote() {
        return mapNote;
    }

    public void setMapNote(String mapNote) {
        this.mapNote = mapNote;
    }

    public boolean isMapNoteEnabled() {
        return mapNoteEnabled;
    }

    public void setMapNoteEnabled(boolean mapNoteEnabled) {
        this.mapNoteEnabled = mapNoteEnabled;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
