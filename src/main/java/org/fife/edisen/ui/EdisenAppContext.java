package org.fife.edisen.ui;

import org.fife.ui.app.AppContext;

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
    protected Edisen createApplication(String[] filesToOpen, EdisenPrefs prefs) {
        return new Edisen(this, prefs);
    }


    @Override
    protected void populatePrefsFromApplication(Edisen edisen, EdisenPrefs prefs) {

        // The "common" preferences
        super.populatePrefsFromApplication(edisen, prefs);

        // Stuff specific to this application
        prefs.lookAndFeel = UIManager.getLookAndFeel().getClass().getName();
        prefs.theme = edisen.getTheme().getKey();
    }

    @Override
    public void savePreferences(Edisen edisen) throws IOException {

        // TODO: Move this check into FifeCommon
        File dir = getPreferencesDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }

        super.savePreferences(edisen);
    }
}
