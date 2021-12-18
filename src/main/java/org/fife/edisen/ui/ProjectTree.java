package org.fife.edisen.ui;

import org.fife.edisen.ui.model.EdisenProject;
import org.fife.ui.UIUtil;
import org.fife.ui.rtextfilechooser.FileSystemTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ResourceBundle;

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

            setLayout(new BorderLayout());

            Box top = Box.createVerticalBox();
            add(top, BorderLayout.NORTH);

            JLabel label = new JLabel(edisen.getString("ProjectTree.NothingLoaded"));
            JPanel temp = new JPanel();
            temp.add(label);
            top.add(temp);

            ResourceBundle msg = edisen.getResourceBundle();
            JButton newButton = UIUtil.newButton(msg, "ProjectTree.NewProject");
            JButton openButton = UIUtil.newButton(msg, "ProjectTree.OpenProject");
            openButton.addActionListener((e) -> edisen.openProjectViaFileChooser());
            temp = new JPanel(new SpringLayout());
            temp.add(newButton);
            temp.add(openButton);
            UIUtil.makeSpringCompactGrid(temp, 2, 1, 0, 0, 5, 5);
            top.add(temp);

            top.add(Box.createVerticalGlue());
        }
    }
}
