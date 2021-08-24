package org.fife.edisen.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fife.edisen.TestUtil;
import org.fife.edisen.model.EdisenProject;
import org.fife.rsta.ui.GoToDialog;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SwingRunnerExtension.class)
public class EdisenTest {

    private TestableEdisen edisen;

    @AfterEach
    public void tearDown() {
        if (edisen != null) {
            edisen.dispose();
        }
    }

    private static TestableEdisen createEdisen() {

        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();

        return new TestableEdisen(context, prefs);
    }

    @Test
    public void testCloseTab_validIndex_notDirty() throws IOException {
        edisen = createEdisen();

        File[] files = new File[] {
            TestUtil.createTempFile(".chr"),
            TestUtil.createTempFile(".s"),
        };
        for (File file : files) {
            edisen.openFileForEditing(file);
        }

        Assertions.assertTrue(edisen.closeTab(0));
    }

    @Test
    public void testCloseTab_invalid_lessThanZero() throws IOException {
        edisen = createEdisen();
        Assertions.assertFalse(edisen.closeTab(-1));
    }

    @Test
    public void testCloseTab_invalid_greaterThanTabCount() throws IOException {
        edisen = createEdisen();
        Assertions.assertFalse(edisen.closeTab(42));
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
    public void testFind() {

        edisen = createEdisen();

        // The first call creates the dialog
        edisen.find();
        verify(edisen.mockFindDialog, times(1)).setVisible(eq(true));

        // The second call uses cached info, so run again for coverage of this scenario
        edisen.find();
        verify(edisen.mockFindDialog, times(2)).setVisible(eq(true));
    }

    @Test
    public void testFindAfterReplace() {

        edisen = createEdisen();

        // The "replace" call creates the search context
        edisen.replace();

        // The subsequent "find" call reuses that search context but creates the Find dialog
        edisen.find();
        verify(edisen.mockFindDialog, times(1)).setVisible(eq(true));
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
    public void testGoToLine_textAreaFocused() throws IOException {

        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s", "line 1\nline 2");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        // Opening a project will open its "main" source file
        edisen.openFile(projectFile);

        edisen.createGoToDialog(); // Unfortunately must be done for our method mock below
        doReturn(0).when(edisen.mockGoToDialog).getLineNumber();
        edisen.goToLine();
        Assertions.assertEquals(0, edisen.exceptionCount);
    }

    @Test
    public void testGoToLine_error_BadLocationException() throws IOException {

        // Note this scenario doesn't happen in real life, but is here for coverage
        edisen = createEdisen();

        EdisenProject project = new EdisenProject();
        project.setAssemblerCommandLine("assembler");
        project.setLinkCommandLine("linker");
        project.setEmulatorCommandLine("emulator");
        File mainProjectFile = TestUtil.createTempFile(".s", "line 1\nline 2");
        project.setGameFile(mainProjectFile.getName());
        String json = new ObjectMapper().writeValueAsString(project);
        File projectFile = TestUtil.createTempFile(".edisen.json", json);

        // Opening a project will open its "main" source file
        edisen.openFile(projectFile);

        edisen.createGoToDialog(); // Unfortunately must be done for our method mock below
        doReturn(999).when(edisen.mockGoToDialog).getLineNumber();

        Assertions.assertEquals(0, edisen.exceptionCount);
        edisen.goToLine();
        Assertions.assertEquals(1, edisen.exceptionCount);
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
    public void testRefreshLookAndFeel_noDialogsCreated() {
        edisen = createEdisen();
        Assertions.assertNotEquals(Theme.NORD, edisen.getTheme());
        edisen.refreshLookAndFeel(Theme.NORD);
        Assertions.assertEquals(Theme.NORD, edisen.getTheme());
    }

    @Test
    public void testRefreshLookAndFeel_dialogsCreated() {

        edisen = createEdisen();
        edisen.getOptionsDialog();
        edisen.find();
        edisen.replace();

        Assertions.assertNotEquals(Theme.NORD, edisen.getTheme());
        edisen.refreshLookAndFeel(Theme.NORD);
        Assertions.assertEquals(Theme.NORD, edisen.getTheme());
    }

    @Test
    public void testReplace() {

        edisen = createEdisen();

        // The first call creates the dialog
        edisen.replace();
        verify(edisen.mockReplaceDialog, times(1)).setVisible(eq(true));

        // The second call uses cached info, so run again for coverage of this scenario
        edisen.replace();
        verify(edisen.mockReplaceDialog, times(2)).setVisible(eq(true));
    }

    @Test
    public void testReplaceAfterFind() {

        edisen = createEdisen();

        // The "find" call creates the search context
        edisen.find();

        // The subsequent "replace" call reuses that search context but creates the Find dialog
        edisen.replace();
        verify(edisen.mockReplaceDialog, times(1)).setVisible(eq(true));
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

    @Test
    public void testSaveProject_success() throws IOException {

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

        edisen.setAssemblerCommandLine("aaa");
        edisen.setLinkerCommandLine("bbb");
        edisen.setEmulatorCommandLine("ccc");

        edisen.saveProject();

        EdisenProject newProject = new ObjectMapper().readValue(projectFile, EdisenProject.class);
        Assertions.assertEquals(edisen.getAssemblerCommandLine(), newProject.getAssemblerCommandLine());
        Assertions.assertEquals(edisen.getLinkerCommandLine(), newProject.getLinkCommandLine());
        Assertions.assertEquals(edisen.getEmulatorCommandLine(), newProject.getEmulatorCommandLine());
        Assertions.assertEquals(project.getName(), newProject.getName());
        Assertions.assertEquals(project.getGameFile(), newProject.getGameFile());
    }

    /**
     * Overridden to not display an error dialog since that doesn't play nicely in CI builds.
     */
    static class TestableEdisen extends Edisen {

        FindDialog mockFindDialog;
        ReplaceDialog mockReplaceDialog;
        GoToDialog mockGoToDialog;
        int exceptionCount;

        TestableEdisen(EdisenAppContext context, EdisenPrefs prefs) {
            super(context, prefs);
        }

        @Override
        protected FindDialog createFindDialog() {
            if (mockFindDialog == null) {
                mockFindDialog = Mockito.mock(FindDialog.class);
            }
            return mockFindDialog;
        }

        @Override
        protected GoToDialog createGoToDialog() {
            if (mockGoToDialog == null) {
                mockGoToDialog = Mockito.mock(GoToDialog.class);
            }
            return mockGoToDialog;
        }

        @Override
        protected ReplaceDialog createReplaceDialog() {
            if (mockReplaceDialog == null) {
                mockReplaceDialog = Mockito.mock(ReplaceDialog.class);
            }
            return mockReplaceDialog;
        }

        @Override
        public void displayException(Frame owner, Throwable t, String desc) {
            exceptionCount++;
            t.printStackTrace();
        }
    }
}
