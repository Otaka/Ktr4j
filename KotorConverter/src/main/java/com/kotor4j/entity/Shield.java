package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.enums.NotVisibleWearableType;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.SHIELD)
public class Shield extends BaseItem {

    @FieldTag(fieldIndex = 20)
    private NotVisibleWearableType wearableType;
    @FieldTag(fieldIndex = 21)
    private int maxUses;
    @FieldTag(fieldIndex = 22)
    private int numberOfUsing;

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int getNumberOfUsing() {
        return numberOfUsing;
    }

    public void setNumberOfUsing(int numberOfUsing) {
        this.numberOfUsing = numberOfUsing;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setWearableType(NotVisibleWearableType wearableType) {
        this.wearableType = wearableType;
    }

    public NotVisibleWearableType getWearableType() {
        return wearableType;
    }
}
