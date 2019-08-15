package com.kotor4j.kotorconverter.original.gff;

import java.util.*;

/**
 * @author sad
 */
class ListIndicies {

    private List<int[]> indicies = new ArrayList<>();

    private HashMap<Integer, int[]> byteOffsetToIndiciesArray = new HashMap<>();

    public ListIndicies() {
    }

    public void add1(int byteOffset, int[] indicies) {
        byteOffsetToIndiciesArray.put(byteOffset, indicies);
        this.indicies.add(indicies);
    }

    public int[] getIndicies1(int byteOffset) {
        return byteOffsetToIndiciesArray.get(byteOffset);
    }

}
