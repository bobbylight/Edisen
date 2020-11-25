package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.model.EdisenProject;
import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.Theme;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A tabbed pane containing all open, editable game files.
 */
public class GameFileTabbedPane extends JTabbedPane {

    private final Edisen edisen;
    private final Map<File, Integer> fileToTabIndex;
    private final Listener listener;

    private static final TabbedPaneContent DUMMY_TABBED_PANE_CONTENT = new DummyTabbedPaneContent();

    public GameFileTabbedPane(Edisen edisen) {

        this.edisen = edisen;
        this.fileToTabIndex = new HashMap<>();
        this.listener = new Listener();

        edisen.addPropertyChangeListener(Edisen.PROPERTY_PROJECT, listener);
    }

    /**
     * Ads a tab containing the specified file.  If this file is already open, its tab is focused.
     *
     * @param file The file to open.
     */
    public void addEditorTab(File file) {

        if (focusTabFor(file)) {
            return;
        }

        CodeEditorTabbedPaneContent content = new CodeEditorTabbedPaneContent(edisen, file);

        fileToTabIndex.put(file, getTabCount());
        addTab(file.getName(), content);

        setSelectedIndex(getTabCount() - 1);
        content.requestFocusInWindow();
    }

    private void applyThemeToTextAreas(Theme theme) throws IOException {

        InputStream in = getClass().getResourceAsStream(theme.getRstaTheme());
        org.fife.ui.rsyntaxtextarea.Theme rstaTheme = org.fife.ui.rsyntaxtextarea.Theme.load(in);

        for (int i = 0; i < getTabCount(); i++) {

            TabbedPaneContent content = (TabbedPaneContent)getComponentAt(i);
            if (content instanceof CodeEditorTabbedPaneContent) {
                TextEditorPane textArea = ((CodeEditorTabbedPaneContent)content).getTextArea();
                rstaTheme.apply(textArea);
            }
        }
    }

    public void focusActiveEditor() {
        SwingUtilities.invokeLater(() -> getCurrentContent().requestFocusInWindow());
    }

    private boolean focusTabFor(File file) {

        Integer index = fileToTabIndex.get(file);
        if (index != null) {
            setSelectedIndex(index);
            getCurrentContent().requestFocusInWindow();
            return true;
        }
        return false;
    }

    /**
     * Returns the currently focused content.
     *
     * @return The currently focused content.  This will never be {@code null}.
     */
    private TabbedPaneContent getCurrentContent() {
        TabbedPaneContent content = (TabbedPaneContent)getSelectedComponent();
        return content != null ? content : DUMMY_TABBED_PANE_CONTENT;
    }

    /**
     * Returns the currently focused text area, or {@code null} if some other component type is
     * focused (e.g. a CHR ROM editor).
     *
     * @return The currently focused text area.
     */
    private TextEditorPane getCurrentTextArea() {

        TabbedPaneContent content = getCurrentContent();
        if (content instanceof CodeEditorTabbedPaneContent) {
            return ((CodeEditorTabbedPaneContent)content).getTextArea();
        }
        return null;
    }

    public SearchListener getSearchListener() {
        return listener;
    }

    /**
     * Opens the set of initial tabs that should be open for a project.
     *
     * @param project The new project.
     */
    private void openInitialTabsForProject(EdisenProject project) {

        File projectRoot = project.getProjectFile().getParent().toFile();
        String gameFile = project.getGameFile();

        removeAll();
        addEditorTab(new File(projectRoot, gameFile));
    }

    @Override
    public void removeAll() {
        super.removeAll();
        fileToTabIndex.clear();
    }

    @Override
    public void updateUI() {

        super.updateUI();

        if (edisen != null) {
            Theme theme = edisen.getTheme();
            try {
                applyThemeToTextAreas(theme);
            } catch (IOException ioe) {
                edisen.displayException(ioe);
            }
        }
    }

    private class Listener implements SearchListener, PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            if (Edisen.PROPERTY_PROJECT.equals(e.getPropertyName())) {
                openInitialTabsForProject((EdisenProject)e.getNewValue());
            }
        }

        @Override
        public void searchEvent(SearchEvent e) {

            TextEditorPane currentTextArea = getCurrentTextArea();
            if (currentTextArea == null) {
                UIManager.getLookAndFeel().provideErrorFeedback(GameFileTabbedPane.this);
                return;
            }

            SearchContext context = e.getSearchContext();

            switch (e.getType()) {
                case FIND -> {
                    SearchResult result = SearchEngine.find(currentTextArea, context);
                    if (!result.wasFound() || result.isWrapped()) {
                        UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
                    }
                }
                case REPLACE -> {
                    SearchResult result = SearchEngine.replace(currentTextArea, context);
                    if (!result.wasFound()) {
                        UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
                    }
                }
                case REPLACE_ALL -> {
                    SearchResult result = SearchEngine.replaceAll(currentTextArea, context);
                    if (!result.wasFound()) {
                        UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
                    }
                }
                case MARK_ALL -> {
                    SearchResult result = SearchEngine.markAll(currentTextArea, context);
                    if (!result.wasFound()) {
                        UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
                    }
                }
            }
        }

        @Override
        public String getSelectedText() {
            TextEditorPane currentTextArea = getCurrentTextArea();
            return currentTextArea != null ? currentTextArea.getSelectedText() : null;
        }
    }
}
