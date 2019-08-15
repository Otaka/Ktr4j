package com.kotor4j.jmdynamicfont;

import com.kotor4j.jmdynamicfont.ShelfRectPacker.Rect;


/**
 * @author sad
 */
public class CharInfo {

    private char character;
    private Rect positionInTexture;

    public CharInfo(char character, Rect positionInTexture) {
        this.character = character;
        this.positionInTexture = positionInTexture;
    }

    public char getCharacter() {
        return character;
    }

    public Rect getPositionInTexture() {
        return positionInTexture;
    }

}
