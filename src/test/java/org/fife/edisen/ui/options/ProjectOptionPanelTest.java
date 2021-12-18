package org.fife.edisen.ui.options;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fife.edisen.TestUtil;
import org.fife.edisen.ui.TestableEdisen;
import org.fife.edisen.ui.model.EdisenProject;
import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.fife.edisen.ui.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;

@ExtendWith(SwingRunnerExtension.class)
public class ProjectOptionPanelTest {

    @Test
    public void testGetTopJComponent() {
        Edisen edisen = TestableEdisen.create();
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        Assertions.assertNotNull(panel.getTopJComponent());
    }

    @Test
    public void testRestoreDefaults_projectLoaded_nothingChanged() throws IOException {

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine(Util.getDefaultAssemblerCommandLine());
        project.setLinkCommandLine(Util.getDefaultLinkerCommandLine());
        project.setEmulatorCommandLine(Util.getDefaultEmulatorCommandLine());
        project.setGameFile("main.s");
        String json = new ObjectMapper().writeValueAsString(project);

        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        // Initialize an Edisen mock with all default values for this option panel
        Edisen edisen = TestableEdisen.create();
        edisen.openFile(projectFile);

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
        project.setLinkCommandLine(Util.getDefaultLinkerCommandLine());
        project.setEmulatorCommandLine(Util.getDefaultEmulatorCommandLine());
        project.setGameFile("main.s");
        String json = new ObjectMapper().writeValueAsString(project);

        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        // Initialize an Edisen mock with all modified values for this option panel
        Edisen edisen = TestableEdisen.create();
        edisen.openFile(projectFile);
        edisen.setAssemblerCommandLine("changed");
        edisen.setEmulatorCommandLine("changed");
        edisen.setLinkerCommandLine("changed");

        // Initialize the option panel with the modified values
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        panel.setValues(edisen);

        // It should return true since (all) fields were modified
        Assertions.assertTrue(panel.restoreDefaults());
    }

    @Test
    public void testRestoreDefaults_projectNotLoaded_falseReturned() {

        // Initialize an Edisen mock with no project loaded
        Edisen edisen = TestableEdisen.create();

        // Initialize the option panel with the modified values
        ProjectOptionPanel panel = new ProjectOptionPanel(edisen);
        panel.setValues(edisen);

        Assertions.assertFalse(panel.restoreDefaults());
    }
}
