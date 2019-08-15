package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.PLACEABLE)
public class Placeable extends BaseEntity {

    @FieldTag(fieldIndex = 1)
    private String title;
    @FieldTag(fieldIndex = 2)
    private String animation;
    @FieldTag(fieldIndex = 3)
    private String modelPath;
    @FieldTag(fieldIndex = 4)
    private boolean isStatic;
    @FieldTag(fieldIndex = 5)
    private String dialogId;
    @FieldTag(fieldIndex = 6)
    private int lockId;
    @FieldTag(fieldIndex = 7)
    private String tag;
    @FieldTag(fieldIndex = 8)
    private String onActivateScript;
    @FieldTag(fieldIndex = 9)
    private String onClosedScript;
    @FieldTag(fieldIndex = 10)
    private String onDeathScript;
    @FieldTag(fieldIndex = 11)
    private String onHeartbeatScript;
    @FieldTag(fieldIndex = 12)
    private String onAttackedScript;
    @FieldTag(fieldIndex = 13)
    private String onOpenScript;
    @FieldTag(fieldIndex = 14)
    private String onSpellAtScript;
    @FieldTag(fieldIndex = 15)
    private String onUserDefinedEvent;
    @FieldTag(fieldIndex = 16)
    private String trapId;
    @FieldTag(fieldIndex = 17)
    private int maxHealth;
    @FieldTag(fieldIndex = 18)
    private int currentHealth;
    @FieldTag(fieldIndex = 19)
    private int minHealth;
    @FieldTag(fieldIndex = 20)
    private boolean dialogInteruptible;
    @FieldTag(fieldIndex = 21)
    private String description;

    public String isDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    
    public boolean isDialogInteruptible() {
        return dialogInteruptible;
    }

    public void setDialogInteruptible(boolean dialogInteruptible) {
        this.dialogInteruptible = dialogInteruptible;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public boolean isIsStatic() {
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

    public int getLockId() {
        return lockId;
    }

    public void setLockId(int lockId) {
        this.lockId = lockId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOnActivateScript() {
        return onActivateScript;
    }

    public void setOnActivateScript(String onActivateScript) {
        this.onActivateScript = onActivateScript;
    }

    public String getOnClosedScript() {
        return onClosedScript;
    }

    public void setOnClosedScript(String onClosedScript) {
        this.onClosedScript = onClosedScript;
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

    public String getOnAttackedScript() {
        return onAttackedScript;
    }

    public void setOnAttackedScript(String onAttackedScript) {
        this.onAttackedScript = onAttackedScript;
    }

    public String getOnOpenScript() {
        return onOpenScript;
    }

    public void setOnOpenScript(String onOpenScript) {
        this.onOpenScript = onOpenScript;
    }

    public String getOnSpellAtScript() {
        return onSpellAtScript;
    }

    public void setOnSpellAtScript(String onSpellAtScript) {
        this.onSpellAtScript = onSpellAtScript;
    }

    public String getOnUserDefinedEvent() {
        return onUserDefinedEvent;
    }

    public void setOnUserDefinedEvent(String onUserDefinedEvent) {
        this.onUserDefinedEvent = onUserDefinedEvent;
    }

    public String getTrapId() {
        return trapId;
    }

    public void setTrapId(String trapId) {
        this.trapId = trapId;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMinHealth() {
        return minHealth;
    }

    public void setMinHealth(int minHealth) {
        this.minHealth = minHealth;
    }

}
