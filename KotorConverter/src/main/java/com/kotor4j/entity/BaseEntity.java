package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;

/**
 * @author Dmitry
 */
public abstract class BaseEntity {
    @FieldTag(fieldIndex = 0)
    private String entityId;

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
    
}
