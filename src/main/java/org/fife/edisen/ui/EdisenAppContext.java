package org.fife.edisen.ui;

import org.fife.ui.app.AppContext;

import java.io.File;

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
    protected void populatePrefsFromApplication(Edisen rtext, EdisenPrefs prefs) {

        // The "common" preferences
        super.populatePrefsFromApplication(rtext, prefs);

        // Stuff specific to this application.
        // todo
    }
}
