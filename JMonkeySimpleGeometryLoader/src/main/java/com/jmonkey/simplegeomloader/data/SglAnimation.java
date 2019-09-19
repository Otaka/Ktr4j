package com.jmonkey.simplegeomloader.data;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dmitry
 */
public class SglAnimation {

    private String name;
    private final List<SglAnimationEvent> events = new ArrayList<>();
    private float animationLength;
    private final List<SglAnimationTrack> animationTracks = new ArrayList<>();

    public List<SglAnimationEvent> getEvents() {
        return events;
    }

    public void setAnimationLength(float animationLength) {
        this.animationLength = animationLength;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAnimationLength() {
        return animationLength;
    }

    public List<SglAnimationTrack> getAnimationTracks() {
        return animationTracks;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (events.isEmpty()) {
            return name+". " + animationLength + "sec. AnimationTracks count:" + animationTracks.size();
        } else {
            return name+". " + animationLength + "sec. AnimationTracks count: " + animationTracks.size() + ". Events count: " + events.size();
        }
    }
}
