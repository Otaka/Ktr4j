package com.kotor4j.enums;

/**
 * @author Dmitry
 */
public enum NotVisibleWearableType {
    Armor(0), Implant(1), Mask(2), Gloves(3), Belt(4), Shield(5);

    private int id;

    private NotVisibleWearableType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
