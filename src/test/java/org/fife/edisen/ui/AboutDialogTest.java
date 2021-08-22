package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;

@ExtendWith(SwingRunnerExtension.class)
public class AboutDialogTest {

    @Test
    public void testHappyPath() {

        Edisen mockEdisen = TestUtil.mockEdisen();

        Graphics g = Mockito.mock(Graphics.class);
        AboutDialog dialog = new AboutDialog(null);
        dialog.setApplication(mockEdisen);
        dialog.paint(g);
    }
}
