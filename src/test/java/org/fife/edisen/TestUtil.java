package org.fife.edisen;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.Theme;
import org.mockito.Mockito;

import java.awt.*;
import java.io.*;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

/**
 * Utility methods for unit tests.
 */
public class TestUtil {

    private static final String DEFAULT_PREFIX = "edisenUnitTest";
    private static final String DEFAULT_SUFFIX = ".s";

    public static void copyResourceToFile(String resource, File file) throws IOException {

        try (BufferedInputStream bin = new BufferedInputStream(TestUtil.class.getResourceAsStream(resource))) {

            byte[] buf = new byte[4096];
            int len;

            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file))) {
                while ((len = bin.read(buf)) > -1) {
                    bos.write(buf, 0, len);
                }
                bos.flush();
            }
        }
    }

    public static File createTempFile() throws IOException {
        return createTempFile(DEFAULT_SUFFIX, null);
    }

    public static File createTempFile(String suffix) throws IOException {
        return createTempFile(suffix, null);
    }

    public static File createTempFile(String suffix, String text) throws IOException {
        return createTempFile(DEFAULT_PREFIX, suffix, text);
    }

    public static File createTempFile(String prefix, String suffix, String text) throws IOException {

        File file = File.createTempFile(prefix, suffix);
        file.deleteOnExit();

        if (text != null) {
            try (PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
                w.println(text);
            }
        }

        return file;
    }

    /**
     * Creates a reasonably well-mocked Edisen.  Mocks most things needed
     * for various tests - i18n stuff, being a parent window, etc.
     *
     * @return The mocked Edisen instance.
     */
    public static Edisen mockEdisen() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        doReturn(Theme.NORD).when(mockEdisen).getTheme();

        // Stuff for tests that create UI's with localized text
        ResourceBundle msg = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");
        doReturn(msg).when(mockEdisen).getResourceBundle();
        doReturn("test").when(mockEdisen).getString(anyString());

        // Needed for setLocationRelativeTo(mockEdisen) to work
        GraphicsConfiguration mockGC = Mockito.mock(GraphicsConfiguration.class);
        doReturn(mockGC).when(mockEdisen).getGraphicsConfiguration();
        doReturn(new Rectangle(0, 0, 1280, 1024)).when(mockGC).getBounds();

        return mockEdisen;
    }
}
