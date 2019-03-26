package com.ncsdcmp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sad
 */
class DecompilerStack {

    private int currentTopOffset = 0;
    private List<StackElement> stackElements = new ArrayList<>();

    public StackElement getStackElement(int offset) {
        int position = 0;
        for (int i = stackElements.size() - 1; i >= 0; i--) {
            StackElement el = stackElements.get(i);
            position += el.size;
            if (position == offset) {
                return el;
            }
            if (position > offset) {
                throw new IllegalStateException("Cannot get stack element on offset [" + offset + "] for stack of size [" + currentTopOffset + "]. element starts on offset " + position);
            }
        }
        throw new IllegalStateException("Cannot find element at offset [" + offset + "] on stack with top [" + currentTopOffset + "]");
    }

    public void removeElementsFromStack(int size) {
        StackElement el = getStackElement(size);
        int index = stackElements.indexOf(el);
        int elementsToRemove = stackElements.size() - 1 - index;
        for (int i = 0; i < elementsToRemove; i++) {
            stackElements.remove(index);
        }
    }

    
    
    public void push(StackElement element) {
        stackElements.add(element);
        currentTopOffset += element.size;
    }

    public static enum StackElementType {
        INT, FLOAT, STRING, OBJECT
    }

    public static class StackElement {

        Object value;
        int size;
    };
}
