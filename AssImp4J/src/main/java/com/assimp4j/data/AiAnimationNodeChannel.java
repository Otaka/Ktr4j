package com.assimp4j.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class AiAnimationNodeChannel {

    private String nodeName;
    private List<AiAnimationPositionKey> positionKeys = new ArrayList<>();
    private List<AiAnimationRotationKey> rotationKeys = new ArrayList<>();
    private List<AiAnimationScaleKey> scaleKeys = new ArrayList<>();

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void addPositionKey(AiAnimationPositionKey key) {
        positionKeys.add(key);
    }

    public void addRotationKey(AiAnimationRotationKey key) {
        rotationKeys.add(key);
    }

    public void addScaleKey(AiAnimationScaleKey key) {
        scaleKeys.add(key);
    }

    public List<AiAnimationScaleKey> getScaleKeys() {
        return scaleKeys;
    }

    public List<AiAnimationRotationKey> getRotationKeys() {
        return rotationKeys;
    }

    public List<AiAnimationPositionKey> getPositionKeys() {
        return positionKeys;
    }

}
