package com.jmonkey.simplegeomloader.data;

import com.jme3.math.Quaternion;

/**
 *
 * @author Dmitry
 */
public class SglNode {

    private String name;
    private float[] position;
    private Quaternion orientation;
    private String parent;
    

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOrientation(Quaternion orientation) {
        this.orientation = orientation;
    }

    public void setPosition(float[] position) {
        this.position = position;
    }

    public Quaternion getOrientation() {
        return orientation;
    }

    public float[] getPosition() {
        return position;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParent() {
        return parent;
    }

    public boolean hasGeometry(){
        return false;
    }
    
    @Override
    public String toString() {
        return (hasGeometry()?"GeometryNode":"Node")+": " + name + ". Parent: " + parent;
    }

}
