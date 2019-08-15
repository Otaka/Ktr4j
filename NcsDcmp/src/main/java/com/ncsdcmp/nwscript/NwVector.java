package com.ncsdcmp.nwscript;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sad
 */
public class NwVector {

    private List<Float> values = new ArrayList<>();

    public List<Float> getValues() {
        return values;
    }

    public void addValue(float value) {
        values.add(value);
    }

    @Override
    public String toString() {
        return values.toString();
    }
}
