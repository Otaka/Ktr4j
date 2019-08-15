package com.kotor4j.ktr4jeditor;

import com.kotor4j.ktr4jeditor.gui.MainWindow;
import java.awt.HeadlessException;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Dmitry
 */
public class Ktr4JMain {

    public static void main(String[] args) {
        System.out.println("Started Ktr4JMain");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainWindow window = new MainWindow();
                    window.setVisible(true);
                    window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
                } catch (HeadlessException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
