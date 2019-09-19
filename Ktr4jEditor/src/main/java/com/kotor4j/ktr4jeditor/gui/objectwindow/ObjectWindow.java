package com.kotor4j.ktr4jeditor.gui.objectwindow;

import com.kotor4j.ktr4jeditor.gui.utils.Lookup;
import com.kotor4j.ktr4jeditor.gui.utils.R;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author Dmitry
 */
public class ObjectWindow extends JInternalFrame {

    private JTabbedPane objectCategoriesTabs;

    public ObjectWindow() {
        setTitle("Object Window");

        setFrameIcon(new ImageIcon((Image) Lookup.getDefault().get(R.MAIN_ICON)));
        objectCategoriesTabs = new JTabbedPane();
        add(objectCategoriesTabs);
        objectCategoriesTabs.add("NPC", new JPanel());
        objectCategoriesTabs.add("Static", new JPanel());

    }

}
