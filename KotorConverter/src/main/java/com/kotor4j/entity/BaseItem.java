package com.kotor4j.entity;

import com.kotor4j.annotations.FieldTag;

/**
 * @author Dmitry
 */
public abstract class BaseItem extends BaseEntity{
    @FieldTag(fieldIndex = 1)
    private String title;
    @FieldTag(fieldIndex = 2)
    private String description;
    @FieldTag(fieldIndex = 3)
    private String iconPath;
    @FieldTag(fieldIndex = 4)
    private String tag;
    @FieldTag(fieldIndex = 5)
    private int price;
    @FieldTag(fieldIndex = 6)
    private boolean questItem;
    @FieldTag(fieldIndex = 7)
    private String onActivateScript;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isQuestItem() {
        return questItem;
    }

    public void setQuestItem(boolean questItem) {
        this.questItem = questItem;
    }

    public String getOnActivateScript() {
        return onActivateScript;
    }

    public void setOnActivateScript(String onActivateScript) {
        this.onActivateScript = onActivateScript;
    }
}
