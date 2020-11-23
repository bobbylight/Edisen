package org.fife.edisen.ui;

import org.fife.ui.OS;
import org.fife.ui.app.GUIApplicationPrefs;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

public class EdisenPrefs extends GUIApplicationPrefs<Edisen> {

    public String assemblerCommandLine;
    public String emulatorCommandLine;

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
     * if no LAF is currently saved.
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

        // Common preferences
        location = new Point(0, 0);
        size = new Dimension(650, 500);
        toolbarVisible = true;
        statusBarVisible = true;
        language = "en";

        // App-specific preferences
        if (OS.get() == OS.MAC_OS_X) {
            // For some reason the combined cl65 doesn't seem to generate the same
            // .o files, and the generated .nes files don't run in Nestopia
            //assemblerCommandLine = "cl65 -o ${rom} ${gameFile} -t nes";
            assemblerCommandLine = "ca65 -o ${objfile} ${gameFile} -t nes && ld65 -o ${rom} ${objfile} -t nes";
//            assemblerCommandLine = "ca65 -o ${objfile} ${gameFile} -t nes && ld65 -o ${rom} ${objfile} -C nesfile.ini";
            emulatorCommandLine = "open -a Nestopia ${rom}";
        }
        else {
            assemblerCommandLine = "D:/cc65-snapshot-win32/bin/ca65 -o ${objfile} ${gameFile} -t nes && D:/cc65-snapshot-win32/bin/ld65 -o ${rom} ${objfile} -t nes";
            emulatorCommandLine = "D:/emulation/nes/emulators/nestopia/nestopia.exe ${rom}";
        }
    }
}
