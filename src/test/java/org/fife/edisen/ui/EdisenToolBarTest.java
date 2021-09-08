package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.swing.*;

import java.awt.event.ActionEvent;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SwingRunnerExtension.class)
public class EdisenToolBarTest {

    @Test
    public void testConstructor() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);
        Action a = new AbstractAction("testAction") {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Do nothing (comment for Sonar)
            }
        };
        doReturn(a).when(mockEdisen).getAction(anyString());

        EdisenToolBar toolBar = new EdisenToolBar(mockEdisen);
        Assertions.assertEquals(12, toolBar.getComponentCount());
    }
}
