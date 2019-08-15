package com.mathlayout;

import static com.mathlayout.ComponentInfo.BASELINE;
import static com.mathlayout.ComponentInfo.HCENTER;
import static com.mathlayout.ComponentInfo.HEIGHT;
import static com.mathlayout.ComponentInfo.VCENTER;
import static com.mathlayout.ComponentInfo.WIDTH;
import static com.mathlayout.ComponentInfo.X1;
import static com.mathlayout.ComponentInfo.X2;
import static com.mathlayout.ComponentInfo.Y1;
import static com.mathlayout.ComponentInfo.Y2;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author Dmitry
 */
public class DependentFieldsRecalculator {

    private List<ReactiveFieldExpression> reactiveFieldExpressions = new ArrayList<>();
    private Queue<Integer> tempRelatedFields = new ArrayDeque<>();
    private Map<Integer, List<ReactiveFieldExpression>> fieldToListOfDependentExpressions = new HashMap<>();

    public DependentFieldsRecalculator() {
        addReactiveFieldExpression(X1, (ci) -> {
            return ci.getValue(X2) - ci.getValue(WIDTH);
        }, X2, WIDTH);
        addReactiveFieldExpression(X1, (ci) -> {
            return ci.getValue(X2) - (ci.getValue(X2) - ci.getValue(HCENTER)) * 2;
        }, X2, HCENTER);
        addReactiveFieldExpression(X1, (ci) -> {
            return ci.getValue(HCENTER) - ci.getValue(WIDTH) / 2;
        }, WIDTH, HCENTER);

        addReactiveFieldExpression(X2, (ci) -> {
            return ci.getValue(X1) + ci.getValue(WIDTH);
        }, X1, WIDTH);
        addReactiveFieldExpression(X2, (ci) -> {
            return ci.getValue(X1) + (ci.getValue(HCENTER) - ci.getValue(X1)) * 2;
        }, X1, HCENTER);
        addReactiveFieldExpression(X2, (ci) -> {
            return ci.getValue(HCENTER) + ci.getValue(WIDTH) / 2;
        }, WIDTH, HCENTER);

        addReactiveFieldExpression(WIDTH, (ci) -> {
            return ci.getValue(X2) - ci.getValue(X1);
        }, X1, X2);
        addReactiveFieldExpression(WIDTH, (ci) -> {
            return (ci.getValue(HCENTER) - ci.getValue(X1)) * 2;
        }, X1, HCENTER);
        addReactiveFieldExpression(WIDTH, (ci) -> {
            return (ci.getValue(X2) - ci.getValue(HCENTER)) * 2;
        }, X2, HCENTER);

        addReactiveFieldExpression(HCENTER, (ci) -> {
            return (ci.getValue(X1) + ci.getValue(X2)) / 2;
        }, X1, X2);
        addReactiveFieldExpression(HCENTER, (ci) -> {
            return ci.getValue(X1) + ci.getValue(WIDTH) / 2;
        }, X1, WIDTH);
        addReactiveFieldExpression(HCENTER, (ci) -> {
            return ci.getValue(X2) + ci.getValue(WIDTH) / 2;
        }, X2, WIDTH);

        addReactiveFieldExpression(Y1, (ci) -> {
            return ci.getValue(Y2) - ci.getValue(HEIGHT);
        }, Y2, HEIGHT);
        addReactiveFieldExpression(Y1, (ci) -> {
            return ci.getValue(Y2) - (ci.getValue(Y2) - ci.getValue(VCENTER)) * 2;
        }, Y2, VCENTER);
        addReactiveFieldExpression(Y1, (ci) -> {
            return ci.getValue(VCENTER) - ci.getValue(HEIGHT) / 2;
        }, HEIGHT, VCENTER);
        addReactiveFieldExpression(Y1, (ci) -> {
            int height = ci.getValue(HEIGHT);
            int baseline = ci.getValue(BASELINE);
            int baselineOffset = ci.getBaseline(10, height);
            if (baselineOffset == -1) {
                baselineOffset = height;
            }
            return baseline - baselineOffset;
        }, BASELINE, HEIGHT);

        addReactiveFieldExpression(Y2, (ci) -> {
            return ci.getValue(Y1) + ci.getValue(HEIGHT);
        }, Y1, HEIGHT);
        addReactiveFieldExpression(Y2, (ci) -> {
            return ci.getValue(Y1) + (ci.getValue(VCENTER) - ci.getValue(Y1)) * 2;
        }, Y1, VCENTER);
        addReactiveFieldExpression(Y2, (ci) -> {
            return ci.getValue(VCENTER) + ci.getValue(HEIGHT) / 2;
        }, HEIGHT, VCENTER);
        addReactiveFieldExpression(Y2, (ci) -> {
            int height = ci.getValue(HEIGHT);
            int baseline = ci.getValue(BASELINE);
            int baselineOffset = ci.getBaseline(10, height);
            if (baselineOffset == -1) {
                baselineOffset = height;
            }
            return baseline - baselineOffset + height;
        }, BASELINE, HEIGHT);

        addReactiveFieldExpression(VCENTER, (ci) -> {
            return (ci.getValue(Y1) + ci.getValue(Y2)) / 2;
        }, Y1, Y2);

        addReactiveFieldExpression(HEIGHT, (ci) -> {
            return ci.getValue(Y2) - ci.getValue(Y1);
        }, Y1, Y2);

        addReactiveFieldExpression(BASELINE, (ci) -> {
            int height = ci.getValue(Y2) - ci.getValue(Y1);
            int baselineOffset = ci.getBaseline(10, height);
            if (baselineOffset == -1) {
                baselineOffset = height;
            }
            return ci.getValue(Y1)+baselineOffset;
        }, Y1, Y2);
    }

    private void addReactiveFieldExpression(int destField, IntMethod method, int... usedFields) {
        BitSet bs = new BitSet(ComponentInfo.FIELDS_COUNT);
        for (int field : usedFields) {
            bs.set(field);
        }

        int mask = (int) bs.toLongArray()[0];
        ReactiveFieldExpression fieldExpression = new ReactiveFieldExpression(destField, mask, method);

        for (int field : usedFields) {
            List<ReactiveFieldExpression> list = fieldToListOfDependentExpressions.get(field);
            if (list == null) {
                list = new ArrayList<>();
                fieldToListOfDependentExpressions.put(field, list);
            }
            list.add(fieldExpression);
        }
    }

    public void calculateDependentFields(ComponentInfo ci, int changedFieldId) {
        tempRelatedFields.clear();
        tempRelatedFields.offer(changedFieldId);
        while (!tempRelatedFields.isEmpty()) {
            int fieldId = tempRelatedFields.poll();
            List<ReactiveFieldExpression> expressions = fieldToListOfDependentExpressions.get(fieldId);
            IntBitSet fieldsWithValues = ci.getFieldsWithValues();
            for (ReactiveFieldExpression expression : expressions) {
                //check if all required fields are present
                if ((fieldsWithValues.getBuffer() & expression.getMask()) == expression.getMask()) {
                    if (fieldsWithValues.get(expression.getDestFieldId()) == false) {
                        int resultValue = expression.expression.process(ci);
                        ci.setValueWithoutChecks(expression.getDestFieldId(), resultValue);
                        tempRelatedFields.offer(expression.getDestFieldId());
                    }
                }
            }
        }
    }

    private interface IntMethod {

        public int process(ComponentInfo ci);
    }

    private static class ReactiveFieldExpression {

        private int destFieldId;
        private int mask;
        private IntMethod expression;

        public ReactiveFieldExpression(int destFieldId, int mask, IntMethod expression) {
            this.destFieldId = destFieldId;
            this.mask = mask;
            this.expression = expression;
        }

        public int getDestFieldId() {
            return destFieldId;
        }

        public IntMethod getExpression() {
            return expression;
        }

        public int getMask() {
            return mask;
        }
    }
}
