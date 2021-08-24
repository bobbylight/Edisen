package org.fife.edisen.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fife.edisen.TestUtil;
import org.fife.edisen.model.EdisenProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;
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

        return new TestableEdisen(context, prefs);
    }

    @Test
    public void testCreateAboutDialog() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.createAboutDialog());
    }

    @Test
    public void testCreateMenuBar() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.createMenuBar(edisen.getPreferences()));
    }

    @Test
    public void testCreateSplashScreen() {
        edisen = createEdisen();
        Assertions.assertNull(edisen.createSplashScreen());
    }

    @Test
    public void testCreateStatusBar() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.createStatusBar(edisen.getPreferences()));
    }

    @Test
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
    public void testGetFileChooser() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getFileChooser());
    }

    @Test
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
    public void testGetOptionsDialog() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getOptionsDialog());
    }

    @Test
    public void testGetPreferences() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getPreferences());
    }

    @Test
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
    public void testGetRecentFiles() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getRecentFiles());
    }

    @Test
    public void testGetRecentProjects() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getRecentProjects());
    }

    @Test
    public void testGetResourceBundleClassName() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getResourceBundleClassName());
    }

    @Test
    public void testGetSelectedTabIndex() {
        edisen = createEdisen();
        Assertions.assertEquals(-1, edisen.getSelectedTabIndex());
    }

    @Test
    public void testGetTheme() {
        edisen = createEdisen();
        Assertions.assertNotNull(edisen.getTheme());
    }

    @Test
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
    public void testLog() {
        edisen = createEdisen();
        edisen.log("stdout", "Test message");
    }

    @Test
    public void testRefreshLookAndFeel() {
        edisen = createEdisen();
        Assertions.assertNotEquals(Theme.NORD, edisen.getTheme());
        edisen.refreshLookAndFeel(Theme.NORD);
        Assertions.assertEquals(Theme.NORD, edisen.getTheme());
    }

    @Test
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

    /**
     * Overridden to not display an error dialog since that doesn't play nicely in CI builds.
     */
    static class TestableEdisen extends Edisen {

        TestableEdisen(EdisenAppContext context, EdisenPrefs prefs) {
            super(context, prefs);
        }

        @Override
        public void displayException(Frame owner, Throwable t, String desc) {
            t.printStackTrace();
        }
    }
}
