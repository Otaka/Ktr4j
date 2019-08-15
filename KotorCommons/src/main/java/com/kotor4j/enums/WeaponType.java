package com.kotor4j.enums;

/**
 * @author Dmitry
 */
public enum WeaponType{
    Pistol(0),Riffle(1),Dagger(2), Blade(3),Lightsaber(4),TwoHandedLightsaber(5),Staff(6);
    
    private int id;

    private WeaponType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
}
