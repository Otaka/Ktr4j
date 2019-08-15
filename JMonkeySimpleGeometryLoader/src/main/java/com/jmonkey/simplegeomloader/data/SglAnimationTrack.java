package com.jmonkey.simplegeomloader.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitry
 */
public class SglAnimationTrack {

    private String nodeName;
    private final List<SglAnimationPosition> positionAnimation = new ArrayList<>();
    private final List<SglAnimationRotation> rotationAnimation = new ArrayList<>();

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<SglAnimationRotation> getRotationAnimation() {
        return rotationAnimation;
    }

    public List<SglAnimationPosition> getPositionAnimation() {
        return positionAnimation;
    }

    public String getNodeName() {
        return nodeName;
    }

    @Override
    public String toString() {
        return "Node: "+nodeName+". PositionTracks: "+positionAnimation.size()+". RotationTracks: "+rotationAnimation.size();
    }

    
}
