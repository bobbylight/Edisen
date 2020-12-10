package org.fife.edisen.ui.tabbedpane;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * An interface denoting tabbed pane content.  For example, a code editor, a CHR viewer,
 * etc.
 */
abstract class TabbedPaneContent extends JPanel {

    /**
     * Property fired when this tab's content is dirtied or saved.
     */
    public static final String PROPERTY_DIRTY = "TabbedPaneContent.dirty";

    private boolean dirty;

    abstract File getFile();

    abstract String getTabName();

    public boolean isDirty() {
        return dirty;
    }

    /**
     * The default implementation beeps.  Subclasses can override with
     * save behavior for their content.
     *
     * @throws IOException If an IO error occurs.
     */
    public void saveChanges() throws IOException {
        UIManager.getLookAndFeel().provideErrorFeedback(this);
    }

    public void setDirty(boolean dirty) {
        if (dirty != this.dirty) {
            this.dirty = dirty;
            firePropertyChange(PROPERTY_DIRTY, !dirty, dirty);
        }
    }
}
