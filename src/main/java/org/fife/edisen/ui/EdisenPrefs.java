package org.fife.edisen.ui;

import org.fife.ui.app.prefs.AppPrefs;
import org.fife.ui.app.themes.FlatDarkTheme;

import java.awt.*;

public class EdisenPrefs extends AppPrefs {

    private static final String DEFAULT_APP_THEME = FlatDarkTheme.NAME;

    public String[] recentFiles;
    public String[] recentProjects;

    public EdisenPrefs() {
        setDefaults();
    }

    @Override
    public void setDefaults() {

        // Common preferences
        location = new Point(0, 0);
        size = new Dimension(650, 500);
        appTheme = DEFAULT_APP_THEME;
        toolbarVisible = true;
        statusBarVisible = true;
        language = "en";

        // App-specific preferences
        recentFiles = new String[0];
        recentProjects = new String[0];
    }
}
