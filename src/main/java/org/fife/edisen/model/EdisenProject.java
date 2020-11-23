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

    public static EdisenProject fromFile(Path path) throws IOException {
        EdisenProject project = load(Files.newBufferedReader(path));
        project.setProjectFile(path);
        return project;
    }

    public static EdisenProject load(Reader r) throws IOException {

        EdisenProject project;

        try (BufferedReader br = new BufferedReader(r)) {
            project = new ObjectMapper().readerFor(EdisenProject.class).readValue(br);
        }

        return project;
    }

    public String getGameFile() {
        return gameFile;
    }

    public String getName() {
        return name;
    }

    public Path getProjectFile() {
        return projectFile;
    }

    public void setGameFile(String gameFile) {
        this.gameFile = gameFile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProjectFile(Path projectFile) {
        this.projectFile = projectFile;
    }
}
