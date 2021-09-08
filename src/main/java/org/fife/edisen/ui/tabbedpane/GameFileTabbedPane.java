package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.model.EdisenProject;
import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.Theme;
import org.fife.edisen.ui.Util;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;
import org.fife.ui.rtextfilechooser.Utilities;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.function.BiConsumer;

/**
 * A tabbed pane containing all open, editable game files.
 */
public class GameFileTabbedPane extends JTabbedPane {

    private final Edisen edisen;
    private final Listener listener;

    private static final TabbedPaneContent DUMMY_TABBED_PANE_CONTENT = new DummyTabbedPaneContent();

    public GameFileTabbedPane(Edisen edisen) {

        this.edisen = edisen;
        this.listener = new Listener();

        // FlatLaF-specific stuff
        putClientProperty("JTabbedPane.tabClosable", true);
        UIManager.put("TabbedPane.closeArc", 999);
        UIManager.put("TabbedPane.closeCrossFilledSize", 5.5f);
        putClientProperty("JTabbedPane.tabCloseCallback",
            (BiConsumer<JTabbedPane, Integer>)(tabbedPane, tabIndex) -> {
                edisen.closeTab(tabIndex);
            });

        edisen.addPropertyChangeListener(Edisen.PROPERTY_PROJECT, listener);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    private void addChrDataTab(File file) {

        if (focusTabFor(file)) {
            return;
        }

        ChrRomTabbedPaneContent content = new ChrRomTabbedPaneContent(edisen, file);
        content.addPropertyChangeListener(TabbedPaneContent.PROPERTY_DIRTY, listener);

        addTab(content.getTabName(), getIconFor(file), content);

        setSelectedIndex(getTabCount() - 1);
        content.requestFocusInWindow();
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
        content.addPropertyChangeListener(TabbedPaneContent.PROPERTY_DIRTY, listener);

        addTab(content.getTabName(), getIconFor(file), content);

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

    public void closeTab(int index) {
        getComponentAt(index).removePropertyChangeListener(TabbedPaneContent.PROPERTY_DIRTY, listener);
        removeTabAt(index);
    }

    public void focusActiveEditor() {
        SwingUtilities.invokeLater(() -> getCurrentContent().requestFocusInWindow());
    }

    private boolean focusTabFor(File file) {

        for (int i = 0; i < getTabCount(); i++) {
            TabbedPaneContent content = (TabbedPaneContent)getComponentAt(i);
            if (file.equals(content.getFile())) {
                setSelectedIndex(i);
                getCurrentContent().requestFocusInWindow();
                return true;
            }
        }
        return false;
    }

    public TabbedPaneContent getContentAt(int index) {
        return (TabbedPaneContent)getComponentAt(index);
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
    public TextEditorPane getCurrentTextArea() {

        TabbedPaneContent content = getCurrentContent();
        if (content instanceof CodeEditorTabbedPaneContent) {
            return ((CodeEditorTabbedPaneContent)content).getTextArea();
        }
        return null;
    }

    public SearchListener getSearchListener() {
        return listener;
    }

    private Icon getIconFor(File file) {

        // Default
        String image = "plain.svg";

        String extension = Utilities.getExtension(file.getName());
        if (extension != null) {
            URL url = getClass().getResource(getIconPath(extension + ".svg"));
            if (url != null) {
                image = extension + ".svg";
            }
            else {
                url = getClass().getResource(getIconPath(extension + ".png"));
                if (url != null) {
                    image = extension + ".png";
                }
            }
        }

        if (image.endsWith(".svg")) {
            return Util.getSvgIcon(edisen, getIconPath(image), 16);
        }
        else {
            return new ImageIcon(getClass().getResource(getIconPath(image)));
        }
    }

    private static String getIconPath(String icon) {
        return "/icons/fileTypes/" + icon;
    }

    public boolean hasDirtyFiles() {
        for (int i = 0; i < getTabCount(); i++) {
            if (isDirty(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
    }

    public boolean isDirty(int index) {
        return getContentAt(index).isDirty();
    }

    public void openFile(File file) {

        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".chr")) {
            addChrDataTab(file);
        }
        else if (file.isFile()) { // i.e. not a directory
            addEditorTab(file);
        }
    }

    /**
     * Opens the set of initial tabs that should be open for a project.
     *
     * @param project The new project.
     */
    private void openInitialTabsForProject(EdisenProject project) {

        removeAll();

        if (project != null) { // Verify they aren't just closing a project
            File projectRoot = project.getProjectFile().getParent().toFile();
            String gameFile = project.getGameFile();
            addEditorTab(new File(projectRoot, gameFile));
        }
    }

    /**
     * Overridden to draw a rectangle when no tabs are open.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getTabCount() == 0) {
            g.setColor(UIManager.getColor("Component.borderColor")); // FlatLaf-specific
            g.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
        }
    }

    private void refreshTabName(TabbedPaneContent content) {

        for (int i = 0; i < getTabCount(); i++) {

            if (content == getComponentAt(i)) {

                String tabName = content.getTabName();
                if (content.isDirty()) {
                    tabName += "*";
                }
                setTitleAt(i, tabName);
                break;
            }
        }
    }

    @Override
    public void removeAll() {

        for (int i = 0; i < getTabCount(); i++) {
            getComponentAt(i).removePropertyChangeListener(TabbedPaneContent.PROPERTY_DIRTY, listener);
        }

        super.removeAll();
    }

    public void saveCurrentFile() {
        try {
            getCurrentContent().saveChanges();
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }
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

            switch (e.getPropertyName()) {
                case Edisen.PROPERTY_PROJECT -> openInitialTabsForProject((EdisenProject)e.getNewValue());
                case TabbedPaneContent.PROPERTY_DIRTY -> refreshTabName((TabbedPaneContent)e.getSource());
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
