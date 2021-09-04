package org.fife.edisen.ui;

import org.fife.edisen.model.EdisenProject;
import org.fife.ui.rtextfilechooser.FileSystemTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/**
 * A tree view that shows all files in the open project.
 */
class ProjectTree extends JPanel {

    private final Edisen edisen;
    private final FileSystemTree tree;
    private final NoProjectLoadedPanel nothingLoadedPanel;

    ProjectTree(Edisen edisen) {

        this.edisen = edisen;

        // Show the background (JViewport) color
        setLayout(new BorderLayout());
        setOpaque(false);

        tree = new FileSystemTree();
        tree.setRootVisible(false);
        tree.setShowsRootHandles(false);

        nothingLoadedPanel = new NoProjectLoadedPanel();
        add(nothingLoadedPanel);

        Listener listener = new Listener();
        tree.addMouseListener(listener);
        edisen.addPropertyChangeListener(Edisen.PROPERTY_PROJECT, listener);
    }

    void possiblyOpenFileForEditing() {
        File selection = tree.getSelectedFile();
        if (selection != null) {
            edisen.openFileForEditing(selection);
        }
    }

    private void showProject(EdisenProject project) {

        if (project != null) {
            remove(nothingLoadedPanel);
            add(tree);
            File projectRoot = project.getProjectFile().getParent().toFile();
            tree.setRoot(projectRoot);
        }
        else {
            remove(tree);
            add(nothingLoadedPanel);
        }

        revalidate();
        repaint();
    }

    private class Listener extends MouseAdapter implements PropertyChangeListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                possiblyOpenFileForEditing();
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            if (Edisen.PROPERTY_PROJECT.equals(e.getPropertyName())) {
                showProject((EdisenProject)e.getNewValue());
            }
        }
    }

    class NoProjectLoadedPanel extends JPanel {

        NoProjectLoadedPanel() {
            add(new JLabel("Nothing loaded"));
        }
    }
}
