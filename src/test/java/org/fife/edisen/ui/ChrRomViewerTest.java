package org.fife.edisen.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;
import java.io.*;

@ExtendWith(SwingRunnerExtension.class)
public class ChrRomViewerTest {

    private static File createTempFile() throws IOException {
        File file = File.createTempFile("edisenUnitTest", ".tmp");
        file.deleteOnExit();
        return file;
    }

    @Test
    public void testPaintComponent() throws IOException {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        File pngFile = createTempFile();
        try (PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(pngFile)))) {
            w.println("Dummy PNG data");
        }

        ChrRomViewer viewer = new ChrRomViewer(mockEdisen, pngFile);

        Graphics g = Mockito.mock(Graphics.class);
        viewer.paintComponent(g);
    }
}
