package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.FileTypeManager;
import org.fife.edisen.ui.Theme;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rsyntaxtextarea.spell.SpellingParser;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.*;
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

    CodeEditorTabbedPaneContent(Edisen edisen, File file) {

        this.edisen = edisen;

        textArea = new TextEditorPane();
        textArea.setSyntaxEditingStyle(FileTypeManager.get().get(file));
        textArea.setCodeFoldingEnabled(true);
        addSpellingParser(textArea);

        try {
            textArea.load(FileLocation.create(file),
                    StandardCharsets.UTF_8.name());
        } catch (IOException ioe) {
            edisen.displayException(ioe);
        }

        RTextScrollPane scrollPane = new RTextScrollPane(textArea);

        setLayout(new BorderLayout());
        add(scrollPane);

        // Do after added to RTextScrollPane so it inherits the theme too
        applyThemeToTextArea(textArea, edisen.getTheme());
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

    TextEditorPane getTextArea() {
        return textArea;
    }

    @Override
    public boolean requestFocusInWindow() {
        return textArea.requestFocusInWindow();
    }
}
