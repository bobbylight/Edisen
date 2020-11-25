package org.fife.edisen.ui;

import org.fife.edisen.model.EdisenProject;
import org.fife.ui.rtextfilechooser.FileSystemTree;

import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.file.Path;

/**
 * A tree view that shows all files in the open project.
 */
class ProjectTree extends FileSystemTree {

    private final Edisen edisen;

    ProjectTree(Edisen edisen, Path projectRoot) {

        // Note the initial project root isn't actually used, but that's OK
        super(projectRoot.toFile());
        this.edisen = edisen;
        setRootVisible(false);
        setShowsRootHandles(false);

        TreePath path = getPathForRow(0);
        setExpandedState(path, true);

        Listener listener = new Listener();
        addMouseListener(listener);
        edisen.addPropertyChangeListener(Edisen.PROPERTY_PROJECT, listener);
    }

    private void showProject(EdisenProject project) {
        File projectRoot = project.getProjectFile().getParent().toFile();
        setRoot(projectRoot);
    }

    private class Listener extends MouseAdapter implements PropertyChangeListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                File selection = getSelectedFile();
                if (selection != null) {
                    edisen.openFileForEditing(selection);
                }
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            if (Edisen.PROPERTY_PROJECT.equals(e.getPropertyName())) {
                showProject((EdisenProject)e.getNewValue());
            }
        }
    }
}
