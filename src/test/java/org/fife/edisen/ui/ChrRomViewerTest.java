package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

@ExtendWith(SwingRunnerExtension.class)
class ChrRomViewerTest {

    @Test
    void testPaintComponent() throws IOException {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        File file = TestUtil.createTempFile(".chr");
        TestUtil.copyResourceToFile("/chr/test.chr", file);

        ChrRomViewer viewer = new ChrRomViewer(mockEdisen, file);

        BufferedImage image = new BufferedImage(1280, 1024, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        viewer.paintComponent(g);
    }
}
