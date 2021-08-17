package org.fife.edisen.ui;

import org.fife.ui.ImageTranscodingUtil;
import org.fife.ui.OS;
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

    public static String getDefaultAssemblerCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            // For some reason the combined cl65 doesn't seem to generate the same
            // .o files, and the generated .nes files don't run in Nestopia
            //return "cl65 -o ${rom} ${gameFile} -t nes";
            return "ca65 -o ${objfile} ${gameFile} -t nes && ld65 -o ${rom} ${objfile} -t nes";
//            return "ca65 -o ${objfile} ${gameFile} -t nes && ld65 -o ${rom} ${objfile} -C nesfile.ini";
        }
        return "D:/cc65-snapshot-win32/bin/ca65 -o ${objfile} ${gameFile} -t nes && D:/cc65-snapshot-win32/bin/ld65 -o ${rom} ${objfile} -t nes";
    }

    public static String getDefaultEmulatorCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            return "open -a Nestopia ${rom}";
        }
        return "D:/emulation/nes/emulators/nestopia/nestopia.exe ${rom}";
    }

    public static Icon getSvgIcon(String resource, int size) {

        // Trust fully-qualified resources.  Non-fully qualified - assume
        // they must match the theme.
        if (!resource.startsWith("/")) {
            Theme theme = Edisen.get().getTheme();
            resource = "/images/" + theme.getImageRoot() + "/" + resource;
        }

        InputStream in = Util.class.getResourceAsStream(resource);

        try {
            BufferedImage image = ImageTranscodingUtil.rasterize(resource, in, size, size);
            return new ImageIcon(image);
        } catch (IOException ioe) {
            System.out.println("Couldn't find icon: " + resource);
            ioe.printStackTrace();
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
