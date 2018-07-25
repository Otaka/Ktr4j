package com.kotor4j.resourcemanager.ssf;

import com.kotor4j.resourcemanager.dialog.StringEntry;

/**
 * @author Dmitry
 */
public class SsfEntry {
    private String effectName;
    private int stringRef;
    private StringEntry voiceStringEntry;

    public SsfEntry(String effectName, int stringRef, StringEntry voiceStringEntry) {
        this.effectName = effectName;
        this.stringRef = stringRef;
        this.voiceStringEntry = voiceStringEntry;
    }

    public String getEffectName() {
        return effectName;
    }

    public int getStringRef() {
        return stringRef;
    }

    public StringEntry getVoiceStringEntry() {
        return voiceStringEntry;
    }
    
    
}
