package org.fife.edisen.ui;

import org.fife.ui.ImageTranscodingUtil;
import org.fife.ui.autocomplete.EmptyIcon;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Obligatory utility methods.
 */
public final class Util {

    /**
     * Private constructor to prevent instantiation.
     */
    private Util() {
        // Do nothing (comment for Sonar)
    }

    public static Icon getSvgIcon(String resource, int size) {

        InputStream in = Util.class.getResourceAsStream(resource);

        try {
            BufferedImage image = ImageTranscodingUtil.rasterize(resource, in, size, size);
            return new ImageIcon(image);
        } catch (IOException ioe) {
            return new EmptyIcon(size);
        }
    }
}
