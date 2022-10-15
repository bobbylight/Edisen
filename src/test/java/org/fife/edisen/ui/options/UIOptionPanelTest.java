package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.fife.edisen.ui.TestableEdisen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

@ExtendWith(SwingRunnerExtension.class)
class UIOptionPanelTest {

    @Test
    void testGetTopJComponent() {
        Edisen edisen = Mockito.mock(Edisen.class);
        UIOptionPanel panel = new UIOptionPanel(edisen);
        Assertions.assertNotNull(panel.getTopJComponent());
    }

    @Test
    void testRestoreDefaults_nothingChanged() {

        Edisen edisen = TestableEdisen.create();
        UIOptionPanel panel = new UIOptionPanel(edisen);
        panel.setValues(edisen);

        Assertions.assertFalse(panel.restoreDefaults());
    }
}
