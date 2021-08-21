package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class EdisenPrefsTest {

    private static void assertDefaults(EdisenPrefs prefs) {

        // Common preferences
        Assertions.assertEquals(new Point(), prefs.location);
        Assertions.assertEquals(new Dimension(650, 500), prefs.size);
        Assertions.assertTrue(prefs.toolbarVisible);
        Assertions.assertTrue(prefs.statusBarVisible);
        Assertions.assertEquals("en", prefs.language);
        Assertions.assertEquals("com.formdev.flatlaf.FlatDarkLaf", prefs.lookAndFeel);

        // App-specific preferences
        Assertions.assertArrayEquals(new String[0], prefs.recentFiles);
        Assertions.assertArrayEquals(new String[0], prefs.recentProjects);
        Assertions.assertEquals(Theme.DARK.getKey(), prefs.theme);
    }

    @Test
    public void testConstructor_verifyDefaults() {
        EdisenPrefs prefs = new EdisenPrefs();
        assertDefaults(prefs);
    }

    @Test
    public void testRestoreDefaults(){

        // Common preferences
        EdisenPrefs prefs = new EdisenPrefs();
        prefs.location = new Point(5, 5);
        prefs.size = new Dimension(42, 42);
        prefs.toolbarVisible = false;
        prefs.statusBarVisible = false;
        prefs.language = "fr";
        prefs.lookAndFeel = "fake.lookAndFeel";

        // App-specific preferences
        prefs.recentFiles = new String[] { "test.s" };
        prefs.recentProjects = new String[] { "test" };
        prefs.theme = Theme.NORD.getKey();
    }
}
