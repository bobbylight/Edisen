package org.fife.edisen.ui;

import org.fife.edisen.ui.model.EdisenProject;
import org.fife.ui.RecentFilesMenu;
import org.fife.ui.UIUtil;
import org.fife.ui.app.MenuBar;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class AppMenuBar extends MenuBar<Edisen> implements PropertyChangeListener {

    private RecentFilesMenu recentProjectsMenu;
    private RecentFilesMenu recentFilesMenu;

    public AppMenuBar(Edisen edisen) {
        super(edisen);
    }

    @Override
    protected void initializeUI() {

        Edisen edisen = getApplication();
        edisen.addPropertyChangeListener(Edisen.PROPERTY_PROJECT, this);
        edisen.addPropertyChangeListener(Edisen.PROPERTY_FILE_OPENED, this);
        ResourceBundle msg = edisen.getResourceBundle();

        JMenu fileMenu = createMenu(msg, "Menu.File");
        add(fileMenu);

        fileMenu.add(createMenuItem(edisen.getAction(Actions.OPEN_PROJECT_ACTION_KEY)));
        fileMenu.add(createMenuItem(edisen.getAction(Actions.CLOSE_PROJECT_ACTION_KEY)));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem(edisen.getAction(Actions.OPEN_ACTION_KEY)));
        fileMenu.add(createMenuItem(edisen.getAction(Actions.SAVE_ACTION_KEY)));
        fileMenu.add(createMenuItem(edisen.getAction(Actions.SAVE_AS_ACTION_KEY)));
        fileMenu.add(createMenuItem(edisen.getAction(Actions.CLOSE_ACTION_KEY)));
        fileMenu.addSeparator();

        recentProjectsMenu = new RecentFilesMenu(edisen.getString("Menu.RecentProjects")) {
            @Override
            protected Action createOpenAction(String fileFullPath) {
                OpenFileFromHistoryAction action =
                    new OpenFileFromHistoryAction(edisen);
                action.setName(UIUtil.getDisplayPathForFile(AppMenuBar.this, fileFullPath));
                action.setFileFullPath(fileFullPath);
                return action;
            }
        };
        fileMenu.add(recentProjectsMenu);
        recentFilesMenu = new RecentFilesMenu(edisen.getString("Menu.RecentFiles")) {
            @Override
            protected Action createOpenAction(String fileFullPath) {
                OpenFileFromHistoryAction action =
                    new OpenFileFromHistoryAction(edisen);
                action.setName(UIUtil.getDisplayPathForFile(AppMenuBar.this, fileFullPath));
                action.setFileFullPath(fileFullPath);
                return action;
            }
        };
        fileMenu.add(recentFilesMenu);
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

    /**
     * Adds the file specified to the file history.
     *
     * @param fileFullPath Full path to a file to add to the file history in
     *        the File menu.
     */
    private void addProjectToProjectHistory(String fileFullPath) {
//        // We don't remember just-created empty text files.
//        // Also, due to the Preferences API needing a non-null key for all
//        // values, a "-" filename means no files were found for the file
//        // history.  So, we won't add this file in either.
//        if (fileFullPath.endsWith(File.separatorChar + rtext.getNewFileName()) ||
//            fileFullPath.equals("-")) {
//            return;
//        }
        recentProjectsMenu.addFileToFileHistory(fileFullPath);
    }

    @Override
    public void addNotify() {

        super.addNotify();

        List<FileLocation> recentFiles = getApplication().getRecentProjects();
        List<FileLocation> recentFilesCopy = new ArrayList<>(recentFiles);
        Collections.reverse(recentFilesCopy);
        for (FileLocation file : recentFilesCopy) {
            recentProjectsMenu.addFileToFileHistory(file.getFileFullPath());
        }
    }

    /**
     * Returns the list of files in the "recent files" menu.
     *
     * @return The list of files in the "recent files" menu.
     */
    public List<String> getFileHistory() {
        return recentProjectsMenu.getFileHistory();
    }

    /**
     * Called whenever a property changes on a component we're listening to.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();

        switch (prop) {

            case Edisen.PROPERTY_PROJECT:
                EdisenProject project = (EdisenProject)e.getNewValue();
                if (project != null) { // Ensure they're not closing a project
                    addProjectToProjectHistory(project.getProjectFile().toString());
                }
                break;

            case Edisen.PROPERTY_FILE_OPENED:
                break;
        }
    }
}
