package com.jmonkey.simplegeomloader.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dmitry
 */
public class SglModel {

    private final Map<String, Object> userProperties = new HashMap<>();
    private final List<SglNode> nodes = new ArrayList<>();
    private final List<SglAnimation> animations = new ArrayList<>();
    private String filename;

    public List<SglNode> getNodes() {
        return nodes;
    }

    public Map<String, Object> getUserProperties() {
        return userProperties;
    }

    public List<SglAnimation> getAnimations() {
        return animations;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public boolean hasSkin() {
        for (SglNode node : getNodes()) {
            if (node instanceof SglGeometry) {
                if (((SglGeometry) node).isSkin()) {
                    return true;
                }
            }
        }

        return false;
    }
    
    @Override
    public String toString() {
        return filename + ". Nodes count: " + nodes.size();
    }
}
