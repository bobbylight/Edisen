package org.fife.edisen.ui;

import org.fife.ui.ImageTranscodingUtil;
import org.fife.ui.app.AppAction;
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

        Theme theme = Edisen.get().getTheme();
        resource = "/images/" + theme.name().toLowerCase() + "/" + resource;
        InputStream in = Util.class.getResourceAsStream(resource);

        try {
            BufferedImage image = ImageTranscodingUtil.rasterize(resource, in, size, size);
            return new ImageIcon(image);
        } catch (IOException ioe) {
            return new EmptyIcon(size);
        }
    }

    /**
     * Sets an icon on an action in the application.  An icon is picked that provides the
     * best contrast for the current theme.  This is called in response to theme changes.
     *
     * @param actionKey The action to update.
     * @param resource The new image.
     */
    @SuppressWarnings("unchecked")
    public static void setIcon(String actionKey, String resource) {

        Edisen edisen = Edisen.get();
        AppAction<Edisen> action = (AppAction<Edisen>)edisen.getAction(actionKey);
        action.setIcon(getSvgIcon(resource, 16));
    }
}
