package org.fife.edisen.model;

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
    private String assembleCommandLine;
    private String linkCommandLine;
    private String emulatorCommandLine;


    public static EdisenProject fromFile(Path path) throws IOException {
        EdisenProject project = load(Files.newBufferedReader(path));
        project.setProjectFile(path);
        return project;
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

    public String getAssembleCommandLine() {
        return assembleCommandLine;
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
     * Saves this project file.  Note this only saves the project JSON file;
     * source files, etc. are not saved or updated.
     *
     * @throws IOException If an IO error occurs.
     * @see #load(Reader)
     */
    public void save() throws IOException {
        new ObjectMapper().writerFor(EdisenProject.class).writeValue(getProjectFile().toFile(), this);
    }

    public void setAssembleCommandLine(String assembleCommandLine) {
        this.assembleCommandLine = assembleCommandLine;
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
