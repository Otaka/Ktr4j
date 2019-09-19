package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.REFERENCE)
public class ReferencedEntity {

    public static final int STATIC_OBJECTS = 10;
    public static final int WAYPOINT_OBJECTS = 20;
    public static final int TRIGGERS_OBJECTS = 30;
    public static final int PLACEABLES_OBJECTS = 40;
    @FieldTag(fieldIndex = 1)
    private String referenceId;
    @FieldTag(fieldIndex = 2)
    private String entityId;
    @FieldTag(fieldIndex = 3)
    private String destEntityId;

    private int destSubreferenceType;

    public ReferencedEntity(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getDestEntityId() {
        return destEntityId;
    }

    public void setDestEntityId(String destEntityId) {
        this.destEntityId = destEntityId;
    }

    public int getDestSubreferenceType() {
        return destSubreferenceType;
    }

    public void setDestSubreferenceType(int destSubreferenceType) {
        this.destSubreferenceType = destSubreferenceType;
    }

}
