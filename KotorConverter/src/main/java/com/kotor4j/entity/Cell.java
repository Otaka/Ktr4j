package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;
import com.kotor4j.annotations.RecordTag;
import com.kotor4j.kotorconverter.Constants;

/**
 * @author Dmitry
 */
@RecordTag(Constants.CELL)
public class Cell extends BaseEntity {

    @FieldTag(fieldIndex = 1)
    private String title;
    @FieldTag(fieldIndex = 2)
    private String tag;
    @FieldTag(fieldIndex = 3)
    private String mapTexture;
    @FieldTag(fieldIndex = 4)
    private float mapPoint1X;
    @FieldTag(fieldIndex = 5)
    private float mapPoint1Y;
    @FieldTag(fieldIndex = 6)
    private float mapPoint2X;
    @FieldTag(fieldIndex = 7)
    private float mapPoint2Y;
    @FieldTag(fieldIndex = 8)
    private float mapWorldPoint1X;
    @FieldTag(fieldIndex = 9)
    private float mapWorldPoint1Y;
    @FieldTag(fieldIndex = 10)
    private float mapWorldPoint2X;
    @FieldTag(fieldIndex = 11)
    private float mapWorldPoint2Y;
    @FieldTag(fieldIndex = 12)
    private boolean unescapable;
    @FieldTag(fieldIndex = 13)
    private String[] onEnterScriptRefId;
    @FieldTag(fieldIndex = 14)
    private String[] onExitScriptRefId;
    @FieldTag(fieldIndex = 15)
    private String onHeartbeatScriptRefId;
    @FieldTag(fieldIndex = 16)
    private String onUserDefinedEventScriptRefId;
    @FieldTag(fieldIndex = 17)
    private String onItemAquiredEventScriptRefId;
    @FieldTag(fieldIndex = 18)
    private String grassTextureName;

    public void setGrassTextureName(String grassTextureName) {
        this.grassTextureName = grassTextureName;
    }

    public String getGrassTextureName() {
        return grassTextureName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMapTexture() {
        return mapTexture;
    }

    public void setMapTexture(String mapTexture) {
        this.mapTexture = mapTexture;
    }

    public float getMapPoint1X() {
        return mapPoint1X;
    }

    public void setMapPoint1X(float mapPoint1X) {
        this.mapPoint1X = mapPoint1X;
    }

    public float getMapPoint1Y() {
        return mapPoint1Y;
    }

    public void setMapPoint1Y(float mapPoint1Y) {
        this.mapPoint1Y = mapPoint1Y;
    }

    public float getMapPoint2X() {
        return mapPoint2X;
    }

    public void setMapPoint2X(float mapPoint2X) {
        this.mapPoint2X = mapPoint2X;
    }

    public float getMapPoint2Y() {
        return mapPoint2Y;
    }

    public void setMapPoint2Y(float mapPoint2Y) {
        this.mapPoint2Y = mapPoint2Y;
    }

    public float getMapWorldPoint1X() {
        return mapWorldPoint1X;
    }

    public void setMapWorldPoint1X(float mapWorldPoint1X) {
        this.mapWorldPoint1X = mapWorldPoint1X;
    }

    public float getMapWorldPoint1Y() {
        return mapWorldPoint1Y;
    }

    public void setMapWorldPoint1Y(float mapWorldPoint1Y) {
        this.mapWorldPoint1Y = mapWorldPoint1Y;
    }

    public float getMapWorldPoint2X() {
        return mapWorldPoint2X;
    }

    public void setMapWorldPoint2X(float mapWorldPoint2X) {
        this.mapWorldPoint2X = mapWorldPoint2X;
    }

    public float getMapWorldPoint2Y() {
        return mapWorldPoint2Y;
    }

    public void setMapWorldPoint2Y(float mapWorldPoint2Y) {
        this.mapWorldPoint2Y = mapWorldPoint2Y;
    }

    public boolean isUnescapable() {
        return unescapable;
    }

    public void setUnescapable(boolean unescapable) {
        this.unescapable = unescapable;
    }

    public void setOnExitScriptRefId(String[] onExitScriptRefId) {
        this.onExitScriptRefId = onExitScriptRefId;
    }

    public void setOnEnterScriptRefId(String[] onEnterScriptRefId) {
        this.onEnterScriptRefId = onEnterScriptRefId;
    }

    public String[] getOnExitScriptRefId() {
        return onExitScriptRefId;
    }

    public String[] getOnEnterScriptRefId() {
        return onEnterScriptRefId;
    }

    public String getOnHeartbeatScriptRefId() {
        return onHeartbeatScriptRefId;
    }

    public void setOnHeartbeatScriptRefId(String onHeartbeatScriptRefId) {
        this.onHeartbeatScriptRefId = onHeartbeatScriptRefId;
    }

    public String getOnUserDefinedEventScriptRefId() {
        return onUserDefinedEventScriptRefId;
    }

    public void setOnUserDefinedEventScriptRefId(String onUserDefinedEventScriptRefId) {
        this.onUserDefinedEventScriptRefId = onUserDefinedEventScriptRefId;
    }

    public void setOnItemAquiredEventScriptRefId(String onItemAquiredEventScriptRefId) {
        this.onItemAquiredEventScriptRefId = onItemAquiredEventScriptRefId;
    }

    public String getOnItemAquiredEventScriptRefId() {
        return onItemAquiredEventScriptRefId;
    }

}
