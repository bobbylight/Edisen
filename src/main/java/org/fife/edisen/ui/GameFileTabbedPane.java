package org.fife.edisen.ui;

import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rsyntaxtextarea.spell.SpellingParser;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A tabbed pane containing all open, editable game files.
 */
class GameFileTabbedPane extends JTabbedPane {

    private final Edisen edisen;
    private final Map<File, Integer> fileToTabIndex;
    private final Listener listener;

    GameFileTabbedPane(Edisen edisen) {
        this.edisen = edisen;
        this.fileToTabIndex = new HashMap<>();
        this.listener = new Listener();
    }

    /**
     * Ads a tab containing the specified file.  If this file is already open, its tab is focused.
     *
     * @param file The file to open.
     */
    void addEditorTab(File file) {

        if (focusTabFor(file)) {
            return;
        }

        TextEditorPane textArea = new TextEditorPane();
        textArea.setSyntaxEditingStyle(FileTypeManager.get().get(file));
        textArea.setCodeFoldingEnabled(true);
        addSpellingParser(textArea);

        try {
            textArea.load(FileLocation.create(file),
                    StandardCharsets.UTF_8.name());
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }

        fileToTabIndex.put(file, getTabCount());
        addTab(file.getName(), new RTextScrollPane(textArea));

        // Do after added to RTextScrollPane so it inherits the theme too
        applyThemeToTextArea(textArea, edisen.getTheme());

        setSelectedIndex(getTabCount() - 1);
        textArea.requestFocusInWindow();
    }

    private void addSpellingParser(TextEditorPane textArea) {

        String[] zipFileLocations = {
                "english_dic.zip", // production
                "src/main/dist/english_dic.zip", // development
                "edisen/src/main/dist/english_dic.zip" // alt development
        };

        try {
            for (String location : zipFileLocations) {
                File file = new File(location);
                if (file.isFile()) {
                    SpellingParser parser = SpellingParser.createEnglishSpellingParser(
                            file, true);
                    textArea.addParser(parser);
                    return;
                }
            }
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }

        System.out.println("Couldn't find dictionary for spell checking");
    }

    private void applyThemeToTextArea(TextEditorPane textArea, Theme theme) {
        try {
            InputStream in = getClass().getResourceAsStream(theme.getRstaTheme());
            org.fife.ui.rsyntaxtextarea.Theme rstaTheme = org.fife.ui.rsyntaxtextarea.Theme.load(in);
            rstaTheme.apply(textArea);
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }
    }

    private void applyThemeToTextAreas(Theme theme) throws IOException {

        InputStream in = getClass().getResourceAsStream(theme.getRstaTheme());
        org.fife.ui.rsyntaxtextarea.Theme rstaTheme = org.fife.ui.rsyntaxtextarea.Theme.load(in);

        for (int i = 0; i < getTabCount(); i++) {

            Component selectedComponent = getTabComponentAt(i);
            if (selectedComponent instanceof RTextScrollPane) {
                TextEditorPane textArea = (TextEditorPane)((RTextScrollPane)selectedComponent).getTextArea();
                rstaTheme.apply(textArea);
            }
        }
    }

    private boolean focusTabFor(File file) {

        Integer index = fileToTabIndex.get(file);
        if (index != null) {
            setSelectedIndex(index);
            getCurrentTextArea().requestFocusInWindow();
            return true;
        }
        return false;
    }

    /**
     * Returns the currently focused text area, or {@code null} if some other component type is
     * focused (e.g. a CHR ROM editor).
     *
     * @return The currently focused text area.
     */
    private TextEditorPane getCurrentTextArea() {

        Component selectedComponent = getSelectedComponent();
        if (selectedComponent instanceof RTextScrollPane) {
            return (TextEditorPane)((RTextScrollPane)selectedComponent).getTextArea();
        }

        return null;
    }

    SearchListener getSearchListener() {
        return listener;
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

    private class Listener implements SearchListener {

        @Override
        public void searchEvent(SearchEvent e) {

            TextEditorPane currentTextArea = getCurrentTextArea();
            if (currentTextArea == null) {
                UIManager.getLookAndFeel().provideErrorFeedback(GameFileTabbedPane.this);
                return;
            }

            SearchContext context = e.getSearchContext();

            switch (e.getType()) {
                case FIND -> SearchEngine.find(currentTextArea, context);
                case REPLACE -> SearchEngine.replace(currentTextArea, context);
                case REPLACE_ALL -> SearchEngine.replaceAll(currentTextArea, context);
                case MARK_ALL -> SearchEngine.markAll(currentTextArea, context);
            }
        }

        @Override
        public String getSelectedText() {
            TextEditorPane currentTextArea = getCurrentTextArea();
            return currentTextArea != null ? currentTextArea.getSelectedText() : null;
        }
    }
}
