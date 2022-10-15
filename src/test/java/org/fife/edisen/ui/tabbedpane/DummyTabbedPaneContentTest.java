package org.fife.edisen.ui.tabbedpane;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DummyTabbedPaneContentTest {

    @Test
    void testGetFile() {
        DummyTabbedPaneContent content = new DummyTabbedPaneContent();
        Assertions.assertNull(content.getFile());
    }

    @Test
    void testGetTabName() {
        DummyTabbedPaneContent content = new DummyTabbedPaneContent();
        Assertions.assertEquals("(dummy)", content.getTabName());
    }
}
