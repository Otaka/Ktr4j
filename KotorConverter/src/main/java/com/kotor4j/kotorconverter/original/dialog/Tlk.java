package com.kotor4j.kotorconverter.original.dialog;

import com.kotor4j.utils.StringBuilderWithPadding;

/**
 * @author sad
 */
public class Tlk {

    private final int languageId;
    private final StringEntry[] entries;

    private final StringEntry emptyString;

    public Tlk(int languageId, StringEntry[] entries) {
        this.languageId = languageId;
        this.entries = entries;
        emptyString = new StringEntry(-1, "", "", 0, 0);
    }

    public int getLanguageId() {
        return languageId;
    }

    public StringEntry[] getEntries() {
        return entries;
    }

    public StringEntry getString(int stringResRef) {
        if (stringResRef < 0 || stringResRef >= entries.length) {
            return emptyString;
        }

        return entries[stringResRef];
    }

    public String toJson() {
        StringBuilderWithPadding sb = new StringBuilderWithPadding("  ");
        sb.println("[");
        sb.incLevel();

        for (int i = 0; i < entries.length; i++) {
            StringEntry se = entries[i];
            if (i > 0) {
                sb.println(",");
            }

            sb.println("{");
            sb.incLevel();

            sb.println("\"id\":" + se.getId() + ",");
            sb.println("\"string\":\"" + escapeJson(se.getString()) + "\",");
            sb.println("\"voiceResRef\":\"" + escapeJson(se.getVoiceResRef()) + "\",");
            sb.println("\"flag\":\"" + se.getFlag() + "\",");
            sb.println("\"soundLength\":\"" + se.getSoundLength() + "\"");

            sb.decLevel();
            sb.print("}");
        }

        sb.decLevel();
        sb.println();
        sb.println("]");
        return sb.toString();
    }

    private String escapeJson(String escapeJson) {
        if (escapeJson.contains("\"")) {
            escapeJson = escapeJson.replace("\"", "\\\"");
        }

        return escapeJson;
    }
}
