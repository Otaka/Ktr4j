package com.kotor4j.kotorconverter;

import com.kotor4j.kotorconverter.original.chitinkey.ResourceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry
 */
public class ResourceType2ResourceListPair {

    private String fileType;
    private String file;
    private Map<ResourceType, List<ResourceRef>> resourceType2ResourceList = new HashMap<>();

    public ResourceType2ResourceListPair(String file, String fileType) {
        this.file = file;
        this.fileType = fileType;
    }

    public Set<ResourceType> getResourceTypes() {
        return resourceType2ResourceList.keySet();
    }

    public String getFile() {
        return file;
    }

    public String getFileType() {
        return fileType;
    }

    public void add(ResourceType rt, ResourceRef r) {
        List<ResourceRef> list = resourceType2ResourceList.get(rt);
        if (list == null) {
            list = new ArrayList<>();
            resourceType2ResourceList.put(rt, list);
        }
        list.add(r);
    }

    public List<ResourceRef> getResourcesForType(ResourceType rt) {
        return resourceType2ResourceList.get(rt);
    }

    @Override
    public String toString() {
        return file;
    }

}
