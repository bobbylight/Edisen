package org.fife.edisen.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.fife.edisen.TestUtil;
import org.fife.edisen.ui.model.EdisenProject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SwingRunnerExtension.class)
class ProjectTreeTest {

    @Test
    void testConstructor_listensForProjectChanges() {

        Edisen mockEdisen = TestUtil.mockEdisen();

        new ProjectTree(mockEdisen);
        verify(mockEdisen, times(1)).addPropertyChangeListener(eq(Edisen.PROPERTY_PROJECT), any());
    }

    @Test
    void testPossiblyOpenFileForEditing_noSelection() {

        Edisen mockEdisen = TestUtil.mockEdisen();
        ProjectTree tree = new ProjectTree(mockEdisen);
        tree.possiblyOpenFileForEditing();
        verify(mockEdisen, times(0)).openFileForEditing(any()); // No selection
    }

    @Test
    @Disabled("Currently no programmatic way to do this")
    void testPossiblyOpenFileForEditing_selection() {
        // Do nothing (comment for Sonar)
    }

    @Test
    void testOpenProject_contentsDisplayed() throws IOException {

        Edisen mockEdisen = TestableEdisen.create();
        ProjectTree tree = new ProjectTree(mockEdisen);

        // Open a project, which fires the property change event this tree listens for.
        EdisenProject project = new EdisenProject();
        project.setGameFile("game.s");
        project.setName("test-project");
        File file = TestUtil.createTempFile(".edisen.json", new ObjectMapper().writeValueAsString(project));
        mockEdisen.openFile(file);
    }

    @Test
    void testCloseProject_contentsRemoved() throws IOException {

        Edisen mockEdisen = TestableEdisen.create();
        ProjectTree tree = new ProjectTree(mockEdisen);

        // Open a project, which fires the property change event this tree listens for.
        EdisenProject project = new EdisenProject();
        project.setGameFile("game.s");
        project.setName("test-project");
        File file = TestUtil.createTempFile(".edisen.json", new ObjectMapper().writeValueAsString(project));
        mockEdisen.openFile(file);

        // Close the prior project
        mockEdisen.openFile(null);
    }
}
