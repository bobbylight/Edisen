package org.fife.edisen.ui;

import org.fife.ui.ComponentMover;
import org.fife.ui.app.MenuBar;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

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

        JMenu editMenu = createMenu(msg, "Menu.Edit");
        add(editMenu);

        editMenu.add(createMenuItem(RSyntaxTextArea.getAction(RSyntaxTextArea.UNDO_ACTION)));
        editMenu.add(createMenuItem(RSyntaxTextArea.getAction(RSyntaxTextArea.REDO_ACTION)));
        editMenu.addSeparator();
        editMenu.add(createMenuItem(RSyntaxTextArea.getAction(RSyntaxTextArea.CUT_ACTION)));
        editMenu.add(createMenuItem(RSyntaxTextArea.getAction(RSyntaxTextArea.COPY_ACTION)));
        editMenu.add(createMenuItem(RSyntaxTextArea.getAction(RSyntaxTextArea.PASTE_ACTION)));
        editMenu.add(createMenuItem(RSyntaxTextArea.getAction(RSyntaxTextArea.DELETE_ACTION)));

        editMenu.addSeparator();
        editMenu.add(createMenuItem(edisen.getAction(Actions.OPTIONS_ACTION_KEY)));

        JMenu searchMenu = createMenu(msg, "Menu.Search");
        add(searchMenu);

        searchMenu.add(createMenuItem(edisen.getAction(Actions.FIND_ACTION_KEY)));
        searchMenu.add(createMenuItem(edisen.getAction(Actions.REPLACE_ACTION_KEY)));

        searchMenu.addSeparator();
        searchMenu.add(createMenuItem(edisen.getAction(Actions.GOTO_ACTION_KEY)));

        JMenu compileMenu = createMenu(msg, "Menu.Compile");
        add(compileMenu);

        compileMenu.add(createMenuItem(edisen.getAction(Actions.COMPILE_ACTION_KEY)));
        compileMenu.add(createMenuItem(edisen.getAction(Actions.EMULATE_ACTION_KEY)));

        JMenu helpMenu = createMenu(msg, "Menu.Help");
        add(helpMenu);

        helpMenu.add(createMenuItem(edisen.getAction(Edisen.HELP_ACTION_KEY)));
        helpMenu.addSeparator();
        helpMenu.add(createMenuItem(edisen.getAction(Edisen.ABOUT_ACTION_KEY)));
    }
}
