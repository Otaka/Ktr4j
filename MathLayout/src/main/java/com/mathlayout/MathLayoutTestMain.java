package com.mathlayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * @author Dmitry
 */
public class MathLayoutTestMain extends JFrame {

    public static void main(String[] args) {
        System.out.println("Started MathLayout Test application");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MathLayoutTestMain window = new MathLayoutTestMain();
                window.init();
            }
        });
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.drawLine(0, 300, 500, 300);
            }
        };

        getContentPane().add(panel);
        
        JTextArea label=new JTextArea();
        label.getBaselineResizeBehavior();
        label.getBaseline(0, 25);
        label.getBaseline(500, label.getPreferredSize().height);

        MathLayout mathLayout = new MathLayout();
        panel.setLayout(mathLayout);
        panel.add(new JLabel("First label"), "label1");
        panel.add(createOpaqueSquare(Color.yellow, 60, 80), "square1");
        panel.add(new JLabel("Second Label"), "label2");
        panel.add(new JLabel("Third label:"), "label3");
        panel.add(new JTextField("mytext"), "textfield");
        panel.add(new JButton("OK"), "ok_button");
        panel.add(createOpaqueSquare(Color.BLUE, 100, 70), "square2");
        mathLayout.createAndSetSimpleVariable("PADDING", 10);
        mathLayout.addExpression("label1.X1=200 ; label1.Y1=PADDING");
        mathLayout.addExpression("square1.HCENTER=label1.HCENTER ; square1.Y1=label1.Y2+PADDING");
        mathLayout.addExpression("label2.HCENTER=square1.HCENTER ; label2.Y1=square1.Y2+PADDING");
        mathLayout.addExpression("square2.HCENTER=label2.HCENTER ; square2.Y1=label2.Y2+PADDING");
        mathLayout.addExpression("label3.X1=0 ; label3.BASELINE=300");
        mathLayout.addExpression("textfield.X1=label3.X2+PADDING ;textfield.BASELINE=300");
        mathLayout.addExpression("ok_button.X1=MOST_RIGHT+30 ; ok_button.BASELINE=label1.BASELINE");
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createOpaqueSquare(Color color, int width, int height) {
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(color);
        panel.setPreferredSize(new Dimension(width, height));
        return panel;
    }
}
