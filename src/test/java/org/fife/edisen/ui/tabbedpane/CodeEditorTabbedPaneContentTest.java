package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.TestUtil;
import org.fife.edisen.ui.Edisen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class CodeEditorTabbedPaneContentTest {

    @Test
    public void testGetFile() throws IOException {

        Edisen edisen = TestUtil.mockEdisen();
        File file = TestUtil.createTempFile();

        CodeEditorTabbedPaneContent content = new CodeEditorTabbedPaneContent(edisen, file);
        Assertions.assertEquals(file, content.getFile());
    }

    @Test
    public void testGetTabName() throws IOException {

        Edisen edisen = TestUtil.mockEdisen();
        File file = TestUtil.createTempFile();

        CodeEditorTabbedPaneContent content = new CodeEditorTabbedPaneContent(edisen, file);
        Assertions.assertEquals(file.getName(), content.getTabName());
    }
}
