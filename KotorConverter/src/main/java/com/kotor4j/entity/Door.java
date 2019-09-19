package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.DOOR)
public class Door extends BaseEntity {
    @FieldTag(fieldIndex = 1)
    private String name;
    @FieldTag(fieldIndex = 2)
    private String modelPath;
    @FieldTag(fieldIndex = 3)
    private boolean isStatic;
    @FieldTag(fieldIndex = 4)
    private String dialogId;
    @FieldTag(fieldIndex = 5)
    private int lockDc;
    @FieldTag(fieldIndex = 6)
    private String tag;
    @FieldTag(fieldIndex = 7)
    private String onDeathScript;
    @FieldTag(fieldIndex = 8)
    private String onHeartbeatScript;
    @FieldTag(fieldIndex = 9)
    private String onOpenScript;
    @FieldTag(fieldIndex = 10)
    private String onSpellcastAtScript;
    @FieldTag(fieldIndex = 11)
    private String onUserDefinedEventScript;
    @FieldTag(fieldIndex = 12)
    private String onFailToOpenScript;
    @FieldTag(fieldIndex = 13)
    private String onClickScript;
    @FieldTag(fieldIndex = 14)
    private int maxHealth;
    @FieldTag(fieldIndex = 15)
    private int currentScript;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public int getLockDc() {
        return lockDc;
    }

    public void setLockDc(int lockDc) {
        this.lockDc = lockDc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOnDeathScript() {
        return onDeathScript;
    }

    public void setOnDeathScript(String onDeathScript) {
        this.onDeathScript = onDeathScript;
    }

    public String getOnHeartbeatScript() {
        return onHeartbeatScript;
    }

    public void setOnHeartbeatScript(String onHeartbeatScript) {
        this.onHeartbeatScript = onHeartbeatScript;
    }

    public String getOnOpenScript() {
        return onOpenScript;
    }

    public void setOnOpenScript(String onOpenScript) {
        this.onOpenScript = onOpenScript;
    }

    public String getOnSpellcastAtScript() {
        return onSpellcastAtScript;
    }

    public void setOnSpellcastAtScript(String onSpellcastAtScript) {
        this.onSpellcastAtScript = onSpellcastAtScript;
    }

    public String getOnUserDefinedEventScript() {
        return onUserDefinedEventScript;
    }

    public void setOnUserDefinedEventScript(String onUserDefinedEventScript) {
        this.onUserDefinedEventScript = onUserDefinedEventScript;
    }

    public String getOnFailToOpenScript() {
        return onFailToOpenScript;
    }

    public void setOnFailToOpenScript(String onFailToOpenScript) {
        this.onFailToOpenScript = onFailToOpenScript;
    }

    public String getOnClickScript() {
        return onClickScript;
    }

    public void setOnClickScript(String onClickScript) {
        this.onClickScript = onClickScript;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentScript() {
        return currentScript;
    }

    public void setCurrentScript(int currentScript) {
        this.currentScript = currentScript;
    }

}
