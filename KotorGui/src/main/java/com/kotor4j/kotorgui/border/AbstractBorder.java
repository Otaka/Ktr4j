package com.kotor4j.kotorgui.border;

import com.kotor4j.kotorgui.components.AbstractComponent;

/**
 * @author Dmitry
 */
public class AbstractBorder {

    protected AbstractComponent parentComponent;

    public float getLeftWidth() {
        return 0;
    }

    public float getRightWidth() {
        return 0;
    }

    public float getTopHeight() {
        return 0;
    }

    public float getBottomHeight() {
        return 0;
    }
    
    public void applyBorderToComponent(AbstractComponent component){
        parentComponent=component;
    }
    
    public void afterComponentResize(){
    
    }
}
