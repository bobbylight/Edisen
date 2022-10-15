package org.fife.edisen.ui;

import org.fife.ui.rtextarea.RTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.swing.*;
import java.util.ResourceBundle;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SwingRunnerExtension.class)
class AppMenuBarTest {

    @BeforeAll
    public static void setUpOnce() {
        new RTextArea(); // Needed for text area-related actions
    }

    @Test
    void testGetFileHistory() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        ResourceBundle msg = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");
        doReturn(msg).when(mockEdisen).getResourceBundle();
        Action a = new Actions.BuildAction(mockEdisen);
        doReturn(a).when(mockEdisen).getAction(anyString());

        AppMenuBar mb = new AppMenuBar(mockEdisen);
        Assertions.assertEquals(0, mb.getFileHistory().size());
    }
}
