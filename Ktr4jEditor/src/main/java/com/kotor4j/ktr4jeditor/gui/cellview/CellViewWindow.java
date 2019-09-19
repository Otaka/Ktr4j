package com.kotor4j.ktr4jeditor.gui.cellview;

import com.kotor4j.ktr4jeditor.gui.utils.Lookup;
import com.kotor4j.ktr4jeditor.gui.utils.R;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

/**
 * @author Dmitry
 */
public class CellViewWindow extends JInternalFrame {

    public CellViewWindow() {
        setTitle("Cell View");
        setFrameIcon(new ImageIcon((Image) Lookup.getDefault().get(R.MAIN_ICON)));
    }

}
