package com.kotor4j.kotorconverter.original.walkmesh;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Dmitry
 */
public enum WalkType {

    Dirt(1), Obscuring(2), Grass(3), Stone(4), Wood(5), Water(6), NonWalk(7), Transparent(8), Carpet(9), Metal(10), Puddles(11), Mud(13), DeepWater(17), NonWalkGrass(19);

    private int code;

    private WalkType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    private static TIntObjectMap<WalkType> codeToWalkType = new TIntObjectHashMap<>();

    static {
        for (WalkType wt : values()) {
            codeToWalkType.put(wt.getCode(), wt);
        }
    }

    public static WalkType getWalkType(int code) {
        return codeToWalkType.get(code);
    }
}
