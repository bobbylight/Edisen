package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;
import java.util.Date;

import static org.mockito.Mockito.doReturn;

@ExtendWith(SwingRunnerExtension.class)
class AboutDialogTest {

    @Test
    void testHappyPath_noBuildDate() {

        Edisen mockEdisen = TestUtil.mockEdisen();

        Graphics g = Mockito.mock(Graphics.class);
        AboutDialog dialog = new AboutDialog(null);
        dialog.setApplication(mockEdisen);
        dialog.paint(g);
    }

    @Test
    void testHappyPath_withBuildDate() {

        Edisen mockEdisen = TestUtil.mockEdisen();
        doReturn(new Date()).when(mockEdisen).getBuildDate();

        Graphics g = Mockito.mock(Graphics.class);
        AboutDialog dialog = new AboutDialog(null);
        dialog.setApplication(mockEdisen);
        dialog.paint(g);
    }
}
