package org.fife.edisen.ui;

import org.fife.edisen.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

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

        new ProjectTree(mockEdisen);
        verify(mockEdisen, times(1)).addPropertyChangeListener(eq(Edisen.PROPERTY_PROJECT), any());
    }
}
