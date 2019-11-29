package org.fife.edisen.ui;

import org.fife.ui.app.AppAction;

import java.awt.event.ActionEvent;

/**
 * Actions used by the application.
 */
public class Actions {

    public static final String OPEN_ACTION_KEY = "openAction";

    /**
     * Private constructor to prevent instantiation.
     */
    private Actions() {
        // Do nothing (comment for Sonar)
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
