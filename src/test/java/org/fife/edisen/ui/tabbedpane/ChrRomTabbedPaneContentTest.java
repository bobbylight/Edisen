package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.TestUtil;
import org.fife.edisen.ui.Edisen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;

public class ChrRomTabbedPaneContentTest {

    @Test
    public void testGetFile() throws IOException {

        Edisen edisen = Mockito.mock(Edisen.class);
        File chrFile = TestUtil.createTempFile();

        ChrRomTabbedPaneContent content = new ChrRomTabbedPaneContent(edisen, chrFile);
        Assertions.assertEquals(chrFile, content.getFile());
    }

    @Test
    public void testGetTabName() throws IOException {

        Edisen edisen = Mockito.mock(Edisen.class);
        File chrFile = TestUtil.createTempFile();

        ChrRomTabbedPaneContent content = new ChrRomTabbedPaneContent(edisen, chrFile);
        Assertions.assertEquals(chrFile.getName(), content.getTabName());
    }
}
