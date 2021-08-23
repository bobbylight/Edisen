package org.fife.edisen.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fife.edisen.TestUtil;
import org.fife.edisen.model.EdisenProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;

@ExtendWith(SwingRunnerExtension.class)
public class EdisenTest {

    private Edisen edisen;

    @AfterEach
    public void tearDown() {
        if (edisen != null) {
            edisen.dispose();
        }
    }

    private static Edisen createEdisen() {

        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();

        return new Edisen(context, prefs);
    }

    @Test
    public void testCreateAboutDialog() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.createAboutDialog());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testCreateMenuBar() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.createMenuBar(edisen.getPreferences()));
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testCreateSplashScreen() {
        edisen = createEdisen();
        Assertions.assertNull(edisen.createSplashScreen());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testCreateStatusBar() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.createStatusBar(edisen.getPreferences()));
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testCreateToolBar() {
        edisen = createEdisen();
        Assertions.assertNull(edisen.createToolBar(edisen.getPreferences()));
    }

    @Test
    @Disabled("Figure out how to implement")
    public void testDoExit() {
        // Do nothing (comment for Sonar)
    }

    @Test
    @Disabled("Figure out how to implement")
    public void testFind() {
        // Do nothing (comment for Sonar)
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetSetAssemblerCommandLine() throws IOException {

        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        edisen.openFile(projectFile);
        Assertions.assertEquals(project.getAssemblerCommandLine(), edisen.getAssemblerCommandLine());

        edisen.setAssemblerCommandLine("test");
        Assertions.assertEquals("test", edisen.getAssemblerCommandLine());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetSetEmulatorCommandLine() throws IOException {

        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        edisen.openFile(projectFile);
        Assertions.assertEquals(project.getEmulatorCommandLine(), edisen.getEmulatorCommandLine());

        edisen.setEmulatorCommandLine("test");
        Assertions.assertEquals("test", edisen.getEmulatorCommandLine());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetFileChooser() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getFileChooser());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetSetLinkerCommandLine() throws IOException {

        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        edisen.openFile(projectFile);
        Assertions.assertEquals(project.getLinkCommandLine(), edisen.getLinkerCommandLine());

        edisen.setLinkerCommandLine("test");
        Assertions.assertEquals("test", edisen.getLinkerCommandLine());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetOptionsDialog() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getOptionsDialog());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetPreferences() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getPreferences());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetProject() throws IOException {

        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        edisen.openFile(projectFile);
        Assertions.assertNotNull(edisen.getProject());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetRecentFiles() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getRecentFiles());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetRecentProjects() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getRecentProjects());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetResourceBundleClassName() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getResourceBundleClassName());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetSelectedTabIndex() {
        edisen = createEdisen();
        Assertions.assertEquals(0, edisen.getSelectedTabIndex());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetTheme() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getTheme());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testGetVersionString() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getVersionString());
    }

    @Test
    @Disabled("Figure out how to implement")
    public void testIsTabOkToClose() {
        // Do nothing (comment for Sonar)
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testLog() {
        edisen = createEdisen();
        edisen.log("stdout", "Test message");
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testRefreshLookAndFeel() {
        edisen = createEdisen();
        Assertions.assertNotEquals(Theme.NORD, edisen.getTheme());
        edisen.refreshLookAndFeel(Theme.NORD);
        Assertions.assertEquals(Theme.NORD, edisen.getTheme());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testSaveAllDirtyFiles_success() throws IOException {

        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        // Opening a project will open its "main" source file
        edisen.openFile(projectFile);

        // TODO: Figure out a way to make the current tab dirty

        Assertions.assertTrue(edisen.saveAllDirtyFiles());
    }

    @Test
    @Disabled("Doesn't play nicely running in GitHub Actions environment")
    public void testSaveCurrentFile() throws IOException {

        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        // Opening a project will open its "main" source file
        edisen.openFile(projectFile);

        edisen.saveCurrentFile();
    }

    @Test
    @Disabled("Figure out how to implement")
    public void testSaveCurrentFileAs() {
        // Do nothing (comment for Sonar)
    }
}
