package org.fife.edisen.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SwingRunnerExtension.class)
public class AboutDialogTest {

    @Test
    public void testHappyPath() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        ResourceBundle msg = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");
        doReturn(msg).when(mockEdisen).getResourceBundle();
        doReturn("test").when(mockEdisen).getString(anyString());

        // Needed for setLocationRelativeTo(mockEdisen) to work
        GraphicsConfiguration mockGC = Mockito.mock(GraphicsConfiguration.class);
        doReturn(mockGC).when(mockEdisen).getGraphicsConfiguration();
        doReturn(new Rectangle(0, 0, 1280, 1024)).when(mockGC).getBounds();

        Graphics g = Mockito.mock(Graphics.class);
        AboutDialog dialog = new AboutDialog(null);
        dialog.setApplication(mockEdisen);
        dialog.paint(g);
    }
}
