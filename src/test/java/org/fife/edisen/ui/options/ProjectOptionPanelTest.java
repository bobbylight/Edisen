package org.fife.edisen.ui.options;

import org.fife.edisen.TestUtil;
import org.fife.edisen.model.EdisenProject;
import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.fife.edisen.ui.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.io.IOException;

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
    public void testRestoreDefaults_projectLoaded_nothingChanged() throws IOException {

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine(Util.getDefaultAssemblerCommandLine());
        project.setEmulatorCommandLine(Util.getDefaultEmulatorCommandLine());
        project.setLinkCommandLine(Util.getDefaultLinkerCommandLine());
        project.setProjectFile(TestUtil.createTempFile(".edisen.json").toPath());

        // Initialize an Edisen mock with all default values for this option panel
        Edisen edisen = Mockito.mock(Edisen.class);
        doReturn(project).when(edisen).getProject();
        doReturn(project.getAssemblerCommandLine()).when(edisen).getAssemblerCommandLine();
        doReturn(project.getEmulatorCommandLine()).when(edisen).getEmulatorCommandLine();
        doReturn(project.getLinkCommandLine()).when(edisen).getLinkerCommandLine();

        // Initialize the option panel with the default values
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        panel.setValues(edisen);

        // It should return false since everything was still set to the defaults
        Assertions.assertFalse(panel.restoreDefaults());
    }

    @Test
    public void testRestoreDefaults_projectLoaded_everythingChanged() throws IOException {

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine(Util.getDefaultAssemblerCommandLine());
        project.setEmulatorCommandLine(Util.getDefaultEmulatorCommandLine());
        project.setLinkCommandLine(Util.getDefaultLinkerCommandLine());
        project.setProjectFile(TestUtil.createTempFile(".edisen.json").toPath());

        // Initialize an Edisen mock with all modified values for this option panel
        Edisen edisen = Mockito.mock(Edisen.class);
        doReturn(project).when(edisen).getProject();
        doReturn("changed").when(edisen).getAssemblerCommandLine();
        doReturn("changed").when(edisen).getEmulatorCommandLine();
        doReturn("changed").when(edisen).getLinkerCommandLine();

        // Initialize the option panel with the modified values
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        panel.setValues(edisen);

        // It should return true since (all) fields were modified
        Assertions.assertTrue(panel.restoreDefaults());
    }

    @Test
    public void testRestoreDefaults_projectNotLoaded_falseReturned() {

        // Initialize an Edisen mock with no project loaded
        Edisen edisen = Mockito.mock(Edisen.class);

        // Initialize the option panel with the modified values
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        panel.setValues(edisen);

        Assertions.assertFalse(panel.restoreDefaults());
    }
}
