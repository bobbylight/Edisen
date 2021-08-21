package org.fife.edisen.ui;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OpenFileFromHistoryActionTest {

    @Test
    public void testActionPerformed() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        OpenFileFromHistoryAction action = new OpenFileFromHistoryAction(mockEdisen);
        action.setFileFullPath("test");

        action.actionPerformed(null);
        verify(mockEdisen, times(1)).openFile(any(File.class));
    }
}
