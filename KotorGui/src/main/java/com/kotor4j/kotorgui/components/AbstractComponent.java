package com.kotor4j.kotorgui.components;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.kotor4j.kotorgui.GuiManager;
import com.kotor4j.kotorgui.border.AbstractBorder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry
 */
public class AbstractComponent {

    private String name;
    protected List<AbstractComponent> children = new ArrayList<>();
    protected AbstractComponent parent;
    private float paddingTop;
    private float paddingBottom;
    private float paddingLeft;
    private float paddingRight;
    private AbstractBorder border;
    private Node componentNode;
    protected AssetManager assetManager;
    protected GuiManager guiManager;

    public AbstractComponent(GuiManager guiManager) {
        this.guiManager = guiManager;
        this.assetManager = guiManager.getApplication().getAssetManager();
        componentNode = new Node();
    }
    
    public Vector3f getGlobalPosition(){
        return componentNode.getWorldTranslation();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GuiManager getGuiManager() {
        return guiManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public Node getComponentNode() {
        return componentNode;
    }

    public void setBorder(AbstractBorder border) {
        setPaddings(border.getLeftWidth(), border.getTopHeight(), border.getRightWidth(), border.getBottomHeight());
        this.border = border;
        border.applyBorderToComponent(this);
    }

    public void setBorderDoNotScaleContent(AbstractBorder border) {
        setXY(getX() - border.getLeftWidth(), getY() - border.getTopHeight());
        setWidth(getWidth() + border.getLeftWidth() + border.getRightWidth());
        setHeight(getHeight() + border.getTopHeight() + border.getBottomHeight());
        setPaddings(border.getLeftWidth(), border.getTopHeight(), border.getRightWidth(), border.getBottomHeight());
        this.border = border;
        border.applyBorderToComponent(this);
    }

    public AbstractBorder getBorder() {
        return border;
    }

    public float getClientX() {
        return paddingLeft;
    }

    public float getClientY() {
        return paddingTop;
    }

    public float getClientWidth() {
        float clientWidth = getWidth() - (paddingLeft + paddingRight);
        if (clientWidth < 0) {
            return 0;
        }
        return clientWidth;
    }

    public float getClientHeight() {
        float clientHeight = getHeight() - (paddingTop + paddingBottom);
        if (clientHeight < 0) {
            return 0;
        }
        return clientHeight;
    }

    public void setPaddings(float left, float top, float right, float bottom) {
        if (!children.isEmpty()) {
            throw new IllegalStateException("You can set components padding only before add any children");
        }
        this.paddingLeft = left;
        this.paddingTop = top;
        this.paddingRight = right;
        this.paddingBottom = bottom;

        onPaddingsChanged();
    }

    public void onPaddingsChanged() {
        //this is empty. Can be overwritten in subclasses to catch client padding change
    }

    public float getPaddingBottom() {
        return paddingBottom;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }

    public float getPaddingRight() {
        return paddingRight;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public List<AbstractComponent> getChildren() {
        return children;
    }

    public AbstractComponent getParent() {
        return parent;
    }

    public void setParent(AbstractComponent parent) {
        this.parent = parent;
    }

    public void addChild(AbstractComponent component) {
        children.add(component);
        getComponentNode().attachChild(component.getComponentNode());
        component.setParent(this);
    }

    public float getX() {
        return componentNode.getLocalTranslation().x;
    }

    public float getY() {
        return componentNode.getLocalTranslation().y;
    }

    public float getWidth() {
        return 0;
    }

    public float getHeight() {
        return 0;
    }

    public void setXY(float x, float y) {
        componentNode.setLocalTranslation(x, y, componentNode.getLocalTranslation().z);
    }

    public float getZOrder() {
        return componentNode.getLocalTranslation().z;
    }

    public void setZOrder(float z) {
        Vector3f translation = componentNode.getLocalTranslation();
        componentNode.setLocalTranslation(translation.x, translation.y, z);
    }

    public void setWidth(float width) {
        //you can override it
    }

    public void setHeight(float width) {
        //you can override it
    }

    public void onMouseEnter() {
        //you can override it
    }

    public void onMouseExit() {
        //you can override it
    }

    public void onMouseMove(int x, int y, int globalX, int globalY, int dx, int dy) {
        //you can override it
    }

    public void onMouseDown(int x, int y, int globalX, int globalY, int mouseButton) {
        //you can override it
    }

    public void onMouseUp(int x, int y, int globalX, int globalY, int mouseButton) {
        //you can override it
    }

    public void onMouseWheel(int x, int y, int globalX, int globalY, int wheelScroll, int dWheelScroll) {
        //you can override it
    }

    public void captureInput(){
        guiManager.captureComponent(this);
    }
    
    public void registerToUpdate() {
        guiManager.registerToUpdate(this);
    }

    public void unregisterToUpdate() {
        guiManager.unregisterToUpdate(this);
    }

    public void update(float tpf) {

    }

    @Override
    public String toString() {
        if (name != null) {
            return name;
        } else {
            return getClass().getSimpleName();
        }
    }

}
