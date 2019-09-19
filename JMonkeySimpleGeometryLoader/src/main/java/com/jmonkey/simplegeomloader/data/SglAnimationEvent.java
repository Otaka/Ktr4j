package com.jmonkey.simplegeomloader.data;

/**
 *
 * @author Dmitry
 */
public class SglAnimationEvent {

    private String name;
    private float time;

    public SglAnimationEvent(String name, float time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public float getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Name: " + name + ". Time: " + time;
    }

}
