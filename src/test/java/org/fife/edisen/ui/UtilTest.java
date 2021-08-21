package org.fife.edisen.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;

public class UtilTest {

    private Edisen edisen;

    @AfterEach
    public void tearDown() {
        if (edisen != null) {
            edisen.dispose();
        }
    }

    @Test
    public void testGetDarkLookAndFeelContentAssistImage() {
        Assertions.assertNotNull(Util.getDarkLookAndFeelContentAssistImage());
    }

    @Test
    public void testGetDefaultAssemblerCommandLine() {
        Assertions.assertNotNull(Util.getDefaultAssemblerCommandLine());
    }

    @Test
    public void testGetDefaultEmulatorCommandLine() {
        Assertions.assertNotNull(Util.getDefaultEmulatorCommandLine());
    }

    @Test
    public void testGetDefaultLInkerCommandLine() {
        Assertions.assertNotNull(Util.getDefaultLinkerCommandLine());
    }

    @Test
    public void testGetSvgIcon_success() {
        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();
        edisen = new Edisen(context, prefs);
        Assertions.assertNotNull(Util.getSvgIcon(edisen, "cut.svg", 16));
    }

    @Test
    public void testGetSvgIcon_error_noSuchIcon() {

        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();
        edisen = new Edisen(context, prefs);

        // This returns an EmptyIcon
        Assertions.assertNotNull(Util.getSvgIcon(edisen, "does-not-exist.svg", 16));
    }

    @Test
    public void testSetIcon() {

        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();
        edisen = new Edisen(context, prefs);
        edisen.createActions(prefs);

        Action a = edisen.getAction(Edisen.EXIT_ACTION_KEY);
        Assertions.assertNull(a.getValue(Action.SMALL_ICON));

        Util.setIcon(edisen, Edisen.EXIT_ACTION_KEY, "cut.svg");
        Assertions.assertNotNull(a.getValue(Action.SMALL_ICON));
    }
}
