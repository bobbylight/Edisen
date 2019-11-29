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
    private Path root;
    private String name;

    public static EdisenProject fromFile(Path path) throws IOException {
        EdisenProject project = load(Files.newBufferedReader(path));
        project.setRoot(path);
        return project;
    }

    public static EdisenProject load(Reader r) throws IOException {

        EdisenProject project;

        try (BufferedReader br = new BufferedReader(r)) {
            project = new ObjectMapper().readerFor(EdisenProject.class).readValue(br);
        }

        return project;
    }

    public String getName() {
        return name;
    }

    public Path getRoot() {
        return root;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRoot(Path root) {
        this.root = root;
    }
}
