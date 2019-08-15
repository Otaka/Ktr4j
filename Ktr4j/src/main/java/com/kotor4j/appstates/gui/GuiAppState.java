package com.kotor4j.appstates.gui;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.kotor4j.GameWindow;
import com.kotor4j.appstates.gui.widgets.ButtonGuiWidget;
import com.kotor4j.appstates.gui.widgets.Gui;
import com.kotor4j.appstates.gui.widgets.GuiBorder;
import com.kotor4j.appstates.gui.widgets.GuiColor;
import com.kotor4j.appstates.gui.widgets.GuiMoveTo;
import com.kotor4j.appstates.gui.widgets.GuiWidget;
import com.kotor4j.appstates.gui.widgets.LabelGuiWidget;
import com.kotor4j.appstates.gui.widgets.PanelGuiWidget;
import com.kotor4j.input.AbstractRawInput;
import com.kotor4j.nodes.PictureNode;
import com.kotor4j.resourcemanager.ResourceRef;
import com.kotor4j.resourcemanager.chitinkey.ResourceType;
import com.kotor4j.resourcemanager.dialog.StringEntry;
import com.kotor4j.resourcemanager.gff.GffStructure;
import com.kotor4j.resourcemanager.gff.fields.GffByte;
import com.kotor4j.resourcemanager.gff.fields.GffDWord;
import com.kotor4j.resourcemanager.gff.fields.GffExoString;
import com.kotor4j.resourcemanager.gff.fields.GffField;
import com.kotor4j.resourcemanager.gff.fields.GffFieldValue;
import com.kotor4j.resourcemanager.gff.fields.GffInt;
import com.kotor4j.resourcemanager.gff.fields.GffList;
import com.kotor4j.resourcemanager.gff.fields.GffResRef;
import com.kotor4j.resourcemanager.gff.fields.GffStruct;
import com.kotor4j.resourcemanager.gff.fields.GffVector;
import com.kotor4j.resourcemanager.gff.filetypes.GuiGff;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class GuiAppState extends BaseAppState {

    private GameWindow app;
    private int screenHeight;
    private List<Gui> guiElements = new ArrayList<>();

    @Override
    protected void initialize(Application app) {
        this.app = (GameWindow) app;
        screenHeight = this.app.getSettings().getHeight();
        initInput();
        app.enqueue(new Runnable() {
            @Override
            public void run() {
                 try {
                    loadGui("mipc8x6");
                } catch (IOException ex) {
                    throw new RuntimeException("Error while loading gui file", ex);
                }
            }
        });
        
        Map<ResourceType,ResourceRef>resMap=this.app.getGameContext().getResourceManager().getResourceRefByName("dialogfont16x16");
        resMap.get(ResourceType.TXI);
    }

    private void initInput() {
        app.getInputManager().addRawInputListener(new AbstractRawInput() {
            @Override
            public void onMouseButtonEvent(MouseButtonEvent evt) {
                if (evt.isPressed()) {
                    if (evt.getButtonIndex() == 0) {
                        if (onClick(evt.getX(), screenHeight - evt.getY())) {
                            evt.setConsumed();
                        }
                    }
                }
            }

            @Override
            public void onMouseMotionEvent(MouseMotionEvent evt) {
                if (onClick(evt.getX(), screenHeight - evt.getY())) {
                }
            }
        });
    }

    private boolean onClick(int x, int y) {
        GuiWidget clickedWidget = searchWidgetByCoords(x, y);
        if (clickedWidget != null) {
           // System.out.println("Clicked on [" + clickedWidget.getId() + ":" + clickedWidget.getTag() + "] Texture:" + clickedWidget.getGuiBorder().getFill());
            return true;
        }

        return false;
    }

    private GuiWidget searchWidgetByCoords(float x, float y) {
        for (Gui gui : guiElements) {
            GuiWidget widget = innerSearchLastWidgetByCoords(gui.getRootWidget(), x, y);
            if (widget != null) {
                return widget;
            }
        }
        return null;
    }

    private GuiWidget innerSearchLastWidgetByCoords(GuiWidget widget, float x, float y) {
        float wx = widget.getX();
        float wy = widget.getY();
        boolean hit = false;
        if (!widget.isLocked()) {
            if (wx <= x && wy <= y && wx + widget.getWidth() > x && wy + widget.getHeight() > y) {
                hit = true;
            }
        }
        List<GuiWidget> widgets = widget.getChildren();
        if (widgets != null) {
            for (int i = widgets.size() - 1; i >= 0; i--) {
                GuiWidget child = widgets.get(i);
                GuiWidget clickedWidget = innerSearchLastWidgetByCoords(child, x, y);
                if (clickedWidget != null) {
                    return clickedWidget;
                }
            }
        }
        if (hit == true) {
            return widget;
        }
        return null;
    }

    public void clearGui() {
        for (Gui gui : guiElements) {
            gui.getRootWidget().getMainPicture().removeFromParent();
        }
        guiElements.clear();
    }

    public void loadGui(String fileName) throws IOException {
        if (getGui(fileName) != null) {
            return;//gui already installed
        }

        Map<ResourceType, ResourceRef> resource = app.getGameContext().getResourceManager().getResourceRefByName(fileName);
        if (resource == null || resource.get(ResourceType.GUI) == null) {
            throw new IllegalArgumentException("Cannot find GUI resource with name [" + fileName + "]");
        }

        Gui gui = new Gui();
        gui.setName(fileName);

        GuiGff guiResource = (GuiGff) app.getGameContext().getResourceManager().getConvertedResource(resource.get(ResourceType.GUI));
        GffStructure gffStructure = guiResource.getGff().getRoot();
        GuiWidget guiWidget = createWidget(gffStructure, gui, null);

        gui.setRootWidget(guiWidget);
        guiElements.add(gui);
        app.getGuiNode().attachChild(guiWidget.getMainPicture());
    }

    public Gui getGui(String fileName) {
        for (Gui gui : guiElements) {
            if (gui.getName().equals(fileName)) {
                return gui;
            }
        }
        return null;
    }

    public void removeGui(String fileName) {
        Gui gui = getGui(fileName);
        if (gui != null) {
            guiElements.remove(gui);
        }
    }

    private GuiWidget createWidget(GffStructure gffStructure, Gui gui, PictureNode parentNode) {
        GffField controlTypeField = findGffField(gffStructure.getFields(), "CONTROLTYPE");
        int controlType = ((GffInt) (controlTypeField.getValue())).getValue();

        GffField idField = findGffField(gffStructure.getFields(), "ID");
        GffField objLockedField = findGffField(gffStructure.getFields(), "Obj_Locked");
        GffField tagField = findGffField(gffStructure.getFields(), "TAG");
        GffField parentIdField = findGffField(gffStructure.getFields(), "Obj_ParentID");
        GffField borderField = findGffField(gffStructure.getFields(), "BORDER");
        GffField extentField = findGffField(gffStructure.getFields(), "EXTENT");
        GffField moveToField = findGffField(gffStructure.getFields(), "MOVETO");

        GuiWidget widget = null;
        if (controlType == GuiWidget.PANEL) {
            widget = new PanelGuiWidget(this);
        } else if (controlType == GuiWidget.LABEL) {
            widget = new LabelGuiWidget(this);
            fillLabel(gffStructure.getFields(), (LabelGuiWidget) widget);
        } else if (controlType == GuiWidget.BUTTON) {
            widget = new ButtonGuiWidget(this);
            fillButton(gffStructure.getFields(), (ButtonGuiWidget) widget);
        } else {
            widget = new PanelGuiWidget(this);
            //throw new IllegalArgumentException("Unknown widget type [" + controlType + "]");
        }

        if (extentField != null) {
            applyExtent((GffStruct) extentField.getValue(), widget);
        }
        if (moveToField != null) {
            widget.setMoveTo(getMoveTo(moveToField));
        }
        if (objLockedField != null) {
            widget.setLocked(getBooleanFromGffField(objLockedField, false));
        }
        if (idField != null) {
            int id = getIntFromGffField(idField);
            widget.setId(id);
            gui.putWidgetById(id, widget);
        }
        if (tagField != null) {
            String tag = getStringFromGffField(tagField);
            widget.setTag(tag);
            gui.putWidgetByTag(tag, widget);
        }
        if (parentIdField != null) {
            int parentId = getIntFromGffField(parentIdField);
            widget.setParentId(parentId);
        }
        
        if(borderField!=null){
            widget.setGuiBorder(getGuiBorder(borderField));
        }

        PictureNode pic = widget.createMainPicture(parentNode);
        if (parentNode != null) {
            parentNode.attachChild(pic);
        }

        widget.initializeComponent(this);

        GffField controls = findGffField(gffStructure.getFields(), "CONTROLS");
        if (controls != null) {
            widget.setChildren(new ArrayList<>());
            GffList controlsVector = (GffList) controls.getValue();
            for (GffStructure control : controlsVector.getValue()) {
                widget.getChildren().add(createWidget(control, gui, pic));
            }
        }
        return widget;
    }

    private void fillButton(GffField[] fields, ButtonGuiWidget widget) {
        GffField hilightField = findGffField(fields, "HILIGHT");
        if (hilightField != null) {
            GuiBorder hilightBorder = getGuiBorder(hilightField);
            widget.setHilight(hilightBorder);
        }
    }

    private void fillLabel(GffField[] fields, LabelGuiWidget widget) {
        GffField gffSection = findGffField(fields, "TEXT");
        if (gffSection == null) {
            throw new IllegalArgumentException("Label widget does not have section TEXT");
        }

        GffStruct textStruct = (GffStruct) gffSection.getValue();
        GffStructure textStructure = textStruct.getValue();
        int alignment = getIntFromGffField(findGffField(textStructure.getFields(), "ALIGNMENT"));
        GuiColor color = getGuiColor(findGffField(textStructure.getFields(), "COLOR"));
        String fontName = getStringFromGffField(findGffField(textStructure.getFields(), "FONT"));
        String text = null;
        GffField textGffField = findGffField(textStructure.getFields(), "TEXT");
        if (textGffField != null) {
            text = getStringFromGffField(textGffField);
        }
        int strRefInt = getIntFromGffField(findGffField(textStructure.getFields(), "STRREF"));
        StringEntry strRef = null;
        if (strRefInt != -1) {
            strRef = app.getGameContext().getResourceManager().getTlk().getEntries()[strRefInt];
        }

        boolean pulsing = getBooleanFromGffField(findGffField(textStructure.getFields(), "PULSING"), false);
        if (pulsing) {
            System.out.println("Pulsing");
        }

        widget.setAlignment(alignment);
        widget.setColor(color);
        widget.setFontName(fontName);
        widget.setText(text);
        widget.setStrRef(strRef);
        widget.setPulsing(pulsing);
        
    }

    private GuiMoveTo getMoveTo(GffField field) {
        GffStruct struct = (GffStruct) field.getValue();
        GffStructure structure = struct.getValue();
        int up = getIntFromGffField(findGffField(structure.getFields(), "UP"));
        int down = getIntFromGffField(findGffField(structure.getFields(), "DOWN"));
        int left = getIntFromGffField(findGffField(structure.getFields(), "LEFT"));
        int right = getIntFromGffField(findGffField(structure.getFields(), "RIGHT"));
        return new GuiMoveTo(down, left, right, up);
    }

    private GuiBorder getGuiBorder(GffField field) {
        GuiBorder guiBorder = new GuiBorder();
        GffStruct struct = (GffStruct) field.getValue();
        GffStructure structure = struct.getValue();
        String corner = getStringFromGffField(findGffField(structure.getFields(), "CORNER"));
        String edge = getStringFromGffField(findGffField(structure.getFields(), "EDGE"));
        String fill = getStringFromGffField(findGffField(structure.getFields(), "FILL"));
        int fillStyle = getIntFromGffField(findGffField(structure.getFields(), "FILLSTYLE"));
        int dimension = getIntFromGffField(findGffField(structure.getFields(), "DIMENSION"));
        GffField innerOffsetField = findGffField(structure.getFields(), "INNEROFFSET");
        int innerOffset = 0;
        if (innerOffsetField != null) {
            innerOffset = getIntFromGffField(innerOffsetField);
        }
        boolean pulsing = false;
        GffField pulsingField = findGffField(structure.getFields(), "PULSING");
        if (pulsingField != null) {
            pulsing = getBooleanFromGffField(pulsingField, false);
        }

        GuiColor color;
        GffField colorField = findGffField(structure.getFields(), "COLOR");
        if (colorField != null) {
            color = getGuiColor(colorField);
        } else {
            color = new GuiColor(0, 0, 0);
        }
        guiBorder.setCorner(corner);
        guiBorder.setEdge(edge);
        guiBorder.setFill(fill);
        guiBorder.setFillStyle(fillStyle);
        guiBorder.setDimension(dimension);
        guiBorder.setInnerOffset(innerOffset);
        guiBorder.setPulsing(pulsing);
        guiBorder.setGuiColor(color);
        return guiBorder;
    }

    private GuiColor getGuiColor(GffField field) {
        GffVector vector = (GffVector) field.getValue();

        float red = vector.getValue()[0];
        float green = vector.getValue()[1];
        float blue = vector.getValue()[2];
        return new GuiColor(red, green, blue);
    }

    private String getStringFromGffField(GffField field) {
        GffFieldValue objVal = field.getValue();
        if (objVal instanceof GffExoString) {
            GffExoString string = (GffExoString) field.getValue();
            return string.getValue();
        } else if (objVal instanceof GffResRef) {
            GffResRef string = (GffResRef) field.getValue();
            String val = string.getValue();
            return val;
        } else {
            throw new IllegalArgumentException("Not implemented string type " + objVal.getClass().getSimpleName());
        }
    }

    private boolean getBooleanFromGffField(GffField field, boolean defaultValue) {
        if (field == null) {
            return defaultValue;
        }
        GffByte byteValue = (GffByte) field.getValue();
        return byteValue.getValue() != 0;
    }

    private int getIntFromGffField(GffField field) {
        if (field.getValue() instanceof GffInt) {
            GffInt intValue = (GffInt) field.getValue();
            return intValue.getValue();
        } else if (field.getValue() instanceof GffDWord) {
            GffDWord intValue = (GffDWord) field.getValue();
            return intValue.getValue();
        } else {
            throw new IllegalArgumentException("Unknown int type " + field.getValue().getClass().getSimpleName());
        }
    }

    private void applyExtent(GffStruct extentField, GuiWidget widget) {
        GffStructure structure = extentField.getValue();
        int left = ((GffInt) findGffField(structure.getFields(), "LEFT").getValue()).getValue();
        int top = ((GffInt) findGffField(structure.getFields(), "TOP").getValue()).getValue();
        int width = ((GffInt) findGffField(structure.getFields(), "WIDTH").getValue()).getValue();
        int height = ((GffInt) findGffField(structure.getFields(), "HEIGHT").getValue()).getValue();

        widget.setHeight(height);
        widget.setWidth(width);
        widget.setX(left);
        widget.setY(top);
    }

    private GffField findGffField(GffField[] fields, String name) {
        for (GffField f : fields) {
            if (f.getLabel().equals(name)) {
                return f;
            }
        }
        return null;
    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
