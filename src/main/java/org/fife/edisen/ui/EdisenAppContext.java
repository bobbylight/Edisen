package org.fife.edisen.ui;

import org.fife.ui.app.AppContext;
import org.fife.ui.rsyntaxtextarea.FileLocation;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class EdisenAppContext extends AppContext<Edisen, EdisenPrefs> {

    @Override
    protected String getPreferencesClassName() {
        return "org.fife.edisen.ui.EdisenPrefs";
    }


    @Override
    public File getPreferencesDir() {
        return new File(System.getProperty("user.home"), ".edisen");
    }


    @Override
    public String getPreferencesFileName() {
        return "edisen.properties";
    }


    @Override
    protected Edisen createApplicationImpl(String[] filesToOpen, EdisenPrefs prefs) {
        UIManager.put("TitlePane.unifiedBackground", true);
        return new Edisen(this, prefs);
    }


    @Override
    protected void populatePrefsFromApplication(Edisen edisen, EdisenPrefs prefs) {

        // The "common" preferences
        super.populatePrefsFromApplication(edisen, prefs);
        prefs.lookAndFeel = UIManager.getLookAndFeel().getClass().getName(); // Must be overridden (!)

        // Stuff specific to this application
        prefs.recentFiles = edisen.getRecentFiles().stream()
            .map(FileLocation::getFileFullPath)
            .toArray(String[]::new);
        prefs.recentProjects = edisen.getRecentProjects().stream()
            .map(FileLocation::getFileFullPath)
            .toArray(String[]::new);
        prefs.theme = edisen.getTheme().getKey();
    }
}
