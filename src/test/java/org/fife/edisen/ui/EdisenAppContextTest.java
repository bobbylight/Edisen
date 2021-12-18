package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.fife.ui.app.themes.NativeTheme;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doReturn;

public class EdisenAppContextTest {

    private EdisenAppContext context;

    @BeforeEach
    public void setUp() {
        context = new EdisenAppContext();
    }

    @Test
    public void testGetPreferencesClassName() {
        Assertions.assertNotNull(context.getPreferencesClassName());
    }

    @Test
    public void testGetPreferencesDir() {
        Assertions.assertNotNull(context.getPreferencesDir());
    }

    @Test
    public void testGetPreferencesFileName() {
        Assertions.assertNotNull(context.getPreferencesFileName());
    }

    @Test
    @Disabled("Can't call this without starting the app")
    public void testCreateApplication() {
        // Do nothing (comment for Sonar)
    }

    @Test
    public void testPopulateFromApplication() throws IOException {

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        EdisenPrefs prefs = new EdisenPrefs();

        File tempFile = TestUtil.createTempFile();

        // Can use the same set for files and projects
        List<FileLocation> mockFileLocations = Collections.singletonList(FileLocation.create(tempFile));

        doReturn(mockFileLocations).when(mockEdisen).getRecentFiles();
        doReturn(mockFileLocations).when(mockEdisen).getRecentProjects();
        doReturn(new NativeTheme()).when(mockEdisen).getTheme();

        context.populatePrefsFromApplication(mockEdisen, prefs);

        String[] expectedFilesAndProjects = mockFileLocations.stream()
                .map(FileLocation::getFileFullPath)
                .toArray(String[]::new);

        Assertions.assertArrayEquals(expectedFilesAndProjects, prefs.recentFiles);
        Assertions.assertArrayEquals(expectedFilesAndProjects, prefs.recentProjects);
        Assertions.assertEquals(NativeTheme.NAME, prefs.appTheme);
    }

    @Test
    public void testSavePreferences() throws IOException {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        // Can use the same set for files and projects
        File tempFile = TestUtil.createTempFile();
        List<FileLocation> mockFileLocations = Collections.singletonList(FileLocation.create(tempFile));

        doReturn(mockFileLocations).when(mockEdisen).getRecentFiles();
        doReturn(mockFileLocations).when(mockEdisen).getRecentProjects();
        doReturn(new NativeTheme()).when(mockEdisen).getTheme();

        context.savePreferences(mockEdisen);
    }
}
