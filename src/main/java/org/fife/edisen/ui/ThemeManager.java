package org.fife.edisen.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;

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

        LookAndFeel laf = switch (theme) {
            case DARK -> new FlatDarkLaf();
            case LIGHT -> new FlatLightLaf();
            case NORD -> new FlatNordIJTheme();
        };

        try {
            UIManager.setLookAndFeel(laf);
            app.refreshLookAndFeel(theme);
        } catch (UnsupportedLookAndFeelException e) {
            app.displayException(e);
        }
    }
}
