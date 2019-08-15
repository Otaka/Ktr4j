package com.kotor4j.ktr4jeditor.gui.renderwindow;

import com.kotor4j.ktr4jeditor.gui.utils.Lookup;
import com.kotor4j.ktr4jeditor.gui.utils.R;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

/**
 * @author Dmitry
 */
public class RenderWindow extends JInternalFrame {

    public RenderWindow() {
        setTitle("Render Window");
        setFrameIcon(new ImageIcon((Image) Lookup.getDefault().get(R.MAIN_ICON)));
    }

}
