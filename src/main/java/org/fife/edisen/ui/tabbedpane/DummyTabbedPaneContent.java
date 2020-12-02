package org.fife.edisen.ui.tabbedpane;

/**
 * Tabbed pane content containing nothing, just to make the code a little cleaner.
 */
class DummyTabbedPaneContent extends TabbedPaneContent {

    @Override
    String getTabName() {
        return "(dummy)";
    }
}
