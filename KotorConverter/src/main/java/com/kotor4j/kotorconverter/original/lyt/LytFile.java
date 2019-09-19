package com.kotor4j.kotorconverter.original.lyt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class LytFile {

    private String fileDependency;
    private List<Map<String, Object>> rooms = new ArrayList<>();
    private List<Map<String, Object>> doorHooks = new ArrayList<>();
    private List<Map<String, Object>> trackObjects = new ArrayList<>();
    private List<Map<String, Object>> obstacleObjects = new ArrayList<>();

    public List<Map<String, Object>> getObstacleObjects() {
        return obstacleObjects;
    }

    public List<Map<String, Object>> getTrackObjects() {
        return trackObjects;
    }

    public List<Map<String, Object>> getRooms() {
        return rooms;
    }

    public List<Map<String, Object>> getDoorHooks() {
        return doorHooks;
    }

    public void setFileDependency(String fileDependency) {
        this.fileDependency = fileDependency;
    }

    public String getFileDependency() {
        return fileDependency;
    }

}
