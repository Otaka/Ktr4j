package com.kotor4j.ktr4jeditor.gui;

import com.kotor4j.ktr4jeditor.gui.cellview.CellViewWindow;
import com.kotor4j.ktr4jeditor.gui.objectwindow.ObjectWindow;
import com.kotor4j.ktr4jeditor.gui.renderwindow.RenderWindow;
import com.kotor4j.ktr4jeditor.gui.utils.Lookup;
import com.kotor4j.ktr4jeditor.gui.utils.R;
import com.kotor4j.ktr4jeditor.gui.utils.components.JStatusBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.SwingUtilities;

/**
 * @author Dmitry
 */
public class MainWindow extends JFrame {

    private JDesktopPane desktop;
    private ObjectWindow objectWindow;
    private RenderWindow renderWindow;
    private CellViewWindow cellViewWindow;
    private JStatusBar statusBar;

    public MainWindow() throws HeadlessException, IOException {
        loadIcon();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Ktr4J Editor");
        desktop = new JDesktopPane();
        desktop.setBackground(Color.GRAY);
        add(desktop);

        createMenu();
        statusBar = new JStatusBar();
        add(statusBar, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        createWindows();
                        fillStatusBar();
                    }
                });
            }
        });
    }

    private void loadIcon() throws IOException {
        InputStream stream = MainWindow.class.getResourceAsStream("/kotor-icon.png");
        if (stream == null) {
            throw new IllegalStateException("Cannot find main icon");
        }

        Image icon = ImageIO.read(stream);
        setIconImage(icon);
        Lookup.getDefault().put(R.MAIN_ICON, icon);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createWindows() {
        objectWindow = createObjectWindow();
        renderWindow = createRenderWindow();
        cellViewWindow = createCellViewWindow();
    }

    private void fillStatusBar() {
        statusBar.addStatusLabel(R.STATUS_OBJ_ID, 15);
        statusBar.addStatusLabel(R.STATUS_OBJ_TYPE, 10);
        statusBar.addStatusLabel(R.STATUS_OBJ_POSITION, 20);
        statusBar.addStatusLabel(R.STATUS_MESSAGE, 100);

        statusBar.setText(R.STATUS_OBJ_ID, "asdlkja sad");
        statusBar.setText(R.STATUS_OBJ_TYPE, "static");
        statusBar.setText(R.STATUS_OBJ_POSITION, "656,468,98");
        statusBar.setText(R.STATUS_MESSAGE, "It is test message");
    }

    private ObjectWindow createObjectWindow() {
        ObjectWindow window = new ObjectWindow();
        desktop.add(window);
        Dimension desktopSize = getContentPane().getSize();
        window.setSize(desktopSize.width / 2 - 5, desktopSize.height - 40);
        window.setLocation(2, 2);
        window.setResizable(true);
        window.setVisible(true);
        return window;
    }

    private RenderWindow createRenderWindow() {
        RenderWindow window = new RenderWindow();
        desktop.add(window);
        Dimension desktopSize = getContentPane().getSize();
        window.setLocation(desktopSize.width / 2, 2);
        window.setSize(desktopSize.width / 2 - 5, desktopSize.height / 2 - 5);
        window.setResizable(true);
        window.setVisible(true);
        return window;
    }

    private CellViewWindow createCellViewWindow() {
        CellViewWindow window = new CellViewWindow();
        desktop.add(window);
        Dimension desktopSize = getContentPane().getSize();
        window.setLocation(desktopSize.width / 2, desktopSize.height / 2 + 2);
        window.setSize(desktopSize.width / 2 - 5, desktopSize.height / 2 - 40);
        window.setResizable(true);
        window.setVisible(true);
        return window;
    }

}
