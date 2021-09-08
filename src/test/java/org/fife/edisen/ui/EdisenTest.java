package org.fife.edisen.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fife.edisen.TestUtil;
import org.fife.edisen.model.EdisenProject;
import org.fife.ui.rtextfilechooser.RTextFileChooser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

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

    @Test
    public void testCloseTab_validIndex_notDirty() throws IOException {
        edisen = TestableEdisen.create();

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
    public void testCloseTab_invalid_lessThanZero() {
        edisen = TestableEdisen.create();
        Assertions.assertFalse(edisen.closeTab(-1));
    }

    @Test
    public void testCloseTab_invalid_greaterThanTabCount() {
        edisen = TestableEdisen.create();
        Assertions.assertFalse(edisen.closeTab(42));
    }

    @Test
    public void testCreateAboutDialog() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.createAboutDialog());
    }

    @Test
    public void testCreateMenuBar() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.createMenuBar(edisen.getPreferences()));
    }

    @Test
    public void testCreateSplashScreen() {
        edisen = TestableEdisen.create();
        Assertions.assertNull(edisen.createSplashScreen());
    }

    @Test
    public void testCreateStatusBar() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.createStatusBar(edisen.getPreferences()));
    }

    @Test
    public void testCreateToolBar() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.createToolBar(edisen.getPreferences()));
    }

    @Test
    @Disabled("Figure out how to implement")
    public void testDoExit() {
        // Do nothing (comment for Sonar)
    }

    @Test
    public void testFind() {

        edisen = TestableEdisen.create();

        // The first call creates the dialog
        edisen.find();
        verify(edisen.mockFindDialog, times(1)).setVisible(eq(true));

        // The second call uses cached info, so run again for coverage of this scenario
        edisen.find();
        verify(edisen.mockFindDialog, times(2)).setVisible(eq(true));
    }

    @Test
    public void testFindAfterReplace() {

        edisen = TestableEdisen.create();

        // The "replace" call creates the search context
        edisen.replace();

        // The subsequent "find" call reuses that search context but creates the Find dialog
        edisen.find();
        verify(edisen.mockFindDialog, times(1)).setVisible(eq(true));
    }

    @Test
    public void testGetSetAssemblerCommandLine() throws IOException {

        edisen = TestableEdisen.create();

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

        edisen = TestableEdisen.create();

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
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getFileChooser());
        Assertions.assertNotNull(edisen.getFileChooser()); // Second time it's cached
    }

    @Test
    public void testGetSetLinkerCommandLine() throws IOException {

        edisen = TestableEdisen.create();

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
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getOptionsDialog());
        Assertions.assertNotNull(edisen.getOptionsDialog()); // Second time it's cached
    }

    @Test
    public void testGetPreferences() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getPreferences());
    }

    @Test
    public void testGetProject() throws IOException {

        edisen = TestableEdisen.create();

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
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getRecentFiles());
    }

    @Test
    public void testGetRecentProjects() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getRecentProjects());
    }

    @Test
    public void testGetResourceBundleClassName() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getResourceBundleClassName());
    }

    @Test
    public void testGetSelectedTabIndex() {
        edisen = TestableEdisen.create();
        Assertions.assertEquals(-1, edisen.getSelectedTabIndex());
    }

    @Test
    public void testGetTheme() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getTheme());
    }

    @Test
    public void testGetVersionString() {
        edisen = TestableEdisen.create();
        Assertions.assertNotNull(edisen.getVersionString());
    }

    @Test
    public void testGoToLine_textAreaFocused() throws IOException {

        edisen = TestableEdisen.create();

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
        edisen = TestableEdisen.create();

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
        edisen = TestableEdisen.create();
        edisen.log("stdout", "Test message");
    }

    @Test
    public void testOpenProjectViaFileChooser_openApproved() throws IOException {

        // Spy to do partial mocks on our Edisen instance
        edisen = Mockito.spy(TestableEdisen.create());
        doNothing().when(edisen).openFile(any());

        RTextFileChooser mockChooser = Mockito.mock(RTextFileChooser.class);
        doReturn(RTextFileChooser.APPROVE_OPTION).when(mockChooser).showOpenDialog(any());
        doReturn(mockChooser).when(edisen).getFileChooser();
        File selectedFile = TestUtil.createTempFile(".edisen.json");
        doReturn(selectedFile).when(mockChooser).getSelectedFile();

        // Verify the file chooser displays, and when "OK" is clicked, the project is opened
        edisen.openProjectViaFileChooser();
        verify(mockChooser, times(1)).showOpenDialog(any());
        verify(edisen, times(1)).openFile(eq(selectedFile));
    }

    @Test
    public void testOpenProjectViaFileChooser_openCancelled() {

        // Spy to do partial mocks on our Edisen instance
        edisen = Mockito.spy(TestableEdisen.create());
        doNothing().when(edisen).openFile(any());

        RTextFileChooser mockChooser = Mockito.mock(RTextFileChooser.class);
        doReturn(RTextFileChooser.CANCEL_OPTION).when(mockChooser).showOpenDialog(any());
        doReturn(mockChooser).when(edisen).getFileChooser();

        // Verify the file chooser displays, and when "OK" is clicked, the project is not opened
        edisen.openProjectViaFileChooser();
        verify(mockChooser, times(1)).showOpenDialog(any());
        verify(edisen, times(0)).openFile(any());
    }

    @Test
    public void testOpenViaFileChooser_openApproved() throws IOException {

        // Spy to do partial mocks on our Edisen instance
        edisen = Mockito.spy(TestableEdisen.create());
        doNothing().when(edisen).openFile(any());

        RTextFileChooser mockChooser = Mockito.mock(RTextFileChooser.class);
        doReturn(RTextFileChooser.APPROVE_OPTION).when(mockChooser).showOpenDialog(any());
        doReturn(mockChooser).when(edisen).getFileChooser();
        File selectedFile = TestUtil.createTempFile(".s");
        doReturn(selectedFile).when(mockChooser).getSelectedFile();

        // Verify the file chooser displays, and when "OK" is clicked, the file is opened
        edisen.openFileViaFileChooser();
        verify(mockChooser, times(1)).showOpenDialog(any());
        verify(edisen, times(1)).openFileForEditing(eq(selectedFile));
    }

    @Test
    public void testOpenViaFileChooser_openCancelled() {

        // Spy to do partial mocks on our Edisen instance
        edisen = Mockito.spy(TestableEdisen.create());
        doNothing().when(edisen).openFile(any());

        RTextFileChooser mockChooser = Mockito.mock(RTextFileChooser.class);
        doReturn(RTextFileChooser.CANCEL_OPTION).when(mockChooser).showOpenDialog(any());
        doReturn(mockChooser).when(edisen).getFileChooser();

        // Verify the file chooser displays, and when "OK" is clicked, the file is not opened
        edisen.openFileViaFileChooser();
        verify(mockChooser, times(1)).showOpenDialog(any());
        verify(edisen, times(0)).openFileForEditing(any());
    }

    @Test
    public void testRefreshLookAndFeel_noDialogsCreated() {
        edisen = TestableEdisen.create();
        Assertions.assertNotEquals(Theme.NORD, edisen.getTheme());
        edisen.refreshLookAndFeel(Theme.NORD);
        Assertions.assertEquals(Theme.NORD, edisen.getTheme());
    }

    @Test
    public void testRefreshLookAndFeel_dialogsCreated() {

        edisen = TestableEdisen.create();
        edisen.getOptionsDialog();
        edisen.find();
        edisen.replace();

        Assertions.assertNotEquals(Theme.NORD, edisen.getTheme());
        edisen.refreshLookAndFeel(Theme.NORD);
        Assertions.assertEquals(Theme.NORD, edisen.getTheme());
    }

    @Test
    public void testReplace() {

        edisen = TestableEdisen.create();

        // The first call creates the dialog
        edisen.replace();
        verify(edisen.mockReplaceDialog, times(1)).setVisible(eq(true));

        // The second call uses cached info, so run again for coverage of this scenario
        edisen.replace();
        verify(edisen.mockReplaceDialog, times(2)).setVisible(eq(true));
    }

    @Test
    public void testReplaceAfterFind() {

        edisen = TestableEdisen.create();

        // The "find" call creates the search context
        edisen.find();

        // The subsequent "replace" call reuses that search context but creates the Find dialog
        edisen.replace();
        verify(edisen.mockReplaceDialog, times(1)).setVisible(eq(true));
    }

    @Test
    public void testSaveAllDirtyFiles_success() throws IOException {

        edisen = TestableEdisen.create();

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

        edisen = TestableEdisen.create();

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

        edisen = TestableEdisen.create();

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
}
