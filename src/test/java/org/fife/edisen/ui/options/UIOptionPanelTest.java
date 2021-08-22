package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

@ExtendWith(SwingRunnerExtension.class)
public class UIOptionPanelTest {

    @Test
    public void testGetTopJComponent() {
        Edisen edisen = Mockito.mock(Edisen.class);
        UIOptionPanel panel = new UIOptionPanel(edisen);
        Assertions.assertNotNull(panel.getTopJComponent());
    }

    @Test
    public void testRestoreDefaults_nothingChanged() {
        Edisen edisen = Mockito.mock(Edisen.class);
        UIOptionPanel panel = new UIOptionPanel(edisen);
        Assertions.assertFalse(panel.restoreDefaults());
    }
}
