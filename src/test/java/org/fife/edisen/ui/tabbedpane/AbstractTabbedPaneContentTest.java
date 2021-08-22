package org.fife.edisen.ui.tabbedpane;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class AbstractTabbedPaneContentTest {

    @Test
    public void testGetSetDirty() {
        TestableTabbedPaneContent content = new TestableTabbedPaneContent();
        Assertions.assertFalse(content.isDirty());
        content.setDirty(true);
        Assertions.assertTrue(content.isDirty());
    }

    @Test
    public void testSaveContent() throws IOException {
        // Just for coverage
        TestableTabbedPaneContent content = new TestableTabbedPaneContent();
        content.saveChanges();
    }

    static class TestableTabbedPaneContent extends TabbedPaneContent {

        @Override
        public File getFile() {
            return null;
        }

        @Override
        String getTabName() {
            return null;
        }
    }
}
