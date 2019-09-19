package com.mathlayout;

import com.mathlayout.IntBitSet;
import java.awt.Component;
import java.util.Arrays;

/**
 * @author Dmitry
 */
public class ComponentInfo {

    public static final int FIELDS_COUNT = 9;
    private String componentId;
    public static final int X1 = 0;
    public static final int Y1 = 1;
    public static final int X2 = 2;
    public static final int Y2 = 3;
    public static final int WIDTH = 4;
    public static final int HEIGHT = 5;
    public static final int HCENTER = 6;
    public static final int VCENTER = 7;
    public static final int BASELINE = 8;

    private static final String fieldNames[] = new String[]{"X1", "Y1", "X2", "Y2", "WIDTH", "HEIGHT", "HCENTER", "VCENTER", "BASELINE"};
    private int[] values = new int[FIELDS_COUNT];
    private IntBitSet fixedFields = new IntBitSet();
    private IntBitSet fieldsWithValues = new IntBitSet();
    private Component component;

    public static String getFieldNameById(int fieldId) {
        return fieldNames[fieldId];
    }

    public IntBitSet getFieldsWithValues() {
        return fieldsWithValues;
    }

    public static int getFieldIdByFieldName(String fieldName) {
        fieldName = fieldName.toUpperCase();
        for (int i = 0; i < fieldNames.length; i++) {
            if (fieldName.equals(fieldNames[i])) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown field name [" + fieldName + "]. Expected names:" + Arrays.deepToString(fieldNames));
    }

    public int getBaseline(int width, int height){
        return component.getBaseline(width, height);
    }
    
    public ComponentInfo(String componentId, Component component) {
        this.componentId = componentId;
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public String getComponentId() {
        return componentId;
    }

    public void clearData() {
        for (int i = 0; i < FIELDS_COUNT; i++) {
            if (!fixedFields.get(i)) {
                fieldsWithValues.clear(i);
                values[i] = 0;
            }
        }
    }

    public void setValue(int fieldId, int value) {
        fieldsWithValues.set(fieldId);
        values[fieldId] = value;
        /*fieldsWithValues.set(fieldId);
        values[fieldId] = value;
        //set value of field, and automatically set dependend fields, if it is possible
        switch (fieldId) {
            case X1: {
                if (isSetField(WIDTH)) {
                    int tx2 = value + values[WIDTH];
                    if (isSetField(X2)) {
                        if (tx2 != values[X2]) {
                            throw new LayoutException("You provided X1 to component [" + componentId + "], but WIDTH is set[" + values[WIDTH] + "], and X2 is set[" + values[X2] + "], but this X1+WIDTH!=X2");
                        }
                    } else {
                        setValueWithoutChecks(X1, value);
                        setValueWithoutChecks(X2, values[X1] + values[WIDTH]);
                    }
                } else if (isSetField(X2)) {
                    //case with X2 set and WIDTH is set already checked in previous branch
                    setValueWithoutChecks(X1, value);
                    setValueWithoutChecks(WIDTH, values[X2] - values[X1]);
                } else {
                    setValueWithoutChecks(X1, value);
                }
                break;
            }
            case Y1: {
                if (isSetField(HEIGHT)) {
                    int ty2 = value + values[HEIGHT];
                    if (isSetField(Y2)) {
                        if (ty2 != values[Y2]) {
                            throw new LayoutException("You provided Y1 to component [" + componentId + "], but HEIGHT is set[" + values[HEIGHT] + "], and Y2 is set[" + values[Y2] + "], but this Y1+HEIGHT!=Y2");
                        }
                    } else {
                        setValueWithoutChecks(Y1, value);
                        setValueWithoutChecks(Y2, values[Y1] + values[HEIGHT]);
                    }
                } else if (isSetField(Y2)) {
                    //case with Y2 set and HEIGHT is set already checked in previous branch
                    setValueWithoutChecks(Y1, value);
                    setValueWithoutChecks(HEIGHT, values[Y2] - values[Y1]);
                } else {
                    setValueWithoutChecks(Y1, value);
                }
                break;
            }
            case X2: {
                if (isSetField(WIDTH)) {
                    int tx1 = value - values[WIDTH];
                    if (isSetField(X1)) {
                        if (tx1 != values[X1]) {
                            throw new LayoutException("You provided X2 to component [" + componentId + "], but WIDTH is set[" + values[WIDTH] + "], and X1 is set[" + values[X1] + "], but this X1+WIDTH!=X2");
                        }
                    } else {
                        setValueWithoutChecks(X2, value);
                        setValueWithoutChecks(X1, values[X2] - values[WIDTH]);
                    }
                } else if (isSetField(X1)) {
                    //case with X1 set and WIDTH is set already checked in previous branch
                    setValueWithoutChecks(X2, value);
                    setValueWithoutChecks(WIDTH, values[X2] - values[X1]);
                } else {
                    setValueWithoutChecks(X2, value);
                }
                break;
            }
            case Y2: {
                if (isSetField(HEIGHT)) {
                    int ty1 = value - values[HEIGHT];
                    if (isSetField(Y1)) {
                        if (ty1 != values[Y1]) {
                            throw new LayoutException("You provided Y2 to component [" + componentId + "], but HEIGHT is set[" + values[HEIGHT] + "], and Y1 is set[" + values[Y1] + "], but this Y1+HEIGHT!=Y2");
                        }
                    } else {
                        setValueWithoutChecks(Y2, value);
                        setValueWithoutChecks(Y1, values[Y2] - values[HEIGHT]);
                    }
                } else if (isSetField(Y1)) {
                    //case with Y1 set and HEIGHT is set already checked in previous branch
                    setValueWithoutChecks(Y2, value);
                    setValueWithoutChecks(HEIGHT, values[Y2] - values[Y1]);
                } else {
                    setValueWithoutChecks(Y2, value);
                }
                break;
            }
            case WIDTH: {
                if (isSetField(X1)) {
                    int tx2 = value + values[X1];
                    if (isSetField(X2)) {
                        if (tx2 != values[X2]) {
                            throw new LayoutException("You provided WIDTH to component [" + componentId + "], but X1 is set[" + values[X1] + "], and X2 is set[" + values[X2] + "], but this X1+WIDTH!=X2");
                        }
                    } else {
                        setValueWithoutChecks(WIDTH, value);
                        setValueWithoutChecks(X2, values[X1] + values[WIDTH]);
                    }
                } else if (isSetField(X2)) {
                    //case with X2 set and X1 is set, already checked in previous branch
                    setValueWithoutChecks(WIDTH, value);
                    setValueWithoutChecks(X1, values[X2] - values[WIDTH]);
                } else {
                    setValueWithoutChecks(WIDTH, value);
                }
                break;
            }
            case HEIGHT: {
                if (isSetField(Y1)) {
                    int ty2 = value + values[Y1];
                    if (isSetField(Y2)) {
                        if (ty2 != values[Y2]) {
                            throw new LayoutException("You provided HEIGHT to component [" + componentId + "], but Y1 is set[" + values[Y1] + "], and Y2 is set[" + values[Y2] + "], but this Y1+HEIGHT!=Y2");
                        }
                    } else {
                        setValueWithoutChecks(HEIGHT, value);
                        setValueWithoutChecks(Y2, values[Y1] + values[HEIGHT]);
                    }
                } else if (isSetField(Y2)) {
                    //case with Y2 set and Y1 is set, already checked in previous branch
                    setValueWithoutChecks(HEIGHT, value);
                    setValueWithoutChecks(Y1, values[Y2] - values[HEIGHT]);
                } else {
                    setValueWithoutChecks(HEIGHT, value);
                }
                break;
            }
        }*/
    }

    public int getValue(int fieldId) {
        if (isSetField(fieldId)) {
            return values[fieldId];
        }
        throw new IllegalArgumentException("Field [" + fieldNames[fieldId] + "] is not set in component [" + componentId + "]");
    }

    public void setValueWithoutChecks(int fieldId, int value) {
        fieldsWithValues.set(fieldId);
        values[fieldId] = value;
    }

    public boolean isSetField(int fieldId) {
        return fieldsWithValues.get(fieldId);
    }

    public void setFixedFields(int[] fields, int[] values) {
        fixedFields.clear();
        for (int i = 0; i < values.length; i++) {
            int fieldId = fields[i];
            int value = values[i];
            fixedFields.set(fieldId);
            setValueWithoutChecks(fieldId, value);
        }
    }

}
