package org.fife.edisen.ui.tabbedpane;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DummyTabbedPaneContentTest {

    @Test
    public void testGetFile() {
        DummyTabbedPaneContent content = new DummyTabbedPaneContent();
        Assertions.assertNull(content.getFile());
    }

    @Test
    public void testGetTabName() {
        DummyTabbedPaneContent content = new DummyTabbedPaneContent();
        Assertions.assertEquals("(dummy)", content.getTabName());
    }
}
