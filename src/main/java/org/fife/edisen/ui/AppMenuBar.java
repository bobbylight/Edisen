package org.fife.edisen.ui;

import org.fife.ui.app.MenuBar;

import javax.swing.*;
import java.util.ResourceBundle;

public class AppMenuBar extends MenuBar {

    public AppMenuBar(Edisen edisen) {

        ResourceBundle msg = edisen.getResourceBundle();

        JMenu fileMenu = createMenu(msg, "Menu.File");
        add(fileMenu);

        fileMenu.add(createMenuItem(edisen.getAction(Actions.OPEN_ACTION_KEY)));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem(edisen.getAction(Edisen.EXIT_ACTION_KEY)));

        JMenu helpMenu = createMenu(msg, "Menu.Help");
        add(helpMenu);

        helpMenu.add(createMenuItem(edisen.getAction(Edisen.HELP_ACTION_KEY)));
        helpMenu.addSeparator();
        helpMenu.add(createMenuItem(edisen.getAction(Edisen.ABOUT_ACTION_KEY)));
    }
}
