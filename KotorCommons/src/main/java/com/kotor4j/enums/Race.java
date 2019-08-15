package com.kotor4j.enums;

/**
 * @author Dmitry
 */
public enum Race {

    Humanoid(0), Droid(1);

    private int id;

    private Race(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
