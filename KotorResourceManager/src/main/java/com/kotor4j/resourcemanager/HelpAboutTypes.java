package com.kotor4j.resourcemanager;

import com.kotor4j.resourcemanager.chitinkey.ResourceType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.apache.commons.io.IOUtils;

/**
 * @author Dmitry
 */
public class HelpAboutTypes {

    public String getHelpAboutTypes(String type) {
        if (type.equalsIgnoreCase("all")) {
            StringBuilder sb = new StringBuilder();
            for (ResourceType rt : ResourceType.values()) {
                sb.append("FILE: "+rt.name()+" :"+ getHelpAboutTypes(rt.toString()));
                sb.append("\n");
            }
            return sb.toString();
        }

        if(type.equalsIgnoreCase("2da")){
            type="_2da";
        }

        ResourceType resType;
        try {
            resType = ResourceType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Do not know such resource [" + type + "]. Known resource types " + Arrays.deepToString(ResourceType.values()));
        }

        String filePath = "/typesReference/" + resType.name().toLowerCase() + ".txt";
        try (InputStream is = HelpAboutTypes.class.getResourceAsStream(filePath)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            throw new RuntimeException("Error while reading typesRefence [" + filePath + "]",ex);
        }
    }
}
