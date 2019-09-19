package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.enums.Gender;
import com.kotor4j.enums.Race;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.CREATURE)
public class Creature extends BaseEntity {

    @FieldTag(fieldIndex = 1)
    private String title;
    @FieldTag(fieldIndex = 2)
    private String tag;
    @FieldTag(fieldIndex = 3)
    private Race race;
    @FieldTag(fieldIndex = 4)
    private String modelPath;
    @FieldTag(fieldIndex = 5)
    private Gender gender;
    @FieldTag(fieldIndex = 6)
    private String bagModelPath;
    @FieldTag(fieldIndex = 7)
    private String iconPath;
    @FieldTag(fieldIndex = 8)
    private String dialogId;
    @FieldTag(fieldIndex = 9)
    private int strength;
    @FieldTag(fieldIndex = 10)
    private int dexterity;
    @FieldTag(fieldIndex = 11)
    private int constitution;
    @FieldTag(fieldIndex = 12)
    private int intelligence;
    @FieldTag(fieldIndex = 13)
    private int wisdom;
    @FieldTag(fieldIndex = 14)
    private int charisma;
    @FieldTag(fieldIndex = 15)
    private int fortitude;
    @FieldTag(fieldIndex = 16)
    private int reflex;
    @FieldTag(fieldIndex = 17)
    private int will;
    @FieldTag(fieldIndex = 18)
    private int baseArmorClass;
    @FieldTag(fieldIndex = 19)
    private int maxHealth;
    @FieldTag(fieldIndex = 20)
    private int currentHealth;
    @FieldTag(fieldIndex = 21)
    private int maxForcePoints;
    @FieldTag(fieldIndex = 22)
    private int currentForcePoints;
    @FieldTag(fieldIndex = 23)
    private int computerUse;
    @FieldTag(fieldIndex = 24)
    private int demolitions;
    @FieldTag(fieldIndex = 25)
    private int stealth;
    @FieldTag(fieldIndex = 26)
    private int awareness;
    @FieldTag(fieldIndex = 27)
    private int persuade;
    @FieldTag(fieldIndex = 28)
    private int repair;
    @FieldTag(fieldIndex = 29)
    private int security;
    @FieldTag(fieldIndex = 30)
    private int threatInjury;
    @FieldTag(fieldIndex = 31)
    private String onHeartbeatScript;
    @FieldTag(fieldIndex = 32)
    private String onNoticeScript;
    @FieldTag(fieldIndex = 33)
    private String onSpellAtScript;
    @FieldTag(fieldIndex = 34)
    private String onAttackedScript;
    @FieldTag(fieldIndex = 35)
    private String onDamagerScript;
    @FieldTag(fieldIndex = 36)
    private String onDisturbedScript;
    @FieldTag(fieldIndex = 37)
    private String onEndRoundScript;
    @FieldTag(fieldIndex = 38)
    private String onEndDialogScript;
    @FieldTag(fieldIndex = 39)
    private String onDialogScript;
    @FieldTag(fieldIndex = 40)
    private String onSpawnScript;
    @FieldTag(fieldIndex = 41)
    private String onDeathScript;
    @FieldTag(fieldIndex = 42)
    private String onUserDefinedEventScript;
    @FieldTag(fieldIndex = 43)
    private String onPathBlockedScript;
    @FieldTag(fieldIndex = 44)
    private int alignment;
    @FieldTag(fieldIndex = 45)
    private int level;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBagModelPath() {
        return bagModelPath;
    }

    public void setBagModelPath(String bagModelPath) {
        this.bagModelPath = bagModelPath;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getDialogId() {
        return dialogId;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getFortitude() {
        return fortitude;
    }

    public void setFortitude(int fortitude) {
        this.fortitude = fortitude;
    }

    public int getReflex() {
        return reflex;
    }

    public void setReflex(int reflex) {
        this.reflex = reflex;
    }

    public int getWill() {
        return will;
    }

    public void setWill(int will) {
        this.will = will;
    }

    public int getBaseArmorClass() {
        return baseArmorClass;
    }

    public void setBaseArmorClass(int baseArmorClass) {
        this.baseArmorClass = baseArmorClass;
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

    public int getMaxForcePoints() {
        return maxForcePoints;
    }

    public void setMaxForcePoints(int maxForcePoints) {
        this.maxForcePoints = maxForcePoints;
    }

    public int getCurrentForcePoints() {
        return currentForcePoints;
    }

    public void setCurrentForcePoints(int currentForcePoints) {
        this.currentForcePoints = currentForcePoints;
    }

    public int getComputerUse() {
        return computerUse;
    }

    public void setComputerUse(int computerUse) {
        this.computerUse = computerUse;
    }

    public int getDemolitions() {
        return demolitions;
    }

    public void setDemolitions(int demolitions) {
        this.demolitions = demolitions;
    }

    public int getStealth() {
        return stealth;
    }

    public void setStealth(int stealth) {
        this.stealth = stealth;
    }

    public int getAwareness() {
        return awareness;
    }

    public void setAwareness(int awareness) {
        this.awareness = awareness;
    }

    public int getPersuade() {
        return persuade;
    }

    public void setPersuade(int persuade) {
        this.persuade = persuade;
    }

    public int getRepair() {
        return repair;
    }

    public void setRepair(int repair) {
        this.repair = repair;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }

    public int getThreatInjury() {
        return threatInjury;
    }

    public void setThreatInjury(int threatInjury) {
        this.threatInjury = threatInjury;
    }

    public String getOnHeartbeatScript() {
        return onHeartbeatScript;
    }

    public void setOnHeartbeatScript(String onHeartbeatScript) {
        this.onHeartbeatScript = onHeartbeatScript;
    }

    public String getOnNoticeScript() {
        return onNoticeScript;
    }

    public void setOnNoticeScript(String onNoticeScript) {
        this.onNoticeScript = onNoticeScript;
    }

    public String getOnSpellAtScript() {
        return onSpellAtScript;
    }

    public void setOnSpellAtScript(String onSpellAtScript) {
        this.onSpellAtScript = onSpellAtScript;
    }

    public String getOnAttackedScript() {
        return onAttackedScript;
    }

    public void setOnAttackedScript(String onAttackedScript) {
        this.onAttackedScript = onAttackedScript;
    }

    public String getOnDamagerScript() {
        return onDamagerScript;
    }

    public void setOnDamagerScript(String onDamagerScript) {
        this.onDamagerScript = onDamagerScript;
    }

    public String getOnDisturbedScript() {
        return onDisturbedScript;
    }

    public void setOnDisturbedScript(String onDisturbedScript) {
        this.onDisturbedScript = onDisturbedScript;
    }

    public String getOnEndRoundScript() {
        return onEndRoundScript;
    }

    public void setOnEndRoundScript(String onEndRoundScript) {
        this.onEndRoundScript = onEndRoundScript;
    }

    public String getOnEndDialogScript() {
        return onEndDialogScript;
    }

    public void setOnEndDialogScript(String onEndDialogScript) {
        this.onEndDialogScript = onEndDialogScript;
    }

    public String getOnDialogScript() {
        return onDialogScript;
    }

    public void setOnDialogScript(String onDialogScript) {
        this.onDialogScript = onDialogScript;
    }

    public String getOnSpawnScript() {
        return onSpawnScript;
    }

    public void setOnSpawnScript(String onSpawnScript) {
        this.onSpawnScript = onSpawnScript;
    }

    public String getOnDeathScript() {
        return onDeathScript;
    }

    public void setOnDeathScript(String onDeathScript) {
        this.onDeathScript = onDeathScript;
    }

    public String getOnUserDefinedEventScript() {
        return onUserDefinedEventScript;
    }

    public void setOnUserDefinedEventScript(String onUserDefinedEventScript) {
        this.onUserDefinedEventScript = onUserDefinedEventScript;
    }

    public String getOnPathBlockedScript() {
        return onPathBlockedScript;
    }

    public void setOnPathBlockedScript(String onPathBlockedScript) {
        this.onPathBlockedScript = onPathBlockedScript;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
