package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

@ExtendWith(SwingRunnerExtension.class)
public class OutputTextPaneTest {

    @Test
    public void testCreatePopupMenu() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        Assertions.assertNotNull(textPane.createPopupMenu());
    }

    @Test
    public void testGetDefaultFont() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        Assertions.assertNotNull(textPane.getDefaultFont());
    }

    @Test
    public void testLog_noNewline_noArguments() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test message");

        // getText() returns with OS-specific newlines, ugh
        Assertions.assertEquals("Test message\n", textPane.getText().replace("\r", ""));
        Assertions.assertEquals(textPane.getDocument().getLength(), textPane.getCaretPosition());
    }

    @Test
    public void testLog_withNewline_noArguments() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test message\n");

        // getText() returns with OS-specific newlines, ugh
        Assertions.assertEquals("Test message\n", textPane.getText().replace("\r", ""));
        Assertions.assertEquals(textPane.getDocument().getLength(), textPane.getCaretPosition());
    }

    @Test
    public void testLog_noNewline_withArguments() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test {0}", "message");

        // getText() returns with OS-specific newlines, ugh
        Assertions.assertEquals("Test message\n", textPane.getText().replace("\r", ""));
        Assertions.assertEquals(textPane.getDocument().getLength(), textPane.getCaretPosition());
    }

    @Test
    public void testLog_withNewline_withArguments() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test {0}\n", "message");

        // getText() returns with OS-specific newlines, ugh
        Assertions.assertEquals("Test message\n", textPane.getText().replace("\r", ""));
        Assertions.assertEquals(textPane.getDocument().getLength(), textPane.getCaretPosition());
    }

    @Test
    public void testReplaceSelection_addingToLastLine() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test message");

        textPane.replaceSelection("added");
        // getText() returns with OS-specific newlines, ugh
        Assertions.assertEquals("Test message\nadded", textPane.getText().replace("\r", ""));
        Assertions.assertEquals(textPane.getDocument().getLength(), textPane.getCaretPosition());
    }

    @Test
    public void testReplaceSelection_typingInPriorLine() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test message");
        textPane.setCaretPosition(0);

        textPane.replaceSelection("added");

        // Caret position should be moved to the final line, can't edit prior lines
        // getText() returns with OS-specific newlines, ugh
        Assertions.assertEquals("Test message\nadded", textPane.getText().replace("\r", ""));
        Assertions.assertEquals(textPane.getDocument().getLength(), textPane.getCaretPosition());
    }

    @Test
    public void testReplaceSelection_typingWithSelectionHandInLastLineHalfOut() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test message");
        textPane.replaceSelection("ad");
        textPane.setCaretPosition(0);
        textPane.moveCaretPosition(textPane.getDocument().getLength());

        textPane.replaceSelection("ded");

        // Caret position should be moved to the final line, can't edit prior lines
        // getText() returns with OS-specific newlines, ugh
        Assertions.assertEquals("Test message\nadded", textPane.getText().replace("\r", ""));
        Assertions.assertEquals(textPane.getDocument().getLength(), textPane.getCaretPosition());
    }

    @Test
    public void testMenu_clearAllAction() {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test message");
        textPane.replaceSelection("added");

        // Note: This assumes menu item order
        Action clearAction = ((JMenuItem)textPane.createPopupMenu().getComponent(2)).getAction();
        clearAction.actionPerformed(null);
        Assertions.assertTrue(textPane.getText().isEmpty());
    }

    @Test
    public void testMenu_copyAllAction() throws IOException, UnsupportedFlavorException {

        Edisen mockEdisen = Mockito.mock(Edisen.class);

        OutputTextPane textPane = new OutputTextPane(mockEdisen);
        textPane.log("stdout", "Test message");
        textPane.replaceSelection("added");

        // Note: This assumes menu item order
        Action copyAction = ((JMenuItem)textPane.createPopupMenu().getComponent(0)).getAction();
        copyAction.actionPerformed(null);
        String text = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        Assertions.assertEquals("Test message\nadded", text);
    }
}
