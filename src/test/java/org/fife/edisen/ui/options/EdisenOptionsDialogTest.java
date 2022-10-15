package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.fife.edisen.ui.TestableEdisen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SwingRunnerExtension.class)
class EdisenOptionsDialogTest {

    @Test
    void testConstructor() {

        Edisen edisen = TestableEdisen.create();

        EdisenOptionsDialog dialog = new EdisenOptionsDialog(null);
        dialog.setApplication(edisen);

    }
}
