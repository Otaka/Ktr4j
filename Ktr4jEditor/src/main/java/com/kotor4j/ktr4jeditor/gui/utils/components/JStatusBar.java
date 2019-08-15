package com.kotor4j.ktr4jeditor.gui.utils.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 * @author Dmitry
 */
public class JStatusBar extends JPanel {

    private Map<String, JLabel> tagToLabel = new HashMap<>();

    public JStatusBar() {
        setPreferredSize(new Dimension(30, 30));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    public void addStatusLabel(String tag, int screenPercent) {
        if (tagToLabel.containsKey(tag)) {
            throw new IllegalArgumentException("Such tag [" + tag + "] already exists");
        }

        JPanel statusPanel = new JPanel(new BorderLayout(0, 0));
        statusPanel.setPreferredSize(new Dimension(getSize().width / 100 * screenPercent, getSize().height - 8));
        JLabel label = new JLabel();
        statusPanel.add(label);
        statusPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        add(statusPanel);
        tagToLabel.put(tag, label);
    }

    public void setText(String tag, String text) {
        JLabel label = tagToLabel.get(tag);
        if (label == null) {
            throw new IllegalArgumentException("There is no such tag [" + tag + "]");
        }
        label.setText("  " + text);
    }
}
