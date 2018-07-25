package com.kotor4j.resourcemanager.chitinkey;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry
 */
public class ChitinKey {

    private final KeyResource[] keyResources;
    private final BifShort[] bifs;
    private final Map<Integer, KeyResource> resourcesMap = new HashMap<>();
    private String version;
    private int buildYear;
    private int buildDay;

    public ChitinKey(KeyResource[] keyResources, BifShort[] biffs) {
        this.keyResources = keyResources;
        this.bifs = biffs;
        for (KeyResource res : keyResources) {
            resourcesMap.put(res.getResId(), res);
        }
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setBuildDay(int buildDay) {
        this.buildDay = buildDay;
    }

    public void setBuildYear(int buildYear) {
        this.buildYear = buildYear;
    }

    public BifShort[] getBifs() {
        return bifs;
    }

    public int getBuildDay() {
        return buildDay;
    }

    public int getBuildYear() {
        return buildYear;
    }

    public KeyResource getResourceByResId(int resId) {
        return resourcesMap.get(resId);
    }

    public KeyResource[] getKeyResources() {
        return keyResources;
    }
}
