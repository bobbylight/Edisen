package org.fife.edisen.ui;

import org.fife.io.ProcessRunner;
import org.fife.io.ProcessRunnerOutputListener;
import org.fife.ui.OS;
import org.fife.ui.app.AppAction;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * Actions used by the application.
 */
public class Actions {

    public static final String COMPILE_ACTION_KEY = "compileAction";
    public static final String EMULATE_ACTION_KEY = "emulateAction";
    public static final String FIND_ACTION_KEY = "findAction";
    public static final String GOTO_ACTION_KEY = "goToAction";
    public static final String OPEN_ACTION_KEY = "openAction";
    public static final String OPTIONS_ACTION_KEY = "optionsAction";
    public static final String REPLACE_ACTION_KEY = "replaceAction";
    public static final String SAVE_ACTION_KEY = "saveAction";
    public static final String SAVE_AS_ACTION_KEY = "saveAsAction";

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    /**
     * Private constructor to prevent instantiation.
     */
    private Actions() {
        // Do nothing (comment for Sonar)
    }

    public static class CompileAction extends AppAction<Edisen> {

        public CompileAction(Edisen app) {
            super(app, "Action.Compile");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String rom = "game.nes";
            String objFile = "game.o";
            String mainGameFile = getApplication().getProject().getGameFile();

            Edisen app = getApplication();
            String commandLine = app.getPreferences().assemblerCommandLine;
            commandLine = commandLine.replace("${rom}", '"' + rom + '"')
                .replace("${gameFile}", mainGameFile)
                .replace("${objfile}", objFile);

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
            app.log("stdin", app.getString("Log.CompilingAt", dateTime));
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

            // TODO: Cache this and kill it if the user manually stops the process
            new ProcessRunnerThread(pr).start();
        }
    }

    public static class EmulateAction extends AppAction<Edisen> {

        public EmulateAction(Edisen app) {
            super(app, "Action.Emulate");
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            Edisen app = getApplication();
            EdisenPrefs prefs = app.getPreferences();

            String rom = "./game.nes"; // "relative path" needed for Windows Nestopia

            String commandLine = prefs.emulatorCommandLine;
            commandLine = commandLine.replace("${rom}", '"' + rom + '"');

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
            app.log("stdin", app.getString("Log.RunningProgram", dateTime));
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

            // TODO: Cache this and kill it if the user manually stops the process
            new ProcessRunnerThread(pr).start();
        }
    }

    public static class FindAction extends AppAction<Edisen> {

        public FindAction(Edisen app) {
            super(app, "Action.Find");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getApplication().find();
        }
    }

    public static class GoToAction extends AppAction<Edisen> {

        public GoToAction(Edisen app) {
            super(app, "Action.GoTo");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getApplication().goToLine();
        }
    }

    public static class OpenAction extends AppAction<Edisen> {

        public OpenAction(Edisen app) {
            super(app, "Action.Open");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getApplication().openProject();
        }
    }

    public static class ReplaceAction extends AppAction<Edisen> {

        public ReplaceAction(Edisen app) {
            super(app, "Action.Replace");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getApplication().replace();
        }
    }

    public static class SaveAction extends AppAction<Edisen> {

        public SaveAction(Edisen app) {
            super(app, "Action.Save");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getApplication().saveCurrentFile();
        }
    }

    public static class SaveAsAction extends AppAction<Edisen> {

        public SaveAsAction(Edisen app) {
            super(app, "Action.SaveAs");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getApplication().saveCurrentFileAs();
        }
    }
}
