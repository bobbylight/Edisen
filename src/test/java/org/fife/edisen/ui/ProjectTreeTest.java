package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SwingRunnerExtension.class)
public class ProjectTreeTest {

    @Test
    public void testConstructor_listensForProjectChanges() throws IOException {

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        Path projectRoot = Files.createTempDirectory("edisenTest");

        new ProjectTree(mockEdisen, projectRoot);
        verify(mockEdisen, times(1)).addPropertyChangeListener(eq(Edisen.PROPERTY_PROJECT), any());
    }

    @Test
    @Disabled("Bug in FileSystemTree.setSelectedFile - does not work if not showing FS roots")
    public void testPossiblyOpenFileForEditing_fileSelected() throws IOException {

        File file = File.createTempFile("edisenTest", ".tmp");
        file.deleteOnExit();

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        Path projectRoot = file.getParentFile().toPath();

        ProjectTree tree = new ProjectTree(mockEdisen, projectRoot);
        Assertions.assertTrue(tree.setSelectedFile(file));

        tree.possiblyOpenFileForEditing();
        verify(mockEdisen, times(1)).openFileForEditing(any(File.class));
    }
}
