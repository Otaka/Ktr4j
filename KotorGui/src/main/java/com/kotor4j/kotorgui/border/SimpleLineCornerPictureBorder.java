package com.kotor4j.kotorgui.border;

import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.kotor4j.kotorgui.components.AbstractComponent;
import com.kotor4j.utils.PictureTextureUtils;

/**
 * @author Dmitry This border expects two images: edge(top edge) and corner(left
 * top corner), that will be copied to all other edges and corners<br/>
 * they should have square shape, and the same sizes
 */
public class SimpleLineCornerPictureBorder extends AbstractBorder {

    private Texture2D leftTopCornerTexture;
    private Texture2D topEdgeTexture;
    private float widthHeight;
    private Picture topEdge;
    private Picture bottomEdge;
    private Picture leftEdge;
    private Picture rightEdge;
    private Picture leftTopCorner;
    private Picture rightTopCorner;
    private Picture leftBottomCorner;
    private Picture rightBottomCorner;

    public SimpleLineCornerPictureBorder(Texture2D leftTopCornerTexture, Texture2D topEdgeTexture) {
        this.leftTopCornerTexture = leftTopCornerTexture;
        this.topEdgeTexture = topEdgeTexture;
        if (leftTopCornerTexture.getImage().getWidth() != leftTopCornerTexture.getImage().getHeight()) {
            throw new IllegalArgumentException("LeftTopCornerTexture [" + leftTopCornerTexture.getName() + "] does not square shape");
        }

        if (topEdgeTexture.getImage().getWidth() != topEdgeTexture.getImage().getHeight()) {
            throw new IllegalArgumentException("TopEdgeTexture [" + leftTopCornerTexture.getName() + "] does not square shape");
        }

        if (topEdgeTexture.getImage().getWidth() != leftTopCornerTexture.getImage().getWidth()) {
            throw new IllegalArgumentException("Size of TopEdgeTexture [" + leftTopCornerTexture.getName() + "] does not equal to LeftTopCornerTexture");
        }

        widthHeight = topEdgeTexture.getImage().getWidth();
    }

    @Override
    public float getLeftWidth() {
        return widthHeight;
    }

    @Override
    public float getBottomHeight() {
        return widthHeight;
    }

    @Override
    public float getRightWidth() {
        return widthHeight;
    }

    @Override
    public float getTopHeight() {
        return widthHeight;
    }

    @Override
    public void applyBorderToComponent(AbstractComponent component) {
        super.applyBorderToComponent(component);
        
        Picture _topEdge = new Picture("topEdge");
        _topEdge.setTexture(component.getAssetManager(), topEdgeTexture, true);
        _topEdge.setWidth(component.getWidth() - widthHeight * 2);
        _topEdge.setHeight(widthHeight);
        _topEdge.setPosition(widthHeight, component.getHeight() - widthHeight);
        component.getComponentNode().attachChild(_topEdge);

        Picture _bottomEdge = new Picture("bottomEdge");
        PictureTextureUtils.mirrorPictureVertically(_bottomEdge);
        _bottomEdge.setTexture(component.getAssetManager(), topEdgeTexture, true);
        _bottomEdge.setWidth(component.getWidth() - widthHeight * 2);
        _bottomEdge.setHeight(widthHeight);
        _bottomEdge.setPosition(widthHeight, 0);
        component.getComponentNode().attachChild(_bottomEdge);

        Picture _rightEdge = new Picture("rightEdge");
        PictureTextureUtils.rotatePicture90(_rightEdge);
        _rightEdge.setTexture(component.getAssetManager(), topEdgeTexture, true);
        _rightEdge.setWidth(widthHeight);
        _rightEdge.setHeight(component.getHeight() - widthHeight * 2);
        _rightEdge.setPosition(component.getWidth() - widthHeight, widthHeight);
        component.getComponentNode().attachChild(_rightEdge);

        Picture _leftEdge = new Picture("leftEdge");
        PictureTextureUtils.rotatePicture270(_leftEdge);
        _leftEdge.setTexture(component.getAssetManager(), topEdgeTexture, true);
        _leftEdge.setWidth(widthHeight);
        _leftEdge.setHeight(component.getHeight() - widthHeight * 2);
        _leftEdge.setPosition(0, widthHeight);
        component.getComponentNode().attachChild(_leftEdge);

        Picture _leftTopCorner = new Picture("leftTopCorner");
        _leftTopCorner.setTexture(component.getAssetManager(), leftTopCornerTexture, true);
        _leftTopCorner.setWidth(widthHeight);
        _leftTopCorner.setHeight(widthHeight);
        _leftTopCorner.setPosition(0, component.getHeight() - widthHeight);
        component.getComponentNode().attachChild(_leftTopCorner);
        
        Picture _rightTopCorner = new Picture("rightTopCorner");
        PictureTextureUtils.mirrorPictureHorizontally(_rightTopCorner);
        _rightTopCorner.setTexture(component.getAssetManager(), leftTopCornerTexture, true);
        _rightTopCorner.setWidth(widthHeight);
        _rightTopCorner.setHeight(widthHeight);
        _rightTopCorner.setPosition(component.getWidth()-widthHeight, component.getHeight() - widthHeight);
        component.getComponentNode().attachChild(_rightTopCorner);

        Picture _leftBottomCorner = new Picture("leftBottomCorner");
        PictureTextureUtils.mirrorPictureVertically(_leftBottomCorner);
        _leftBottomCorner.setTexture(component.getAssetManager(), leftTopCornerTexture, true);
        _leftBottomCorner.setWidth(widthHeight);
        _leftBottomCorner.setHeight(widthHeight);
        _leftBottomCorner.setPosition(0, 0);
        component.getComponentNode().attachChild(_leftBottomCorner);
        
        Picture _rightBottomCorner = new Picture("rightBottomCorner");
        PictureTextureUtils.mirrorPictureVertically(_rightBottomCorner);
        PictureTextureUtils.mirrorPictureHorizontally(_rightBottomCorner);
        _rightBottomCorner.setTexture(component.getAssetManager(), leftTopCornerTexture, true);
        _rightBottomCorner.setWidth(widthHeight);
        _rightBottomCorner.setHeight(widthHeight);
        _rightBottomCorner.setPosition(component.getWidth()-widthHeight, 0);
        component.getComponentNode().attachChild(_rightBottomCorner);
    }

    
}
