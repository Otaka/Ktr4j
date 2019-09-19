package com.kotor4j.appstates.gui.widgets;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry
 */
public class Gui {

    private GuiWidget rootWidget;
    private String name;
    private Map<Integer, GuiWidget> id2Widget = new HashMap<>();
    private Map<String, GuiWidget> tag2Widget = new HashMap<>();

    public void putWidgetById(int id, GuiWidget widget) {
        id2Widget.put(id, widget);
    }

    public void putWidgetByTag(String tag, GuiWidget widget) {
        tag2Widget.put(tag, widget);
    }

    public GuiWidget getWidgetById(int id) {
        return id2Widget.get(id);
    }

    public GuiWidget getWidgetByTag(String tag) {
        return tag2Widget.get(tag);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRootWidget(GuiWidget rootWidget) {
        this.rootWidget = rootWidget;
    }

    public String getName() {
        return name;
    }

    public GuiWidget getRootWidget() {
        return rootWidget;
    }

}
