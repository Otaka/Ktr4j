package com.mathlayout;

import com.mathlayout.expressions.ExpressionParser;
import com.mathlayout.expressions.LayoutException;
import com.mathlayout.expressions.MutableFloat;
import com.mathlayout.expressions.VariableValueHandler;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmitry
 */
public class MathLayout implements LayoutManager, LayoutManager2 {

    private List<ComponentInfo> components = new ArrayList<>();
    private Map<String, ComponentInfo> idToComponent = new HashMap<>();
    private ExpressionParser expressionParser;
    private Dimension size;
    private List<LayoutExpression> layoutExpressions = new ArrayList<>();
    private boolean processedFixedFields = false;
    private DependentFieldsRecalculator dependentFieldsRecalculator = new DependentFieldsRecalculator();
    private int rightPadding = 10;
    private int bottomPadding = 10;

    public MathLayout() {
        expressionParser = new ExpressionParser();
        expressionParser.addVariableValueHandler(createVariableHandler());
    }

    public void setRightPadding(int rightPadding) {
        this.rightPadding = rightPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public void addExpression(String... expressions) {
        try {
            for (String expr : expressions) {
                LayoutExpression layoutExpression = new LayoutExpression(null, expr, expressionParser.parseString(expr));
                layoutExpressions.add(layoutExpression);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Cannot parse expression [" + Arrays.deepToString(expressions) + "]", ex);
        }
    }

    public boolean hasVarialbe(String varName) {
        return expressionParser.hasVariable(varName);
    }

    public float getVariable(String varName) {
        return expressionParser.getVariable(varName);
    }

    public void createAndSetSimpleVariable(String varName, float value) {
        expressionParser.createAndSetSimpleVariable(varName, value);
    }

    public void setVariable(String varName, float value) {
        expressionParser.setVariable(varName, value);
    }

    @Override
    public void invalidateLayout(Container target) {
        for (ComponentInfo ci : components) {
            ci.clearData();
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        String id = (String) constraints;
        ComponentInfo ci = new ComponentInfo(id, comp);
        idToComponent.put(id, ci);
        components.add(ci);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        System.out.println("redundand addLayoutComponent");
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        String id = getComponentId(comp);
        components.remove(idToComponent.remove(id));
    }

    @Override
    public void layoutContainer(Container parent) {
        processFixedFields();
        for (LayoutExpression layoutExpression : layoutExpressions) {
            try {
                layoutExpression.getComputationAst().process();
            } catch (Exception ex) {
                throw new LayoutException("Error while process layout rule [" + layoutExpression.getRuleText() + "]", ex);
            }
        }

        int[] fieldsToCheck = new int[]{ComponentInfo.X1, ComponentInfo.Y1, ComponentInfo.WIDTH, ComponentInfo.HEIGHT};
        int maxY = 0;
        int maxX = 0;
        for (ComponentInfo ci : components) {
            List<String> notPresentFields = new ArrayList<>();
            for (int field : fieldsToCheck) {
                if (!ci.isSetField(field)) {
                    notPresentFields.add(ComponentInfo.getFieldNameById(field));
                }
            }
            if (!notPresentFields.isEmpty()) {
                throw new LayoutException("Cannot layout component [" + ci.getComponentId() + "], because necessary fields " + notPresentFields.toString() + " were not set");
            }

            Component component = ci.getComponent();
            int x1 = ci.getValue(ComponentInfo.X1);
            int y1 = ci.getValue(ComponentInfo.Y1);
            int width = ci.getValue(ComponentInfo.WIDTH);
            int height = ci.getValue(ComponentInfo.HEIGHT);
            int x2 = x1 + width;
            int y2 = y1 + height;
            component.setLocation(x1, y1);
            component.setSize(width, height);
            if (maxX < x2) {
                maxX = x2;
            }

            if (maxY < y2) {
                maxY = y2;
            }

            size = new Dimension(maxX + rightPadding, maxY + bottomPadding);
        }

    }

    private void processFixedFields() {
        if (processedFixedFields == true) {
            return;
        }
        processedFixedFields = true;
        for (ComponentInfo ci : components) {
            Component component = ci.getComponent();
            if (component.getPreferredSize() != null) {
                Dimension prefSize = component.getPreferredSize();
                int w = prefSize.width;
                int h = prefSize.height;
                ci.setFixedFields(new int[]{ComponentInfo.WIDTH, ComponentInfo.HEIGHT}, new int[]{w, h});
            }
        }
    }

    private VariableValueHandler createVariableHandler() {
        return new VariableValueHandler() {
            @Override
            public boolean getVariableValue(String variableName, MutableFloat result) {
                return internalGetVariableValue(variableName, result);
            }

            @Override
            public boolean setVariableValue(String variableName, float variableValue) {
                return internalSetVariableValue(variableName, variableValue);
            }

        };
    }

    private boolean internalGetVariableValue(String variableName, MutableFloat result) {
        if (variableName.equals("BOTTOM_PADDING")) {
            result.setValue(bottomPadding);
            return true;
        } else if (variableName.equals("RIGHT_PADDING")) {
            result.setValue(rightPadding);
            return true;
        } else if (variableName.equals("MOST_RIGHT")) {
            result.setValue(getMostRight());
            return true;
        } else if (variableName.equals("MOST_TOP")) {
            result.setValue(getMostTop());
            return true;
        } else if (variableName.equals("MOST_LEFT")) {
            result.setValue(getMostLeft());
            return true;
        } else if (variableName.equals("MOST_BOTTOM")) {
            result.setValue(getMostBottom());
            return true;
        } else if (variableName.contains(".")) {
            String[] nameParts = variableName.split("\\.");
            if (nameParts.length != 2) {
                throw new IllegalArgumentException("Layout expression variable should be simple variable name, or ID.FIELD, but you have provided [" + variableName + "]");
            }

            String componentId = nameParts[0];
            String field = nameParts[1];
            ComponentInfo ci = idToComponent.get(componentId);
            if (ci == null) {
                throw new IllegalArgumentException("Cannot find component [" + componentId + "] by it's id. You provided it in layout expression");
            }

            int fieldId = ComponentInfo.getFieldIdByFieldName(field);
            float value = ci.getValue(fieldId);
            result.setValue(value);
            return true;
        }

        return false;
    }

    private boolean internalSetVariableValue(String variableName, float value) {
        if (variableName.equals("RIGHT_PADDING")) {
            rightPadding = (int) value;
            return true;
        } else if (variableName.equals("BOTTOM_PADDING")) {
            bottomPadding = (int) value;
            return true;
        } else if (variableName.contains(".")) {
            String[] nameParts = variableName.split("\\.");
            if (nameParts.length != 2) {
                throw new IllegalArgumentException("Layout expression variable should be simple variable name, or ID.FIELD, but you have provided [" + variableName + "]");
            }

            String componentId = nameParts[0];
            String field = nameParts[1];
            ComponentInfo ci = idToComponent.get(componentId);
            if (ci == null) {
                throw new IllegalArgumentException("Cannot find component [" + componentId + "] by it's id. You provided it in layout expression");
            }

            int fieldId = ComponentInfo.getFieldIdByFieldName(field);
            ci.setValue(fieldId, (int) value);
            dependentFieldsRecalculator.calculateDependentFields(ci, fieldId);
            return true;
        }

        return false;
    }

    private int getMostLeft() {
        int minX = Integer.MAX_VALUE;
        for (ComponentInfo ci : components) {
            if (ci.isSetField(ComponentInfo.X1)) {
                int x = ci.getValue(ComponentInfo.X1);
                if (minX > x) {
                    minX = x;
                }
            }
        }
        if (minX == Integer.MAX_VALUE) {
            return 0;
        }
        return minX;
    }

    private int getMostTop() {
        int minY = Integer.MAX_VALUE;
        for (ComponentInfo ci : components) {
            if (ci.isSetField(ComponentInfo.Y1)) {
                int y = ci.getValue(ComponentInfo.Y1);
                if (minY > y) {
                    minY = y;
                }
            }
        }
        if (minY == Integer.MAX_VALUE) {
            return 0;
        }
        return minY;
    }

    private int getMostRight() {
        int maxX = 0;
        for (ComponentInfo ci : components) {
            if (ci.isSetField(ComponentInfo.X2)) {
                int x = ci.getValue(ComponentInfo.X2);
                if (maxX < x) {
                    maxX = x;
                }
            }
        }
        return maxX;
    }

    private int getMostBottom() {
        int maxY = 0;
        for (ComponentInfo ci : components) {
            if (ci.isSetField(ComponentInfo.Y2)) {
                int y = ci.getValue(ComponentInfo.Y2);
                if (maxY < y) {
                    maxY = y;
                }
            }
        }
        return maxY;
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        if (size == null) {
            layoutContainer(target);
        }
        return size;
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        if (size == null) {
            layoutContainer(parent);
        }
        return size;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        if (size == null) {
            layoutContainer(parent);
        }
        return size;
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    private String getComponentId(Component component) {
        for (ComponentInfo ci : components) {
            if (ci.getComponent() == component) {
                return ci.getComponentId();
            }
        }
        throw new IllegalArgumentException("No such component added to this layout [" + component + "]");
    }
}
