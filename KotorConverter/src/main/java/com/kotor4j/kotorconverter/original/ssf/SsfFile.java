package com.kotor4j.kotorconverter.original.ssf;

import com.kotor4j.kotorconverter.original.dialog.StringEntry;
import com.kotor4j.utils.StringBuilderWithPadding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class SsfFile {

    private List<SsfEntry> ssfEntries = new ArrayList<>();
    private Map<String, SsfEntry> mapSsfEntries = new HashMap<>();

    public List<SsfEntry> getSsfEntries() {
        return ssfEntries;
    }

    public Map<String, SsfEntry> getMapSsfEntries() {
        return mapSsfEntries;
    }

    public String toJson() {

        StringBuilderWithPadding sb = new StringBuilderWithPadding("  ");
        sb.println("[");
        sb.incLevel();
        for (int i = 0; i < ssfEntries.size(); i++) {
            SsfEntry ssfEntry = ssfEntries.get(i);
            if (i > 0) {
                sb.println(",");
            }

            sb.println("{");
            sb.incLevel();

            sb.println("\"effectName\":\"" + ssfEntry.getEffectName() + "\",");
            if (ssfEntry.getVoiceStringEntry() == null) {
                sb.println("\"strRef\":" + ssfEntry.getStringRef());
            } else {
                StringEntry stringEntry = ssfEntry.getVoiceStringEntry();
                sb.println("\"strRef\":" + ssfEntry.getStringRef() + ",");
                sb.println("\"dbgString\":\"" + stringEntry.getString().replace("\"", "\"\"") + "\",");
                sb.println("\"dbgVoiceRef\":\"" + stringEntry.getVoiceResRef().replace("\"", "\"\"") + "\",");
                sb.println("\"dbgFlag\":" + stringEntry.getFlag() + ",");
                sb.println("\"dbgSoundLength\":" + stringEntry.getSoundLength());
            }

            sb.decLevel();
            sb.print("}");
        }

        sb.decLevel();
        sb.println();
        sb.println("]");
        return sb.toString();
    }
}
