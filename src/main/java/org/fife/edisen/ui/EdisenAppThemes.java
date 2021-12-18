package org.fife.edisen.ui;

import org.fife.ui.app.AppTheme;
import org.fife.ui.app.themes.FlatDarkTheme;
import org.fife.ui.app.themes.FlatLightTheme;
import org.fife.ui.app.themes.NativeTheme;
import org.fife.ui.rsyntaxtextarea.Theme;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * '
 * Manages the application's current theme.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public final class EdisenAppThemes {

    /**
     * The color to use when rendering the name of modified documents in tabs when the current Look
     * and Feel is light.
     */
    public static final Color LIGHT_MODIFIED_DOCUMENT_NAME_COLOR = Color.RED;

    /**
     * The color to use when rendering the name of modified documents in tabs when the current Look
     * and Feel is dark.
     */
    public static final Color DARK_MODIFIED_DOCUMENT_NAME_COLOR = new Color(255, 128, 128);

    /**
     * Private constructor to prevent instantiation.
     */
    private EdisenAppThemes() {
        // Do nothing - comment for Sonar
    }

    public static List<AppTheme> get() {

        Color lightListAltRowColor = new Color(0xf4f4f4);
        Color darkListAltRowColor = new Color(60, 63, 65);

        List<AppTheme> themes = new ArrayList<>();

        NativeTheme nativeTheme = new NativeTheme();
        nativeTheme.setHyperlinkForeground(Color.BLUE);
        nativeTheme.addExtraUiDefault("edisen.editorTheme", "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml");
        nativeTheme.addExtraUiDefault("edisen.iconGroupName", "Eclipse Icons");
        nativeTheme.addExtraUiDefault("edisen.labelErrorForeground", LIGHT_MODIFIED_DOCUMENT_NAME_COLOR);
        nativeTheme.addExtraUiDefault("edisen.listAltRowColor", lightListAltRowColor);
        themes.add(nativeTheme);

        FlatDarkTheme flatDarkTheme = new FlatDarkTheme();
        flatDarkTheme.setHyperlinkForeground(new Color(0x589df6));
        flatDarkTheme.addExtraUiDefault("edisen.editorTheme", "/org/fife/ui/rsyntaxtextarea/themes/monokai.xml");
        flatDarkTheme.addExtraUiDefault("edisen.iconGroupName", "IntelliJ Icons (Dark)");
        flatDarkTheme.addExtraUiDefault("edisen.labelErrorForeground", DARK_MODIFIED_DOCUMENT_NAME_COLOR);
        flatDarkTheme.addExtraUiDefault("edisen.listAltRowColor", darkListAltRowColor);
        themes.add(flatDarkTheme);

        FlatLightTheme flatLightTheme = new FlatLightTheme();
        flatLightTheme.setHyperlinkForeground(Color.BLUE);
        flatLightTheme.addExtraUiDefault("edisen.editorTheme", "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml");
        flatLightTheme.addExtraUiDefault("edisen.iconGroupName", "IntelliJ Icons (Light)");
        flatLightTheme.addExtraUiDefault("edisen.labelErrorForeground", LIGHT_MODIFIED_DOCUMENT_NAME_COLOR);
        flatLightTheme.addExtraUiDefault("edisen.listAltRowColor", lightListAltRowColor);
        themes.add(flatLightTheme);

        return themes;
    }


    /**
     * Returns the RSTA editor theme from an application theme.
     *
     * @param theme The application theme.
     * @return The RSTA editor theme.
     * @throws IOException If an IO error occurs.
     */
    public static org.fife.ui.rsyntaxtextarea.Theme getRstaTheme(AppTheme theme) throws IOException {

        String rstaThemeName = (String)theme.getExtraUiDefaults().get("edisen.editorTheme");

        return Theme.load(EdisenAppThemes.class.getResourceAsStream(rstaThemeName));
    }
}
