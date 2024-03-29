package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.fife.edisen.ui.model.EdisenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(SwingRunnerExtension.class)
class RecentFileManagerTest {

    private Edisen edisen;

    @BeforeEach
    public void setUp() {
        edisen = Mockito.mock(Edisen.class);
    }

    private static String createTempFile() throws IOException {
        return TestUtil.createTempFile().getAbsolutePath();
    }

    @Test
    void testConstructor_nullFileList() {
        RecentFileManager rfm = new RecentFileManager(edisen, null);
        Assertions.assertEquals(0, rfm.getRecentFiles().size());
    }

    @Test
    void testConstructor_emptyFileList() {
        RecentFileManager rfm = new RecentFileManager(edisen, Collections.emptyList());
        Assertions.assertEquals(0, rfm.getRecentFiles().size());
    }

    @Test
    void testConstructor_nonEmptyFileListButFilesNoLongerExist() {
        List<String> recentFiles = Collections.singletonList("does-not-exist.txt");
        RecentFileManager rfm = new RecentFileManager(edisen, recentFiles);
        Assertions.assertEquals(0, rfm.getRecentFiles().size());
    }

    @Test
    void testConstructor_nonEmptyFileListWithFilesThatExist() throws IOException {
        List<String> recentFiles = Collections.singletonList(createTempFile());
        RecentFileManager rfm = new RecentFileManager(edisen, recentFiles);
        Assertions.assertEquals(recentFiles.size(), rfm.getRecentFiles().size());
    }

    @Test
    void testAddFile_addingTheSameFileTwiceDoesNotDuplicate() throws IOException {

        String file = createTempFile();

        List<String> recentFiles = Collections.singletonList(file);
        RecentFileManager rfm = new RecentFileManager(edisen, recentFiles);
        Assertions.assertEquals(1, rfm.getRecentFiles().size());

        EdisenProject project = new EdisenProject();
        project.setProjectFile(new File(file).toPath());
        PropertyChangeEvent e = new PropertyChangeEvent(edisen, Edisen.PROPERTY_PROJECT, null, project);
        rfm.propertyChange(e);

        // Still just one entry
        Assertions.assertEquals(1, rfm.getRecentFiles().size());
    }

    @Test
    void testAddFile_maxFileCount() throws IOException {

        List<String> recentFiles = new ArrayList<>();
        for (int i = 0; i < 76; i++) {
            recentFiles.add(createTempFile());
        }

        RecentFileManager rfm = new RecentFileManager(edisen, recentFiles);
        Assertions.assertEquals(rfm.getMaxFileCount(), rfm.getRecentFiles().size());
    }

    @Test
    void testProjectOpenedAddsAnEntry() throws IOException {

        RecentFileManager rfm = new RecentFileManager(edisen, Collections.emptyList());
        Assertions.assertEquals(0, rfm.getRecentFiles().size());

        EdisenProject project = new EdisenProject();
        project.setProjectFile(Files.createTempFile("edisenProject", ".tmp"));
        PropertyChangeEvent e = new PropertyChangeEvent(edisen, Edisen.PROPERTY_PROJECT, null, project);
        rfm.propertyChange(e);
        Assertions.assertEquals(1, rfm.getRecentFiles().size());
    }
}
