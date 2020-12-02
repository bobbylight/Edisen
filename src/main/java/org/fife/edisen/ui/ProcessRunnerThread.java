package org.fife.edisen.ui;

import org.fife.io.ProcessRunner;
import org.fife.ui.GUIWorkerThread;

/**
 * Runs a process in a separate, killable thread.  The process is
 * terminated as cleanly as possible if the thread is terminated.
 */
class ProcessRunnerThread extends GUIWorkerThread<Object> {

    private final ProcessRunner pr;

    ProcessRunnerThread(ProcessRunner pr) {
        this.pr = pr;
    }

    @Override
    public Object construct() {
        pr.run();
        return null;
    }
}
