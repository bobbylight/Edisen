package org.fife.edisen.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

/**
 * '
 * Manages the application's current theme.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class ThemeManager {

    public static void apply(Edisen app, Theme theme) {

        LookAndFeel laf;

        switch (theme) {
            case DARK:
                laf = new FlatDarkLaf();
                break;
            default:
            case LIGHT:
                laf = new FlatLightLaf();
                break;
        }

        try {
            UIManager.setLookAndFeel(laf);
            app.refreshLookAndFeel(theme);
        } catch (UnsupportedLookAndFeelException e) {
            app.displayException(e);
        }
    }
}
