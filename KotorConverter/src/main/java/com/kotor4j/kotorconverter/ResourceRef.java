package com.kotor4j.kotorconverter;

import com.kotor4j.kotorconverter.original.chitinkey.ResourceType;
import java.io.File;
import java.util.Objects;

/**
 * @author Dmitry
 */
public class ResourceRef {

    private String name;
    private ResourceType resourceType;
    private File containerFile;
    private int position;
    private int length;
    /**
     * if this resource is not part of the container, but just regular file
     */
    private boolean wholeFile;

    public ResourceRef(String name, ResourceType resourceType, File containerFile, int position, int length, boolean wholeFile) {
        this.name = name;
        this.resourceType = resourceType;
        this.containerFile = containerFile;
        this.position = position;
        this.length = length;
        this.wholeFile = wholeFile;
    }

    public boolean isWholeFile() {
        return wholeFile;
    }

    public File getContainerFile() {
        return containerFile;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.resourceType);
        hash = 97 * hash + Objects.hashCode(this.containerFile);
        hash = 97 * hash + this.position;
        hash = 97 * hash + (this.wholeFile ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceRef other = (ResourceRef) obj;
        if (this.position != other.position) {
            return false;
        }
        if (this.wholeFile != other.wholeFile) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (this.resourceType != other.resourceType) {
            return false;
        }
        if (!Objects.equals(this.containerFile, other.containerFile)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name+"."+resourceType;
    }

    
}
