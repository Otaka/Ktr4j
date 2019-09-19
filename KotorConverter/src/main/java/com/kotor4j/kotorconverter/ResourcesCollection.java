package com.kotor4j.kotorconverter;

import com.kotor4j.kotorconverter.original.chitinkey.ResourceType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class ResourcesCollection {

    /**
     * Resource_Name->(ResType->ResourceRef)
     */
    private Map<String, Map<ResourceType, ResourceRef>> resources = new HashMap<>();

    public ResourcesCollection() {

    }

    public ResourcesCollection(List<ResourceRef> resources) {
        for (ResourceRef r : resources) {
            add(r);
        }
    }

    public void add(ResourceRef resource) {
        Map<ResourceType, ResourceRef> resourceWithType = resources.get(resource.getName());
        if (resourceWithType == null) {
            resourceWithType = new HashMap<>();
            resources.put(resource.getName(), resourceWithType);
        }

        resourceWithType.put(resource.getResourceType(), resource);
    }

    public ResourceRef getResource(String resourceName, ResourceType resourceType) {
        Map<ResourceType, ResourceRef> rt2r = resources.get(resourceName);
        if (rt2r == null) {
            return null;
        }

        return rt2r.get(resourceType);
    }
    
    public List<ResourceRef>collectedResources(){
        List<ResourceRef>collectedResources=new ArrayList<>();
        for(Map<ResourceType, ResourceRef>mapResourceTypeToResource:resources.values()){
            for(ResourceRef r:mapResourceTypeToResource.values()){
                collectedResources.add(r);
            }
        }

        return collectedResources;
    }
}
