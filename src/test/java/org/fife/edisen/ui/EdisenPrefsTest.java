package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

class EdisenPrefsTest {

    private static void assertDefaults(EdisenPrefs prefs) {

        // Common preferences
        Assertions.assertEquals(new Point(), prefs.location);
        Assertions.assertEquals(new Dimension(650, 500), prefs.size);
        Assertions.assertTrue(prefs.toolbarVisible);
        Assertions.assertTrue(prefs.statusBarVisible);
        Assertions.assertEquals("en", prefs.language);

        // App-specific preferences
        Assertions.assertArrayEquals(new String[0], prefs.recentFiles);
        Assertions.assertArrayEquals(new String[0], prefs.recentProjects);
    }

    @Test
    void testConstructor_verifyDefaults() {
        EdisenPrefs prefs = new EdisenPrefs();
        assertDefaults(prefs);
    }
}
