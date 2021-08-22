package org.fife.edisen.ui.options;

import org.fife.edisen.TestUtil;
import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SwingRunnerExtension.class)
public class EdisenOptionsDialogTest {

    @Test
    public void testConstructor() {

        Edisen edisen = TestUtil.mockEdisen();

        EdisenOptionsDialog dialog = new EdisenOptionsDialog(null);
        dialog.setApplication(edisen);

    }
}
