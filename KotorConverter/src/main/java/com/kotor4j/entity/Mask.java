package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.MASK)
public class Mask extends BaseItem {

    @FieldTag(fieldIndex = 20)
    private String modelPath;
    @FieldTag(fieldIndex = 22)
    private String modelTexture;
    @FieldTag(fieldIndex = 25)
    private int armorClass;
    @FieldTag(fieldIndex = 26)
    private boolean canUpgrade;

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getModelTexture() {
        return modelTexture;
    }

    public void setModelTexture(String modelTexture) {
        this.modelTexture = modelTexture;
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
