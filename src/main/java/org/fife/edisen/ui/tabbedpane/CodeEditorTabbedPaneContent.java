package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.ui.Edisen;
import org.fife.ui.UIUtil;
import org.fife.ui.app.AppTheme;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.FileTypeUtil;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.spell.SpellingParser;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * A code editor wrapped as tabbed pane content.
 */
class CodeEditorTabbedPaneContent extends TabbedPaneContent {

    private final Edisen edisen;
    private final TextEditorPane textArea;
    private SpellingParser parser;

    private static final Color SPELLING_ERROR_SQUIGGLE_COLOR_DARK = new Color(224, 224, 255);
    private static final Color SPELLING_ERROR_SQUIGGLE_COLOR_LIGHT = Color.BLUE;

    CodeEditorTabbedPaneContent(Edisen edisen, File file) {

        this.edisen = edisen;
        Listener listener = new Listener();

        textArea = new TextEditorPane();
        textArea.setCodeFoldingEnabled(true);
        textArea.addPropertyChangeListener(TextEditorPane.DIRTY_PROPERTY, listener);
        addSpellingParser(textArea);

        try {
            textArea.load(FileLocation.create(file),
                    StandardCharsets.UTF_8.name());
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }

        setSyntaxEditingStyle(textArea, file);

        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        UIUtil.removeTabbedPaneFocusTraversalKeyBindings(scrollPane);

        setLayout(new BorderLayout());
        add(scrollPane);

        // Do after added to RTextScrollPane so it inherits the theme too
        applyThemeToTextArea(textArea, edisen.getTheme());
    }

    private void addSpellingParser(TextEditorPane textArea) {
        possiblyCreateSpellingParser();
        if (parser != null) {
            textArea.addParser(parser);
        }
    }

    private void applyThemeToTextArea(TextEditorPane textArea, AppTheme theme) {

        String editorTheme = (String)theme.getExtraUiDefaults().get("edisen.editorTheme");

        try {
            InputStream in = getClass().getResourceAsStream(editorTheme);
            Theme rstaTheme = Theme.load(in);
            rstaTheme.apply(textArea);
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }
    }

    private static Color getSpellingErrorSquiggleColor() {
        return UIUtil.isDarkLookAndFeel() ?
                SPELLING_ERROR_SQUIGGLE_COLOR_DARK :
                SPELLING_ERROR_SQUIGGLE_COLOR_LIGHT;
    }

    @Override
    public File getFile() {
        return new File(textArea.getFileFullPath());
    }

    @Override
    String getTabName() {
        return textArea.getFileName();
    }

    TextEditorPane getTextArea() {
        return textArea;
    }

    private void possiblyCreateSpellingParser() {

        if (parser != null) {
            return;
        }

        String[] zipFileLocations = {
                "english_dic.zip", // production
                "src/main/dist/english_dic.zip", // development
                "edisen/src/main/dist/english_dic.zip" // alt development
        };

        try {
            for (String location : zipFileLocations) {
                File file = new File(location);
                if (file.isFile()) {
                    parser = SpellingParser.createEnglishSpellingParser(
                            file, true);
                    parser.setSquiggleUnderlineColor(getSpellingErrorSquiggleColor());
                    return;
                }
            }
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }

        System.out.println("Couldn't find dictionary for spell checking");
    }

    @Override
    public boolean requestFocusInWindow() {
        return textArea.requestFocusInWindow();
    }

    @Override
    public void saveChanges() throws IOException {
        textArea.save();
    }

    private static void setSyntaxEditingStyle(TextEditorPane textArea, File file) {
        String style = FileTypeUtil.get().guessContentType(file);
        if (style == null) {
            style = FileTypeUtil.get().guessContentType(textArea);
        }
        textArea.setSyntaxEditingStyle(style);
    }

    @Override
    public void updateUI() {

        super.updateUI();

        if (parser != null) {
            parser.setSquiggleUnderlineColor(getSpellingErrorSquiggleColor());
        }
    }

    /**
     * Listens for events in this component.
     */
    private class Listener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {

            String property = e.getPropertyName();

            // When the editor's dirty state changes, percolate it up as this tab's
            // dirty state changing
            if (TextEditorPane.DIRTY_PROPERTY.equals(property)) {
                setDirty((Boolean)e.getNewValue());
            }
        }
    }
}
