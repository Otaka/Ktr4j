package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.enums.WeaponType;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.WEAPON)
public class Weapon extends BaseItem {

    @FieldTag(fieldIndex = 20)
    private String modelPath;
    @FieldTag(fieldIndex = 21)
    private WeaponType weaponType;
    @FieldTag(fieldIndex = 22)
    private int numDice;
    @FieldTag(fieldIndex = 23)
    private int maxDie;
    @FieldTag(fieldIndex = 24)
    private boolean canUpgrade;
    @FieldTag(fieldIndex = 25)
    private int range;

    public Weapon() {

    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public int getNumDice() {
        return numDice;
    }

    public void setNumDice(int numDice) {
        this.numDice = numDice;
    }

    public int getMaxDie() {
        return maxDie;
    }

    public void setMaxDie(int maxDie) {
        this.maxDie = maxDie;
    }

    public boolean isCanUpgrade() {
        return canUpgrade;
    }

    public void setCanUpgrade(boolean canUpgrade) {
        this.canUpgrade = canUpgrade;
    }

    @Override
    public String toString() {
        return "[" + getWeaponType() + "] " + getTitle();
    }
}
