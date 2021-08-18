package org.fife.edisen.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EdisenProjectTest {

    @Test
    public void testFromFile_success() throws IOException {

        String projectJson = "{\n" +
            "  \"name\": \"Test Project\",\n" +
            "  \"gameFile\": \"test.s\",\n" +
            "  \"assembleCommandLine\": \"ca65 test.nes\",\n" +
            "  \"linkCommandLine\": \"ld65 test.o\"\n" +
            "}\n";

        Path tempProject = Files.createTempFile("edisen", ".json");
        Files.writeString(tempProject, projectJson);

        try {
            EdisenProject project = EdisenProject.fromFile(tempProject);
            Assertions.assertEquals("Test Project", project.getName());
            Assertions.assertEquals("test.s", project.getGameFile());
            Assertions.assertEquals("ca65 test.nes", project.getAssemblerCommandLine());
            Assertions.assertEquals("ld65 test.o", project.getLinkCommandLine());
            Assertions.assertEquals(tempProject, project.getProjectFile());
        } finally {
            Files.delete(tempProject);
        }
    }

    @Test
    public void testFromFile_failure() throws IOException {

        // Empty file
        Path tempProject = Files.createTempFile("edisen", ".json");

        try {
            Assertions.assertThrows(IOException.class, () -> {
                EdisenProject.fromFile(tempProject);
            });
        } finally {
            Files.delete(tempProject);
        }
    }

    @Test
    public void testGetSetAssembleCommandLine() {
        EdisenProject project = new EdisenProject();
        Assertions.assertNull(project.getAssemblerCommandLine());
        project.setAssemblerCommandLine("foo");
        Assertions.assertEquals("foo", project.getAssemblerCommandLine());
    }

    @Test
    public void testGetSetGameFile() {
        EdisenProject project = new EdisenProject();
        Assertions.assertNull(project.getGameFile());
        project.setGameFile("test.nes");
        Assertions.assertEquals("test.nes", project.getGameFile());
    }

    @Test
    public void testGetSetLinkCommandLine() {
        EdisenProject project = new EdisenProject();
        Assertions.assertNull(project.getLinkCommandLine());
        project.setLinkCommandLine("foo");
        Assertions.assertEquals("foo", project.getLinkCommandLine());
    }

    @Test
    public void testGetSetName() {
        EdisenProject project = new EdisenProject();
        Assertions.assertNull(project.getName());
        project.setName("Test Project");
        Assertions.assertEquals("Test Project", project.getName());
    }

    @Test
    public void testGetSetProjectFile() {
        EdisenProject project = new EdisenProject();
        Assertions.assertNull(project.getProjectFile());
        project.setProjectFile(Path.of("test.json"));
        Assertions.assertEquals(Path.of("test.json"), project.getProjectFile());
    }
}
