package org.fife.edisen.ui;

import org.fife.ui.ImageTranscodingUtil;
import org.fife.ui.OS;
import org.fife.ui.SelectableLabel;
import org.fife.ui.app.AppAction;
import org.fife.ui.autocomplete.EmptyIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

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

    /**
     * Adds pairs of components to a parent container using {@code SpringLayout}
     * in a label/value layout, honoring the component orientation.
     *
     * @param parent The parent container to add to.
     * @param o The component orientation.
     * @param pairs The pairs of strings to add.  This should be an even number
     *        of strings.
     * @see #addLabelValuePairs(Container, ComponentOrientation, Component...)
     */
    public static void addLabelValuePairs(Container parent, ComponentOrientation o, String... pairs) {

        SelectableLabel[] labels = Arrays.stream(pairs)
            .map(SelectableLabel::new)
            .toArray(SelectableLabel[]::new);
        addLabelValuePairs(parent, o, labels);
    }

    /**
     * Adds pairs of components to a parent container using {@code SpringLayout}
     * in a label/value layout, honoring the component orientation.
     *
     * @param parent The parent container to add to.
     * @param o The component orientation.
     * @param pairs The pairs of components to add.  This should be an even number
     *        of components.
     * @see #addLabelValuePairs(Container, ComponentOrientation, String...)
     */
    public static void addLabelValuePairs(Container parent, ComponentOrientation o, Component... pairs) {
        if (o.isLeftToRight()) {
            for (int i = 0; i < pairs.length; i += 2) {
                parent.add(pairs[i]);
                parent.add(pairs[i + 1]);
            }
        }
        else {
            for (int i = 0; i < pairs.length; i += 2) {
                parent.add(pairs[i]);
                parent.add(pairs[i + 1]);
            }
        }
    }

    public static Image getDarkLookAndFeelContentAssistImage() {

        Image image = null;
        InputStream in = Util.class.getResourceAsStream("/images/dark/intentionBulb.svg");

        try {
            image = ImageTranscodingUtil.rasterize("bulb", in, 12, 12);
        } catch (IOException ioe) { // Never happens
            ioe.printStackTrace();
        }

        return image;
    }

    public static String getDefaultAssemblerCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            // For some reason the combined cl65 doesn't seem to generate the same
            // .o files, and the generated .nes files don't run in Nestopia
            //return "cl65 -o ${rom} ${gameFile} -t nes";
            return "ca65 -o ${objfile} ${gameFile} -t nes";
//            return "ca65 -o ${objfile} ${gameFile} -t nes";
        }
        return "D:/cc65-snapshot-win32/bin/ca65 -o ${objfile} ${gameFile} -t nes";
    }

    public static String getDefaultLinkerCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            return "ld65 -o ${rom} ${objfile} -t nes";
//            return "ld65 -o ${rom} ${objfile} -C nesfile.ini";
        }
        return "D:/cc65-snapshot-win32/bin/ld65 -o ${rom} ${objfile} -t nes";
    }

    public static String getDefaultEmulatorCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            return "open -a Nestopia ${rom}";
        }
        return "D:/emulation/nes/emulators/nestopia/nestopia.exe ${rom}";
    }

    public static Icon getSvgIcon(Edisen edisen, String resource, int size) {

        // Trust fully-qualified resources.  Non-fully qualified - assume
        // they must match the theme.
        if (!resource.startsWith("/")) {
            Theme theme = edisen.getTheme();
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
     * @param edisen The parent application.
     * @param actionKey The action to update.
     * @param resource The new image.
     */
    @SuppressWarnings("unchecked")
    public static void setIcon(Edisen edisen, String actionKey, String resource) {
        AppAction<Edisen> action = (AppAction<Edisen>)edisen.getAction(actionKey);
        action.setIcon(getSvgIcon(edisen, resource, 16));
    }
}
