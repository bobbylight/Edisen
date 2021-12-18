package org.fife.edisen.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Metadata about an Edisen project.
 */
public class EdisenProject {

    @JsonIgnore
    private Path projectFile;
    private String name;

    private String gameFile;
    private String assemblerCommandLine;
    private String linkCommandLine;
    private String emulatorCommandLine;


    public static EdisenProject fromFile(Path path) throws IOException {
        EdisenProject project = load(Files.newBufferedReader(path));
        project.setProjectFile(path);
        return project;
    }

    public String getAssemblerCommandLine() {
        return assemblerCommandLine;
    }

    public String getGameFile() {
        return gameFile;
    }

    public String getLinkCommandLine() {
        return linkCommandLine;
    }

    public String getName() {
        return name;
    }

    public Path getProjectFile() {
        return projectFile;
    }

    /**
     * Loads a project.
     *
     * @param r The project to load.
     * @return The project.
     * @throws IOException If an IO error occurs.
     * @see #save()
     */
    public static EdisenProject load(Reader r) throws IOException {

        EdisenProject project;

        try (BufferedReader br = new BufferedReader(r)) {
            project = new ObjectMapper().readerFor(EdisenProject.class).readValue(br);
        }

        return project;
    }

    /**
     * Saves this project file.  Note this only saves the project JSON file;
     * source files, etc. are not saved or updated.
     *
     * @throws IOException If an IO error occurs.
     * @see #load(Reader)
     */
    public void save() throws IOException {
        new ObjectMapper().writerFor(EdisenProject.class)
            .withDefaultPrettyPrinter()
            .writeValue(getProjectFile().toFile(), this);
    }

    public void setAssemblerCommandLine(String assemblerCommandLine) {
        this.assemblerCommandLine = assemblerCommandLine;
    }

    public void setGameFile(String gameFile) {
        this.gameFile = gameFile;
    }

    public void setLinkCommandLine(String linkCommandLine) {
        this.linkCommandLine = linkCommandLine;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectFile(Path projectFile) {
        this.projectFile = projectFile;
    }

    public String getEmulatorCommandLine() {
        return emulatorCommandLine;
    }

    public void setEmulatorCommandLine(String emulatorCommandLine) {
        this.emulatorCommandLine = emulatorCommandLine;
    }
}
