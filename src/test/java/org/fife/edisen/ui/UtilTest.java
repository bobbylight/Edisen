package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.swing.*;

import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SwingRunnerExtension.class)
public class UtilTest {

    private Edisen edisen;

    private void createEdisen() {

        edisen = Mockito.mock(Edisen.class);

        ResourceBundle msg = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");
        doReturn(msg).when(edisen).getResourceBundle();
        doReturn(Theme.NORD).when(edisen).getTheme();

        Action action = new Actions.BuildAction(edisen);
        doReturn(action).when(edisen).getAction(anyString());
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

        Action a = edisen.getAction(Edisen.EXIT_ACTION_KEY);
        Assertions.assertNull(a.getValue(Action.SMALL_ICON));

        Util.setIcon(edisen, Edisen.EXIT_ACTION_KEY, "cut.svg");
        Assertions.assertNotNull(a.getValue(Action.SMALL_ICON));
    }
}
