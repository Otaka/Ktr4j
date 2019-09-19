package com.kotor4j.kotorconverter.original.dialog;

/**
 * @author sad
 */
public class StringEntry {
    private int id;
    private String string;
    private String voiceResRef;
    private float soundLength;
    private int flag;

    public StringEntry(int id, String string, String voiceResRef, float soundLength, int flag) {
        this.id = id;
        this.string = string;
        this.voiceResRef = voiceResRef;
        this.soundLength = soundLength;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public String getString() {
        return string;
    }

    public String getVoiceResRef() {
        return voiceResRef;
    }

    public float getSoundLength() {
        return soundLength;
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public String toString() {
        return string;
    }
}