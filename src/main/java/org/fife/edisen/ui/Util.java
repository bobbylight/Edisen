package org.fife.edisen.ui;

import org.fife.io.ProcessRunner;
import org.fife.io.ProcessRunnerOutputListener;
import org.fife.ui.OS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * Obligatory utility methods.
 */
public final class Util {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    /**
     * Private constructor to prevent instantiation.
     */
    private Util() {
        // Do nothing (comment for Sonar)
    }

    public static ProcessRunner createProcessRunner(Edisen app, String logKey, String commandLine) {

        List<String> command = new ArrayList<>();

        if (OS.get() == OS.WINDOWS) {
            command.add("cmd.exe");
            command.add("/c");
        }
        else {
            command.add("/bin/sh");
            command.add("-c");
        }

        command.add(commandLine);
        ProcessRunner pr = new ProcessRunner(command.toArray(new String[0]));

        pr.setDirectory(app.getProject().getProjectFile().getParent().toFile());

        String dateTime = DATE_TIME_FORMATTER.format(LocalDateTime.now());
        app.log("stdin", app.getString(logKey, dateTime));
        app.log("stdout", pr.getCommandLineString());

        pr.setOutputListener(new ProcessRunnerOutputListener() {
            @Override
            public void outputWritten(Process p, String output, boolean stdout) {
                app.log(stdout ? "stdout" : "stderr", output);
            }

            @Override
            public void processCompleted(Process p, int rc, Throwable e) {
                app.log("stdin", app.getString("Log.ProcessCompleted", rc));
            }
        });

        return pr;
    }

    public static String getDefaultAssemblerCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            // For some reason the combined cl65 doesn't seem to generate the same
            // .o files, and the generated .nes files don't run in Nestopia
            //return "cl65 -o ${rom} ${gameFile} -t nes";
            return "ca65 -o ${objfile} ${gameFile} -t nes";
            //return "ca65 -o ${objfile} ${gameFile} -t nes";
        }
        return "D:/cc65-snapshot-win32/bin/ca65 -o ${objfile} ${gameFile} -t nes";
    }

    public static String getDefaultLinkerCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            return "ld65 -o ${rom} ${objfile} -t nes";
            //return "ld65 -o ${rom} ${objfile} -C nesfile.ini";
        }
        return "D:/cc65-snapshot-win32/bin/ld65 -o ${rom} ${objfile} -t nes";
    }

    public static String getDefaultEmulatorCommandLine() {
        if (OS.get() == OS.MAC_OS_X) {
            return "open -a Nestopia ${rom}";
        }
        return "D:/emulation/nes/emulators/nestopia/nestopia.exe ${rom}";
    }
}
