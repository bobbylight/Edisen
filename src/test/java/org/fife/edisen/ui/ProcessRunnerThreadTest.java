package org.fife.edisen.ui;

import org.fife.io.ProcessRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ProcessRunnerThreadTest {

    @Test
    void testConstruct() {

        ProcessRunner pr = Mockito.mock(ProcessRunner.class);

        Object result = new ProcessRunnerThread(pr).construct();
        Assertions.assertNull(result); // Doesn't return anything, just runs the process

        verify(pr, times(1)).run();
    }
}
