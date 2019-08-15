package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.ARMOR)
public class Armor extends BaseItem {

    @FieldTag(fieldIndex = 20)
    private String maleModelPath;
    @FieldTag(fieldIndex = 21)
    private String femaleModelPath;
    @FieldTag(fieldIndex = 22)
    private String maleModelTexture;
    @FieldTag(fieldIndex = 23)
    private String femaleModelTexture;
    @FieldTag(fieldIndex = 25)
    private int armorClass;
    @FieldTag(fieldIndex = 26)
    private boolean canUpgrade;

    public String getMaleModelTexture() {
        return maleModelTexture;
    }

    public String getFemaleModelTexture() {
        return femaleModelTexture;
    }

    public void setFemaleModelTexture(String femaleModelTexture) {
        this.femaleModelTexture = femaleModelTexture;
    }

    public void setMaleModelTexture(String maleModelTexture) {
        this.maleModelTexture = maleModelTexture;
    }

    public String getMaleModelPath() {
        return maleModelPath;
    }

    public void setMaleModelPath(String maleModelPath) {
        this.maleModelPath = maleModelPath;
    }

    public void setFemaleModelPath(String femaleModelPath) {
        this.femaleModelPath = femaleModelPath;
    }

    public String getFemaleModelPath() {
        return femaleModelPath;
    }

    public int getArmorClass() {
        return armorClass;
    }

    public void setArmorClass(int armorClass) {
        this.armorClass = armorClass;
    }

    public boolean isCanUpgrade() {
        return canUpgrade;
    }

    public void setCanUpgrade(boolean canUpgrade) {
        this.canUpgrade = canUpgrade;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
