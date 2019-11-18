package org.fife.edisen.ui;

import org.fife.ui.app.GUIApplicationPrefs;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class EdisenPrefs extends GUIApplicationPrefs<Edisen> {

    public EdisenPrefs() {
        setDefaults();
    }

    /**
     * Returns just the LookAndFeel saved in the application preferences.
     * This is so we can set the LAF before loading the application, to allow
     * finnicky LAF's like Substance to work properly (Substance only works if
     * it's set before the first JFrame is created).
     *
     * @return The name of the LookAndFeel to load, or the system default LAF
     *         if no LAF is currently saved.
     */
    static String getLookAndFeelToLoad() {
        Preferences prefs = Preferences.userNodeForPackage(Edisen.class);
        String defaultLAF = UIManager.getSystemLookAndFeelClassName();
        return prefs.get("lookAndFeel", defaultLAF);
    }

    @Override
    public GUIApplicationPrefs<Edisen> load() {

        try {

            Preferences prefs = Preferences.userNodeForPackage(Edisen.class);
            loadCommonPreferences(prefs);

            // Load app-specific preferences
            // If anything at all goes wrong, just use default property values.
        } catch (RuntimeException re) {
            throw re; // Let RuntimeExceptions through (FindBugs warning)
        } catch (Exception e) {
            e.printStackTrace();
            setDefaults();
        }

        return this;
    }

    @Override
    public GUIApplicationPrefs<Edisen> populate(Edisen app) {

        populateCommonPreferences(app);

        // populate app-specific preferences from the app to this object

        return this;
    }

    @Override
    public void save() {

        Preferences prefs = Preferences.userNodeForPackage(Edisen.class);
        saveCommonPreferences(prefs);

        // Put any app-specific preferences here
    }

    @Override
    protected void setDefaults() {
        location = new Point(0,0);
        size = new Dimension(650,500);
        toolbarVisible = true;
        statusBarVisible = true;
        language = "en";
    }
}
