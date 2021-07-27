package org.fife.edisen.ui.tabbedpane;

import java.io.File;

/**
 * Tabbed pane content containing nothing, just to make the code a little cleaner.
 */
class DummyTabbedPaneContent extends TabbedPaneContent {

    @Override
    public File getFile() {
        return null;
    }

    @Override
    String getTabName() {
        return "(dummy)";
    }
}
