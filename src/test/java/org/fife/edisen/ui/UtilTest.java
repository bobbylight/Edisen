package org.fife.edisen.ui;

import org.fife.ui.rtextarea.RTextArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.swing.*;

@ExtendWith(SwingRunnerExtension.class)
public class UtilTest {

    private Edisen edisen;
    private EdisenPrefs prefs;

    @BeforeEach
    public void setUp() {
        prefs = new EdisenPrefs();
    }

    @AfterEach
    public void tearDown() {
        if (edisen != null) {
            edisen.dispose();
        }
    }

    private void createEdisen() {

        new RTextArea(); // Yuck, needed for initialization

        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();
        edisen = new Edisen(context, prefs);
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
    public void testGetDefaultLinkerCommandLine() {
        Assertions.assertNotNull(Util.getDefaultLinkerCommandLine());
    }

    @Test
    public void testGetSvgIcon_success() {
        createEdisen();
        Assertions.assertNotNull(Util.getSvgIcon(edisen, "cut.svg", 16));
    }

    @Test
    public void testGetSvgIcon_error_noSuchIcon() {

        createEdisen();

        // This returns an EmptyIcon
        Assertions.assertNotNull(Util.getSvgIcon(edisen, "does-not-exist.svg", 16));
    }

    @Test
    public void testSetIcon() {

        createEdisen();
        edisen.preCreateActions(prefs, null);
        edisen.createActions(prefs);

        Action a = edisen.getAction(Edisen.EXIT_ACTION_KEY);
        Assertions.assertNull(a.getValue(Action.SMALL_ICON));

        Util.setIcon(edisen, Edisen.EXIT_ACTION_KEY, "cut.svg");
        Assertions.assertNotNull(a.getValue(Action.SMALL_ICON));
    }
}
