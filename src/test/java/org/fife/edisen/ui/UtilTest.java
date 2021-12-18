package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SwingRunnerExtension.class)
public class UtilTest {

    @Test
    public void testGetDefaultAssemblerCommandLine() {
        Assertions.assertNotNull(Util.getDefaultAssemblerCommandLine());
    }

    @Test
    public void testGetDefaultEmulatorCommandLine() {
        Assertions.assertNotNull(Util.getDefaultEmulatorCommandLine());
    }

    @Test
    public void testGetDefaultLinkerCommandLine() {
        Assertions.assertNotNull(Util.getDefaultLinkerCommandLine());
    }
}
