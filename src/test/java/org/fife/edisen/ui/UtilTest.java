package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SwingRunnerExtension.class)
class UtilTest {

    @Test
    void testGetDefaultAssemblerCommandLine() {
        Assertions.assertNotNull(Util.getDefaultAssemblerCommandLine());
    }

    @Test
    void testGetDefaultEmulatorCommandLine() {
        Assertions.assertNotNull(Util.getDefaultEmulatorCommandLine());
    }

    @Test
    void testGetDefaultLinkerCommandLine() {
        Assertions.assertNotNull(Util.getDefaultLinkerCommandLine());
    }
}
