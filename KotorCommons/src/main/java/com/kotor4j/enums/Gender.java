package com.kotor4j.enums;

/**
 * @author Dmitry
 */
public enum Gender {
    Male(0), Female(1);

    private int id;

    private Gender(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
