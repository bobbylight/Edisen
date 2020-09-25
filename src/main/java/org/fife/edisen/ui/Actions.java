package org.fife.edisen.ui;

import org.fife.ui.app.AppAction;

import java.awt.event.ActionEvent;

/**
 * Actions used by the application.
 */
public class Actions {

    public static final String COMPILE_ACTION_KEY = "compileAction";
    public static final String EMULATE_ACTION_KEY = "emulateAction";
    public static final String OPEN_ACTION_KEY = "openAction";
    public static final String OPTIONS_ACTION_KEY = "optionsAction";

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
            getApplication().compile();
        }
    }

    public static class EmulateAction extends AppAction<Edisen> {

        public EmulateAction(Edisen app) {
            super(app, "Action.Emulate");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            getApplication().emulate();
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
}
