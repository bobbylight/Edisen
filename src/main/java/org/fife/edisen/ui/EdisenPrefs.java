package org.fife.edisen.ui;

import org.fife.ui.app.prefs.AppPrefs;

import java.awt.*;

public class EdisenPrefs extends AppPrefs {

    public String[] recentFiles;
    public String[] recentProjects;
    public String theme;

    public EdisenPrefs() {
        setDefaults();
    }

    @Override
    public void setDefaults() {

        // Common preferences
        location = new Point(0, 0);
        size = new Dimension(650, 500);
        toolbarVisible = true;
        statusBarVisible = true;
        language = "en";
        lookAndFeel = "com.formdev.flatlaf.FlatDarkLaf";

        // App-specific preferences
        recentProjects = new String[0];
        theme = Theme.DARK.getKey();
    }
}
