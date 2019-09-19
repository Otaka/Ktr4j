package com.kotor4j.kotorgui;

import com.jme3.app.SimpleApplication;
import com.jme3.input.RawInputListener;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.kotor4j.kotorgui.components.AbstractComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author Dmitry
 */
public class GuiManager {

    private RawInputListener rawInputListener;
    private SimpleApplication application;
    private List<AbstractComponent> topLevelComponents = new ArrayList<>();
    private int screenWidth;
    private int screenHeight;
    private int lastMouseX;
    private int lastMouseY;
    private int lastComponentX;
    private int lastComponentY;
    private Stack<AbstractComponent> mouseEnteredComponents = new Stack<>();
    private List<AbstractComponent> registerForUpdate = new ArrayList<>();
    private AbstractComponent capturedComponent;

    public GuiManager(SimpleApplication application) {
        screenWidth = application.getCamera().getWidth();
        screenHeight = application.getCamera().getHeight();
        createInputListener();
        this.application = application;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public SimpleApplication getApplication() {
        return application;
    }

    public void update(float tpf) {
        if (!registerForUpdate.isEmpty()) {
            for (int i = 0; i < registerForUpdate.size(); i++) {
                AbstractComponent component = registerForUpdate.get(i);
                component.update(tpf);
            }
        }
    }

    public void registerToUpdate(AbstractComponent component) {
        if (!registerForUpdate.contains(component)) {
            registerForUpdate.add(component);
        }
    }

    public void unregisterToUpdate(AbstractComponent component) {
        registerForUpdate.remove(component);
    }

    public void captureComponent(AbstractComponent component) {
        capturedComponent = component;
    }

    private void createInputListener() {
        rawInputListener = new RawInputListenerAdapter() {
            @Override
            public void onMouseMotionEvent(MouseMotionEvent evt) {
                if (evt.getDeltaWheel() != 0) {
                    processMouseWheel(evt);
                } else {
                    processMouseMotionEvent(evt);
                }
            }

            @Override
            public void onMouseButtonEvent(MouseButtonEvent evt) {
                //System.out.println(evt.toString());
                processMouseButton(evt);
            }

            @Override
            public void onKeyEvent(KeyInputEvent evt) {

            }
        };
    }

    private void processMouseWheel(MouseMotionEvent evt) {
        AbstractComponent component = null;
        if (capturedComponent != null) {
            component = capturedComponent;
        } else if (!mouseEnteredComponents.isEmpty()) {
            component = mouseEnteredComponents.peek();
        }
        if (component != null) {
            component.onMouseWheel(lastComponentX, lastComponentY, lastMouseX, lastMouseY, evt.getWheel(), evt.getDeltaWheel());
            evt.setConsumed();
        }
    }

    private void processMouseButton(MouseButtonEvent evt) {
        AbstractComponent component = null;
        if (capturedComponent != null) {
            component = capturedComponent;
        } else if (!mouseEnteredComponents.isEmpty()) {
            component = mouseEnteredComponents.peek();
        }

        if (component != null) {
            if (evt.isPressed()) {
                component.onMouseDown(lastComponentX, lastComponentY, lastMouseX, lastMouseY, evt.getButtonIndex());
            } else {
                component.onMouseUp(lastComponentX, lastComponentY, lastMouseX, lastMouseY, evt.getButtonIndex());
                capturedComponent = null;
            }
            evt.setConsumed();
        }
    }

    private static boolean isComponentContainsPoint(AbstractComponent component, float componentGlobalOffsetX, float componentGlobalOffsetY, float px, float py) {
        if ((px >= componentGlobalOffsetX) && (py >= componentGlobalOffsetY)
                && (px <= (componentGlobalOffsetX + component.getWidth())
                && (py <= (componentGlobalOffsetY + component.getHeight())))) {
            return true;
        } else {
            return false;
        }
    }

    private void processMouseMotionEvent(MouseMotionEvent e) {
        //at first let's look at components where mouse was in previous invocation
        float mouseX = e.getX();
        float mouseY = e.getY();
        lastMouseX = (int) mouseX;
        lastMouseY = (int) mouseY;
        if (capturedComponent != null) {
            Vector3f componentWorldPosition = capturedComponent.getGlobalPosition();
            lastComponentX = (int) (lastMouseX - componentWorldPosition.getX());
            lastComponentY = (int) (lastMouseY - componentWorldPosition.getY());
            capturedComponent.onMouseMove(lastComponentX, lastComponentY, lastMouseX, lastMouseY, e.getDX(), e.getDY());
            e.setConsumed();
            return;
        }
        float parentGlobalX = 0;
        float parentGlobalY = 0;
        AbstractComponent lastMatchedComponent = null;
        int elementsToRemoveFromStack = 0;
        int size = mouseEnteredComponents.size();
        for (int i = 0; i < size; i++) {
            AbstractComponent component = mouseEnteredComponents.get(i);
            float componentGlobalX = parentGlobalX + component.getX();
            float componentGlobalY = parentGlobalY + component.getY();
            if (isComponentContainsPoint(component, componentGlobalX, componentGlobalY, mouseX, mouseY)) {
                lastMatchedComponent = component;
                parentGlobalX = componentGlobalX;
                parentGlobalY = componentGlobalY;
            } else {
                elementsToRemoveFromStack = size - i;
                break;
            }
        }
        //we have some component in stack where mouse were, and we should remove them
        if (elementsToRemoveFromStack > 0) {
            for (int i = 0; i < elementsToRemoveFromStack; i++) {
                AbstractComponent component = mouseEnteredComponents.pop();
                component.onMouseExit();
            }
        }

        if (lastMatchedComponent != null) {
            //we should sear
            while (true) {
                List<AbstractComponent> children = lastMatchedComponent.getChildren();
                size = children.size();
                boolean found = false;
                for (int i = 0; i < size; i++) {
                    AbstractComponent child = children.get(i);
                    float componentGlobalX = parentGlobalX + child.getX();
                    float componentGlobalY = parentGlobalY + child.getY();
                    if (isComponentContainsPoint(child, componentGlobalX, componentGlobalY, mouseX, mouseY)) {
                        child.onMouseEnter();
                        mouseEnteredComponents.add(child);
                        lastMatchedComponent = child;
                        parentGlobalX = componentGlobalX;
                        parentGlobalY = componentGlobalY;
                        found = true;
                        break;
                    }
                }
                if (found == false) {
                    break;
                }
            }
        } else {
            int countOfTopLevelComponents = topLevelComponents.size();
            for (int i = 0; i < countOfTopLevelComponents; i++) {
                AbstractComponent component = topLevelComponents.get(i);
                parentGlobalX = 0;
                parentGlobalY = 0;
                float componentGlobalX = parentGlobalX + component.getX();
                float componentGlobalY = parentGlobalY + component.getY();
                if (isComponentContainsPoint(component, componentGlobalX, componentGlobalY, mouseX, mouseY)) {
                    boolean foundInBranch = false;
                    boolean foundOnLastIteration = true;
                    while (foundOnLastIteration) {
                        foundOnLastIteration = false;
                        mouseEnteredComponents.push(component);
                        component.onMouseEnter();
                        parentGlobalX = componentGlobalX;
                        parentGlobalY = componentGlobalY;
                        int childrenCount = component.getChildren().size();
                        for (int j = 0; j < childrenCount; j++) {
                            AbstractComponent childComponent = component.getChildren().get(j);
                            componentGlobalX = parentGlobalX + childComponent.getX();
                            componentGlobalY = parentGlobalY + childComponent.getY();
                            if (isComponentContainsPoint(childComponent, componentGlobalX, componentGlobalY, mouseX, mouseY)) {
                                component = childComponent;
                                foundOnLastIteration = true;
                                foundInBranch = true;
                            }
                        }
                    }
                    if (foundInBranch == true) {
                        break;
                    }
                }
            }
        }

        if (!mouseEnteredComponents.isEmpty()) {
            AbstractComponent lastComponent = mouseEnteredComponents.peek();
            lastComponentX = (int) (mouseX - parentGlobalX);
            lastComponentY = (int) (mouseY - parentGlobalY);
            lastComponent.onMouseMove(lastComponentX, lastComponentY, (int) mouseX, (int) mouseY, e.getDX(), e.getDY());
            e.setConsumed();
        }
    }

    public RawInputListener getRawInputListener() {
        return rawInputListener;
    }

    public void addComponent(AbstractComponent abstractComponent) {
        topLevelComponents.add(abstractComponent);
        //application.getGuiNode().attachChild(abstractComponent.getComponentNode());
    }

    public void removeComponent(AbstractComponent component) {
        topLevelComponents.remove(component);
        application.getGuiNode().detachChild(component.getComponentNode());
    }

    public void onRender(Node guiNode, RenderManager renderManager, float tpf) {
        int count = topLevelComponents.size();
        for (int i = 0; i < count; i++) {
            AbstractComponent comp = topLevelComponents.get(i);
            guiNode.attachChild(comp.getComponentNode());
        }

        guiNode.updateLogicalState(tpf);
        guiNode.updateGeometricState();
        ViewPort viewPort = renderManager.getMainView("Default");
        for (int i = 0; i < count; i++) {
            AbstractComponent comp = topLevelComponents.get(i);
            renderComponent(renderManager, comp, viewPort);

        }

        //renderManager.renderScene(guiNode, renderManager.getMainViews().get(0));
        for (int i = 0; i < count; i++) {
            AbstractComponent comp = topLevelComponents.get(i);
            guiNode.detachChild(comp.getComponentNode());
        }

        //renderManager.getRenderer().clearClipRect();
    }

    private void renderComponent(RenderManager renderManager, AbstractComponent component, ViewPort viewport) {
        Vector3f globalPos = component.getGlobalPosition();
        renderManager.getRenderer().setClipRect((int) globalPos.x, (int) globalPos.y, (int) component.getWidth(), (int) component.getHeight());
        List<Spatial> geometryList = component.getComponentNode().getUserData("guiChildren");

        int geometryCount = geometryList.size();
        for (int i = 0; i < geometryCount; i++) {
            Spatial sp = geometryList.get(i);
            renderManager.renderScene(sp, viewport);
        }

        int childrenCount = component.getChildren().size();
        for (int i = 0; i < childrenCount; i++) {
            AbstractComponent childComponent = component.getChildren().get(i);
            renderComponent(renderManager, childComponent, viewport);
        }
    }
}
