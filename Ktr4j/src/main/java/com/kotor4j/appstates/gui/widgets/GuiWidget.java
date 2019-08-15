package com.kotor4j.appstates.gui.widgets;

import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.kotor4j.GameWindow;
import com.kotor4j.appstates.gui.GuiAppState;
import java.util.List;
import com.kotor4j.Utils;
import com.kotor4j.nodes.PictureNode;
import com.kotor4j.resourcemanager.ResourceManager;
import com.kotor4j.resourcemanager.ResourceRef;
import com.kotor4j.resourcemanager.chitinkey.ResourceType;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * @author Dmitry
 */
public class GuiWidget {

    public static int MAIN_PANEL = 11;
    public static int PROGRESSBAR = 10;
    public static int SCROLL = 9;
    public static int BUTTON = 6;
    public static int LABEL = 4;
    public static int PANEL = 2;
    private static Texture2D emptyTexture;

    private int x;
    private int y;
    private int width;
    private int height;
    private int id;
    private boolean locked = false;
    private String tag;
    private int parentId;
    private GuiBorder guiBorder;
    private GuiMoveTo moveTo;
    private List<GuiWidget> children;
    private PictureNode mainPicture;
    private GuiAppState guiAppState;

    public GuiWidget(GuiAppState guiAppState) {
        this.guiAppState = guiAppState;
    }

    public PictureNode getMainPicture() {
        return mainPicture;
    }

    public void setChildren(List<GuiWidget> children) {
        this.children = children;
    }

    public List<GuiWidget> getChildren() {
        return children;
    }

    public void setMoveTo(GuiMoveTo moveTo) {
        this.moveTo = moveTo;
    }

    public GuiMoveTo getMoveTo() {
        return moveTo;
    }

    public void setGuiBorder(GuiBorder guiBorder) {
        this.guiBorder = guiBorder;
    }

    public GuiBorder getGuiBorder() {
        return guiBorder;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getParentId() {
        return parentId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private float convertY(float y,PictureNode parentNode) {
        if(parentNode==null){
            return ((GameWindow)guiAppState.getApplication()).getSettings().getHeight()-y;
        }
        return parentNode.getHeight() - y;
    }

    public PictureNode createMainPicture(PictureNode parentNode) {
        PictureNode picture = new PictureNode(this.getClass().getSimpleName());
        picture.setWidth(width);
        picture.setHeight(height);
        picture.setPosition(x,convertY(y, parentNode)-height, 0);
        picture.setTexture(guiAppState.getApplication().getAssetManager(), getEmptyTexture(), true);
        mainPicture = picture;
        return picture;
    }

    public void initializeComponent(GuiAppState guiAppState){
        if(guiBorder!=null){
            applyBorder();
        }
    }
    
    public void applyBorder() {
        if (Utils.isEmpty(guiBorder.getFill()) == false) {
            if(guiBorder.getFill().equals("lbl_mimsg")){
                int x=0;
            }
            mainPicture.setTexture(guiAppState.getApplication().getAssetManager(), getTexture2D(guiBorder.getFill()), true);
        }
        if (!Utils.isEmpty(guiBorder.getCorner()) || !Utils.isEmpty(guiBorder.getEdge())) {
            if (!Utils.isEmpty(guiBorder.getCorner()) && !Utils.isEmpty(guiBorder.getEdge())) {

                Texture2D cornerTexture = getTexture2D(guiBorder.getCorner());
                Texture2D edgeTexture = getTexture2D(guiBorder.getEdge());
                int cornerWidth = cornerTexture.getImage().getWidth();
                int cornerHeight = cornerTexture.getImage().getHeight();
                int edgeWidth = edgeTexture.getImage().getWidth();
                int edgeHeight = edgeTexture.getImage().getHeight();

                mainPicture.setWidth(width - cornerWidth * 2);
                mainPicture.setHeight(height - cornerHeight * 2);
                mainPicture.setInnerPicturePosition(cornerWidth, cornerHeight);

                Picture tlCorner = new Picture("tlCorner");
                tlCorner.setWidth(cornerWidth);
                tlCorner.setHeight(cornerHeight);
                tlCorner.setPosition(0, height - cornerHeight);
                tlCorner.setTexture(guiAppState.getApplication().getAssetManager(), cornerTexture, true);
                mainPicture.attachChild(tlCorner);

                Picture trCorner = new Picture("trCorner");
                Utils.mirrorPictureHorizontally(trCorner);
                trCorner.setWidth(cornerWidth);
                trCorner.setHeight(cornerHeight);
                trCorner.setPosition(width - cornerWidth, height - cornerHeight);
                trCorner.setTexture(guiAppState.getApplication().getAssetManager(), cornerTexture, true);
                mainPicture.attachChild(trCorner);

                Picture blCorner = new Picture("blCorner");
                Utils.mirrorPictureVertically(blCorner);
                blCorner.setWidth(cornerWidth);
                blCorner.setHeight(cornerHeight);
                blCorner.setPosition(0, 0);
                blCorner.setTexture(guiAppState.getApplication().getAssetManager(), cornerTexture, true);
                mainPicture.attachChild(blCorner);

                Picture brCorner = new Picture("brCorner");
                Utils.mirrorPictureVertically(brCorner);
                Utils.mirrorPictureHorizontally(brCorner);
                brCorner.setWidth(cornerWidth);
                brCorner.setHeight(cornerHeight);
                brCorner.setPosition(width - cornerWidth, 0);
                brCorner.setTexture(guiAppState.getApplication().getAssetManager(), cornerTexture, true);
                mainPicture.attachChild(brCorner);

                Picture topEdge = new Picture("topEdge");
                topEdge.setWidth(width - cornerWidth * 2);
                topEdge.setHeight(edgeHeight);
                topEdge.setPosition(cornerWidth, height - cornerHeight);
                topEdge.setTexture(guiAppState.getApplication().getAssetManager(), edgeTexture, true);
                mainPicture.attachChild(topEdge);

                Picture bottomEdge = new Picture("bottomEdge");
                Utils.mirrorPictureVertically(bottomEdge);
                bottomEdge.setWidth(width - cornerWidth * 2);
                bottomEdge.setHeight(edgeHeight);
                bottomEdge.setPosition(cornerWidth, 0);
                bottomEdge.setTexture(guiAppState.getApplication().getAssetManager(), edgeTexture, true);
                mainPicture.attachChild(bottomEdge);

                Picture leftEdge = new Picture("leftEdge");
                Utils.rotatePicture270(leftEdge);
                leftEdge.setWidth(edgeWidth);
                leftEdge.setHeight(height - cornerHeight * 2);
                leftEdge.setPosition(0, cornerHeight);
                leftEdge.setTexture(guiAppState.getApplication().getAssetManager(), edgeTexture, true);
                mainPicture.attachChild(leftEdge);

                Picture rightEdge = new Picture("rightEdge");
                Utils.rotatePicture90(rightEdge);
                rightEdge.setWidth(edgeWidth);
                rightEdge.setHeight(height - cornerHeight * 2);
                rightEdge.setPosition(width - cornerWidth, cornerHeight);
                rightEdge.setTexture(guiAppState.getApplication().getAssetManager(), edgeTexture, true);
                mainPicture.attachChild(rightEdge);
            }
        }
    }

    public Texture2D getTexture2D(String name) {
        try {
            ResourceManager resourceManager = ((GameWindow) guiAppState.getApplication()).getGameContext().getResourceManager();
            Map<ResourceType, ResourceRef> foundResources = resourceManager.getResourceRefByName(name.toLowerCase());
            if (foundResources == null) {
                System.out.println("Cannot find texture ["+name+"]");
                foundResources=resourceManager.getResourceRefByName("inone");
            }
            ResourceRef resourceRef = foundResources.get(ResourceType.TPC);
             if (resourceRef == null) {
                throw new IllegalArgumentException("Cannot find texture ["+name+"]");
            }
            Texture2D texture2d = Utils.textureFromImage((BufferedImage) resourceManager.getConvertedResource(resourceRef), name);
            return texture2d;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error while loading texture [" + name + "]", ex);
        }
    }

    public Texture2D getEmptyTexture() {
        if (emptyTexture == null) {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(0, 0, 1, 1);
            g.dispose();
            emptyTexture = Utils.textureFromImage(image, "emptyTexture");
        }

        return emptyTexture;
    }
    
    
}
