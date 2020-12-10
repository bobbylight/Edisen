package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.ui.ChrRomViewer;
import org.fife.edisen.ui.Edisen;
import org.fife.ui.RScrollPane;

import java.awt.*;
import java.io.File;

/**
 * Displays a preview of a {@code .chr} file.
 */
class ChrRomTabbedPaneContent extends TabbedPaneContent {

    private final Edisen edisen;
    private final File chrFile;

    ChrRomTabbedPaneContent(Edisen edisen, File chrFile) {
        this.edisen = edisen;
        this.chrFile = chrFile;
        initUI();
    }

    @Override
    File getFile() {
        return chrFile;
    }

    @Override
    String getTabName() {
        return chrFile.getName();
    }

    private void initUI() {

        setLayout(new BorderLayout());

        ChrRomViewer viewer = new ChrRomViewer(edisen, chrFile);
        add(new RScrollPane(viewer));
    }
}
