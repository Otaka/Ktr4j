package com.assimp4j.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class AiAnimation {

    private String name;
    private double ticksPerSecond;
    private double duration;
    private List<AiAnimationNodeChannel> channels = new ArrayList<>();

    public void addChannel(AiAnimationNodeChannel channel) {
        channels.add(channel);
    }

    public List<AiAnimationNodeChannel> getChannels() {
        return channels;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void setTicksPerSecond(double ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
    }

    public double getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public double getTicksPerSecond() {
        return ticksPerSecond;
    }

    @Override
    public String toString() {
        return ""+getName()+". Duration:"+getDuration();
    }
    
}
