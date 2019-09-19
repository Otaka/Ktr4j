package com.kotor4j.kotorconverter.original.ssf;

import com.kotor4j.io.NwnByteArrayInputStream;
import com.kotor4j.kotorconverter.original.dialog.StringEntry;
import com.kotor4j.kotorconverter.original.dialog.Tlk;

import java.io.IOException;

/**
 * @author Dmitry
 */
public class FileReaderSsf {

    public static final String BATTLECRY_1 = "Battlecry 1";
    public static final String BATTLECRY_2 = "Battlecry 2";
    public static final String BATTLECRY_3 = "Battlecry 3";
    public static final String BATTLECRY_4 = "Battlecry 4";
    public static final String BATTLECRY_5 = "Battlecry 5";
    public static final String BATTLECRY_6 = "Battlecry 6";
    public static final String SELECT_1 = "Select 1";
    public static final String SELECT_2 = "Select 2";
    public static final String SELECT_3 = "Select 3";
    public static final String ATTACK_GRUNT_1 = "Attack Grunt 1";
    public static final String ATTACK_GRUNT_2 = "Attack Grunt 2";
    public static final String ATTACK_GRUNT_3 = "Attack Grunt 3";
    public static final String PAIN_GRUNT_1 = "Pain Grunt 1";
    public static final String PAIN_GRUNT_2 = "Pain Grunt 2";
    public static final String LOW_HEALTH = "Low Health";
    public static final String DEAD = "Dead";
    public static final String CRITICAL_HIT = "Critical Hit";
    public static final String TARGET_IMMUNE_TO_ASSAULT = "Target Immune To Assault";
    public static final String LAY_MINE = "Lay Mine";
    public static final String DISARM_MINE = "Disarm Mine";
    public static final String BEGIN_STEALTH = "Begin Stealth";
    public static final String BEGIN_SEARCH = "Begin Search";
    public static final String BEGIN_UNLOCK = "Begin Unlock";
    public static final String UNLOCK_FAILED = "Unlock Failed";
    public static final String UNLOCK_SUCCESS = "Unlock Success";
    public static final String SEPARATE_FROM_PARTY = "Separate From Party";
    public static final String REJOIN_PARTY = "Rejoin Party";
    public static final String POISONED = "Poisoned";
    private String[] effectNames = new String[]{
        BATTLECRY_1,
        BATTLECRY_2,
        BATTLECRY_3,
        BATTLECRY_4,
        BATTLECRY_5,
        BATTLECRY_6,
        SELECT_1,
        SELECT_2,
        SELECT_3,
        ATTACK_GRUNT_1,
        ATTACK_GRUNT_2,
        ATTACK_GRUNT_3,
        PAIN_GRUNT_1,
        PAIN_GRUNT_2,
        LOW_HEALTH,
        DEAD,
        CRITICAL_HIT,
        TARGET_IMMUNE_TO_ASSAULT,
        LAY_MINE,
        DISARM_MINE,
        BEGIN_STEALTH,
        BEGIN_SEARCH,
        BEGIN_UNLOCK,
        UNLOCK_FAILED,
        UNLOCK_SUCCESS,
        SEPARATE_FROM_PARTY,
        REJOIN_PARTY,
        POISONED
    };

    public SsfFile loadFile(NwnByteArrayInputStream stream, String fileName, Tlk tlkFile) throws IOException {
        String magicNumber = stream.readString(4);
        String version = stream.readString(4);
        if (!magicNumber.equals("SSF ")) {
            throw new IllegalArgumentException("Cannot parse file [" + fileName + "] as SSF file");
        }
        if (!version.equals("V1.1")) {
            throw new IllegalArgumentException("Cannot parse file [" + fileName + "] as SSF file");
        }

        int entriesOffset = stream.readInt();
        int entryCount = (stream.length() - entriesOffset) / 4;
        stream.setPosition(entriesOffset);
        SsfFile ssfFile = new SsfFile();
        int maxCount=Math.min(entryCount, effectNames.length);
        for (int i = 0; i < maxCount; i++) {
            int stringRef = stream.readInt();
            StringEntry stringEntry = null;
            if (stringRef != -1) {
                stringEntry = tlkFile.getString(stringRef);
            }
            String effectName = effectNames[i];
            SsfEntry ssfEntry = new SsfEntry(effectName, stringRef, stringEntry);
            ssfFile.getSsfEntries().add(ssfEntry);
            ssfFile.getMapSsfEntries().put(effectName, ssfEntry);
        }

        return ssfFile;
    }
}
