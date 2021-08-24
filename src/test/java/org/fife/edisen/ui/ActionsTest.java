package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.fife.edisen.model.EdisenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class ActionsTest {

    @Test
    public void testBuildAction_success() throws IOException {

        Edisen edisen = TestUtil.mockEdisen();
        doReturn("echo 'assemble'").when(edisen).getAssemblerCommandLine();
        doReturn("echo 'link'").when(edisen).getLinkerCommandLine();
        EdisenProject project = new EdisenProject();
        project.setGameFile("test.edisen.json");
        project.setProjectFile(TestUtil.createTempFile().toPath());
        doReturn(project).when(edisen).getProject();

        Object result = new Actions.BuildAction(edisen).compileAndLink();
        Assertions.assertNull(result); // Nothing returned just yet
        verify(edisen, times(4)).log(anyString(), anyString());
    }

    @Test
    public void testBuildAction_assembleFails() throws IOException {

        Edisen edisen = TestUtil.mockEdisen();
        doReturn("unknown-command").when(edisen).getAssemblerCommandLine();
        EdisenProject project = new EdisenProject();
        project.setGameFile("test.edisen.json");
        project.setProjectFile(TestUtil.createTempFile().toPath());
        doReturn(project).when(edisen).getProject();

        Object result = new Actions.BuildAction(edisen).compileAndLink();
        Assertions.assertNull(result); // Nothing returned just yet
    }

    @Test
    public void testCloseAction() {

        Edisen edisen = TestUtil.mockEdisen();
        doReturn(0).when(edisen).getSelectedTabIndex();

        new Actions.CloseAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).closeTab(eq(0));
    }

    @Test
    public void testEmulateAction_success() throws IOException {

        Edisen edisen = TestUtil.mockEdisen();
        doReturn("echo 'emulate'").when(edisen).getEmulatorCommandLine();
        EdisenProject project = new EdisenProject();
        project.setGameFile("test.edisen.json");
        project.setProjectFile(TestUtil.createTempFile().toPath());
        doReturn(project).when(edisen).getProject();

        new Actions.EmulateAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).log(anyString(), anyString());
    }

    @Test
    public void testFindAction() {

        Edisen edisen = TestUtil.mockEdisen();

        new Actions.FindAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).find();
    }

    @Test
    public void testGoToAction() {

        Edisen edisen = TestUtil.mockEdisen();

        new Actions.GoToAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).goToLine();
    }

    @Test
    public void testOpenProjectAction() {

        Edisen edisen = TestUtil.mockEdisen();

        new Actions.OpenProjectAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).openProjectViaFileChooser();
    }

    @Test
    public void testReplaceAction() {

        Edisen edisen = TestUtil.mockEdisen();

        new Actions.ReplaceAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).replace();
    }

    @Test
    public void testSaveAction() {

        Edisen edisen = TestUtil.mockEdisen();

        new Actions.SaveAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).saveCurrentFile();
    }

    @Test
    public void testSaveAsAction() {

        Edisen edisen = TestUtil.mockEdisen();

        new Actions.SaveAsAction(edisen).actionPerformed(null);
        verify(edisen, times(1)).saveCurrentFileAs();
    }
}
