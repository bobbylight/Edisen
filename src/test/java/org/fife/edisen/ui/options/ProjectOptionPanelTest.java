package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.fife.edisen.ui.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.Mockito.doReturn;

@ExtendWith(SwingRunnerExtension.class)
public class ProjectOptionPanelTest {

    @Test
    public void testGetTopJComponent() {
        Edisen edisen = Mockito.mock(Edisen.class);
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        Assertions.assertNotNull(panel.getTopJComponent());
    }

    @Test
    public void testRestoreDefaults_nothingChanged() {

        // Initialize an Edisen mock with all default values for this option panel
        Edisen edisen = Mockito.mock(Edisen.class);
        doReturn(Util.getDefaultAssemblerCommandLine()).when(edisen).getAssemblerCommandLine();
        doReturn(Util.getDefaultEmulatorCommandLine()).when(edisen).getEmulatorCommandLine();
        doReturn(Util.getDefaultLinkerCommandLine()).when(edisen).getLinkerCommandLine();

        // Initialize the option panel with the default values
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        panel.setValues(edisen);

        // It should return false since everything was still set to the defaults
        Assertions.assertFalse(panel.restoreDefaults());
    }

    @Test
    public void testRestoreDefaults_everythingChanged() {

        // Initialize an Edisen mock with all modified values for this option panel
        Edisen edisen = Mockito.mock(Edisen.class);
        doReturn("changed").when(edisen).getAssemblerCommandLine();
        doReturn("changed").when(edisen).getEmulatorCommandLine();
        doReturn("changed").when(edisen).getLinkerCommandLine();

        // Initialize the option panel with the modified values
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        panel.setValues(edisen);

        // It should return true since (all) fields were modified
        Assertions.assertTrue(panel.restoreDefaults());
    }
}
