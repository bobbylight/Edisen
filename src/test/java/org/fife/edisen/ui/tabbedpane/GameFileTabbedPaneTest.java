package org.fife.edisen.ui.tabbedpane;

import org.fife.edisen.TestUtil;
import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.SwingRunnerExtension;
import org.fife.edisen.ui.TestableEdisen;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.SearchContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SwingRunnerExtension.class)
public class GameFileTabbedPaneTest {

    @Test
    public void testCloseTab() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);
        tabbedPane.openFile(TestUtil.createTempFile());
        tabbedPane.openFile(TestUtil.createTempFile());

        Assertions.assertEquals(2, tabbedPane.getTabCount());
        tabbedPane.closeTab(0);
        Assertions.assertEquals(1, tabbedPane.getTabCount());
    }

    @Test
    public void testFocusActiveEditor() throws IOException {
        Edisen edisen = TestableEdisen.create();
        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);
        tabbedPane.openFile(TestUtil.createTempFile());
        tabbedPane.focusActiveEditor();
    }

    @Test
    public void testGetSearchListener_find_textFound() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world"));
        TextEditorPane textArea = tabbedPane.getCurrentTextArea();
        textArea.setCaretPosition(0);

        // Search for "world"
        SearchContext sc = new SearchContext("world");
        SearchEvent e = new SearchEvent(textArea, SearchEvent.Type.FIND, sc);
        tabbedPane.getSearchListener().searchEvent(e);

        // Verify the text was selected
        Assertions.assertEquals(6, textArea.getSelectionStart());
        Assertions.assertEquals(11, textArea.getSelectionEnd());
        Assertions.assertEquals("world", tabbedPane.getSearchListener().getSelectedText());
    }

    @Test
    public void testGetSearchListener_replace_textFound() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world"));
        TextEditorPane textArea = tabbedPane.getCurrentTextArea();
        textArea.setCaretPosition(0);

        // Search for "world"
        SearchContext sc = new SearchContext("world");
        sc.setReplaceWith("Bob");
        SearchEvent e = new SearchEvent(textArea, SearchEvent.Type.REPLACE, sc);
        tabbedPane.getSearchListener().searchEvent(e);

        // Verify the text was updated
        Assertions.assertEquals(9, textArea.getSelectionStart());
        Assertions.assertEquals(9, textArea.getSelectionEnd());
        Assertions.assertEquals("Hello Bob", textArea.getText().trim());
        Assertions.assertNull(tabbedPane.getSearchListener().getSelectedText());
    }

    @Test
    public void testGetSearchListener_replaceAll_textFound() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world world"));
        TextEditorPane textArea = tabbedPane.getCurrentTextArea();
        textArea.setCaretPosition(0);

        // Search for "world"
        SearchContext sc = new SearchContext("world");
        sc.setReplaceWith("Bob");
        SearchEvent e = new SearchEvent(textArea, SearchEvent.Type.REPLACE_ALL, sc);
        tabbedPane.getSearchListener().searchEvent(e);

        // Verify the text was updated
        Assertions.assertEquals(14, textArea.getSelectionStart());
        Assertions.assertEquals(14, textArea.getSelectionEnd());
        Assertions.assertEquals("Hello Bob Bob", textArea.getText().trim());
        Assertions.assertNull(tabbedPane.getSearchListener().getSelectedText());
    }

    @Test
    public void testGetSearchListener_markAll_textFound() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world world"));
        TextEditorPane textArea = tabbedPane.getCurrentTextArea();
        textArea.setCaretPosition(0);

        // Search for "world"
        SearchContext sc = new SearchContext("world");
        sc.setMarkAll(true);
        SearchEvent e = new SearchEvent(textArea, SearchEvent.Type.MARK_ALL, sc);
        tabbedPane.getSearchListener().searchEvent(e);

        // Verify the text was updated
        Assertions.assertEquals(0, textArea.getSelectionStart());
        Assertions.assertEquals(0, textArea.getSelectionEnd());
        Assertions.assertEquals("Hello world world", textArea.getText().trim());
        Assertions.assertNull(tabbedPane.getSearchListener().getSelectedText());
    }

    @Test
    public void testHasDirtyFiles_noFilesOpen() {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);
        Assertions.assertFalse(tabbedPane.hasDirtyFiles());
    }

    @Test
    public void testHasDirtyFiles_filesOpenButNoneDirty() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".chr"));
        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world"));

        Assertions.assertFalse(tabbedPane.hasDirtyFiles());
    }

    @Test
    public void testHasDirtyFiles_oneDirtyFile() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".chr"));
        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world"));

        Assertions.assertFalse(tabbedPane.hasDirtyFiles());
        tabbedPane.getCurrentTextArea().append("added");
        Assertions.assertTrue(tabbedPane.hasDirtyFiles());
    }

    @Test
    public void testOpenFile_chrDataFile() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);
        Assertions.assertEquals(0, tabbedPane.getTabCount());

        tabbedPane.openFile(TestUtil.createTempFile(".chr"));
        Assertions.assertEquals(1, tabbedPane.getTabCount());
        Assertions.assertNull(tabbedPane.getCurrentTextArea()); // Not a code editor for this file type
    }

    @Test
    public void testOpenFile_sourceFile() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);
        Assertions.assertEquals(0, tabbedPane.getTabCount());

        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world"));
        Assertions.assertEquals(1, tabbedPane.getTabCount());

        Assertions.assertNotNull(tabbedPane.getCurrentTextArea());
        String text = tabbedPane.getCurrentTextArea().getText().trim();
        Assertions.assertEquals("Hello world", text);
    }

    @Test
    public void testRemoveAll() throws IOException {

        Edisen edisen = TestableEdisen.create();

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".chr"));
        tabbedPane.openFile(TestUtil.createTempFile(".s"));

        Assertions.assertEquals(2, tabbedPane.getTabCount());
        tabbedPane.removeAll();
        Assertions.assertEquals(0, tabbedPane.getTabCount());
    }

    @Test
    public void testSaveCurrentFile() throws IOException {

        Edisen edisen = spy(TestableEdisen.create());

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        File file = TestUtil.createTempFile(".s");
        tabbedPane.openFile(file);
        tabbedPane.getCurrentTextArea().append("Modified");

        tabbedPane.saveCurrentFile();
        verify(edisen, never()).displayException(any(java.awt.Frame.class), any(), any());

        String content;
        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            content = r.readLine();
        }
        Assertions.assertEquals("Modified", content);
    }

    @Test
    public void testUpdateUI() throws IOException {

        Edisen edisen = spy(TestableEdisen.create());

        GameFileTabbedPane tabbedPane = new GameFileTabbedPane(edisen);

        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world"));
        tabbedPane.openFile(TestUtil.createTempFile(".s", "Hello world"));

        tabbedPane.updateUI();
        verify(edisen, never()).displayException(any(java.awt.Frame.class), any(), any());
    }
}
