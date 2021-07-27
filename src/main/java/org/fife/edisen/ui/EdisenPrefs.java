package org.fife.edisen.ui;

import org.fife.ui.OS;
import org.fife.ui.app.prefs.AppPrefs;

import java.awt.*;

public class EdisenPrefs extends AppPrefs {

    public String assemblerCommandLine;
    public String emulatorCommandLine;
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

        recentProjects = new String[0];
        theme = Theme.DARK.getKey();
    }
}
