package com.mathlayout.expressions;

import com.mathlayout.ComponentInfo;
import com.mathlayout.DependentFieldsRecalculator;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.mathlayout.ComponentInfo.X1;
import static com.mathlayout.ComponentInfo.X2;
import static com.mathlayout.ComponentInfo.Y1;
import static com.mathlayout.ComponentInfo.Y2;
import static com.mathlayout.ComponentInfo.WIDTH;
import static com.mathlayout.ComponentInfo.HEIGHT;
import static com.mathlayout.ComponentInfo.HCENTER;
import static com.mathlayout.ComponentInfo.VCENTER;
import static com.mathlayout.ComponentInfo.BASELINE;

/**
 *
 * @author Dmitry
 */
public class ComponentInfoTest {

    @Test
    public void testFixedField() {
        ComponentInfo ci = new ComponentInfo("testComponent", null) {
            @Override
            public int getBaseline(int width, int height) {
                return 10;
            }
        };
        ci.setFixedFields(new int[]{WIDTH, HEIGHT}, new int[]{45, 23});
        assertFalse(ci.isSetField(X1));
        assertFalse(ci.isSetField(X2));
        assertFalse(ci.isSetField(Y1));
        assertFalse(ci.isSetField(Y2));
        assertEquals(45, ci.getValue(WIDTH));
        assertEquals(23, ci.getValue(HEIGHT));

        //after clear, fixed fields should still have same values
        ci.clearData();
        assertFalse(ci.isSetField(X1));
        assertFalse(ci.isSetField(X2));
        assertFalse(ci.isSetField(Y1));
        assertFalse(ci.isSetField(Y2));
        assertEquals(45, ci.getValue(WIDTH));
        assertEquals(23, ci.getValue(HEIGHT));
    }

    @Test
    public void testSetValue() {
        DependentFieldsRecalculator recalculator = new DependentFieldsRecalculator();
        ComponentInfo ci = new ComponentInfo("testComponent", null) {
            @Override
            public int getBaseline(int width, int height) {
                return 10;
            }
        };
        ci.setValue(X1, 10);
        recalculator.calculateDependentFields(ci, X1);
        assertEquals(10, ci.getValue(X1));
        assertFalse(ci.isSetField(X2));
        assertFalse(ci.isSetField(WIDTH));

        ci.setValue(WIDTH, 5);
        recalculator.calculateDependentFields(ci, WIDTH);
        assertEquals(10, ci.getValue(X1));
        assertEquals(5, ci.getValue(WIDTH));
        assertEquals(15, ci.getValue(X2));

        ci.setValue(Y1, 10);
        recalculator.calculateDependentFields(ci, Y1);
        assertEquals(10, ci.getValue(Y1));
        assertFalse(ci.isSetField(Y2));
        assertFalse(ci.isSetField(HEIGHT));

        ci.setValue(HEIGHT, 5);
        recalculator.calculateDependentFields(ci, HEIGHT);
        assertEquals(10, ci.getValue(Y1));
        assertEquals(5, ci.getValue(HEIGHT));
        assertEquals(15, ci.getValue(Y2));

        ci.clearData();
        assertFalse(ci.isSetField(X1));
        assertFalse(ci.isSetField(X2));
        assertFalse(ci.isSetField(Y1));
        assertFalse(ci.isSetField(Y2));
        assertFalse(ci.isSetField(WIDTH));
        assertFalse(ci.isSetField(HEIGHT));

        ci.setValue(X1, 10);
        ci.setValue(X2, 25);
        recalculator.calculateDependentFields(ci, X1);
        recalculator.calculateDependentFields(ci, X2);
        assertEquals(10, ci.getValue(X1));
        assertEquals(15, ci.getValue(WIDTH));
        assertEquals(25, ci.getValue(X2));

        ci.setValue(Y1, 10);
        ci.setValue(Y2, 25);
        recalculator.calculateDependentFields(ci, Y1);
        recalculator.calculateDependentFields(ci, Y2);
        assertEquals(10, ci.getValue(Y1));
        assertEquals(15, ci.getValue(HEIGHT));
        assertEquals(25, ci.getValue(Y2));

        ci.clearData();
        ci.setValue(Y2, 25);
        ci.setValue(Y1, 10);
        recalculator.calculateDependentFields(ci, Y2);
        recalculator.calculateDependentFields(ci, Y1);
        assertEquals(10, ci.getValue(Y1));
        assertEquals(15, ci.getValue(HEIGHT));
        assertEquals(25, ci.getValue(Y2));
        assertEquals(17, ci.getValue(VCENTER));
        assertEquals(25, ci.getValue(BASELINE));

        ci.setValue(X2, 25);
        ci.setValue(X1, 10);
        recalculator.calculateDependentFields(ci, X2);
        recalculator.calculateDependentFields(ci, X1);
        assertEquals(10, ci.getValue(X1));
        assertEquals(15, ci.getValue(WIDTH));
        assertEquals(25, ci.getValue(X2));
        assertEquals(17, ci.getValue(HCENTER));

        ci.clearData();
        ci.setValue(X2, 30);
        ci.setValue(WIDTH, 10);
        recalculator.calculateDependentFields(ci, X2);
        recalculator.calculateDependentFields(ci, WIDTH);
        assertEquals(20, ci.getValue(X1));
        assertEquals(25, ci.getValue(HCENTER));

    }
}
