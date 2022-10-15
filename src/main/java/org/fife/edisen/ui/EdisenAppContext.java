package org.fife.edisen.ui;

import org.fife.ui.app.AppContext;
import org.fife.ui.app.AppTheme;
import org.fife.ui.rsyntaxtextarea.FileLocation;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * Basic information needed by the application.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class EdisenAppContext extends AppContext<Edisen, EdisenPrefs> {

    @Override
    public List<AppTheme> getAvailableAppThemes() {
        return EdisenAppThemes.get();
    }

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

        // Stuff specific to this application
        prefs.recentFiles = edisen.getRecentFiles().stream()
            .map(FileLocation::getFileFullPath)
            .toArray(String[]::new);
        prefs.recentProjects = edisen.getRecentProjects().stream()
            .map(FileLocation::getFileFullPath)
            .toArray(String[]::new);
    }
}
