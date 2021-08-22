package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;
import java.util.Date;

import static org.mockito.Mockito.doReturn;

@ExtendWith(SwingRunnerExtension.class)
public class AboutDialogTest {

    @Test
    public void testHappyPath_noBuildDate() {

        Edisen mockEdisen = TestUtil.mockEdisen();

        Graphics g = Mockito.mock(Graphics.class);
        AboutDialog dialog = new AboutDialog(null);
        dialog.setApplication(mockEdisen);
        dialog.paint(g);
    }

    @Test
    public void testHappyPath_withBuildDate() {

        Edisen mockEdisen = TestUtil.mockEdisen();
        doReturn(new Date()).when(mockEdisen).getBuildDate();

        Graphics g = Mockito.mock(Graphics.class);
        AboutDialog dialog = new AboutDialog(null);
        dialog.setApplication(mockEdisen);
        dialog.paint(g);
    }

    @Test
    public void testRefreshLookAndFeel() {

        Edisen mockEdisen = TestUtil.mockEdisen();

        AboutDialog dialog = new AboutDialog(null);
        dialog.setApplication(mockEdisen);
        dialog.refreshLookAndFeel(Theme.LIGHT);
    }
}
