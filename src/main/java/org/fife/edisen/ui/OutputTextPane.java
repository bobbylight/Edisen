package org.fife.edisen.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.MessageFormat;

import org.fife.ui.app.console.AbstractConsoleTextArea;
import org.fife.ui.OptionsDialog;
import org.fife.ui.StandardAction;
import org.fife.ui.rtextarea.RTextArea;

/**
 * Text component that displays the output of a tool.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class OutputTextPane extends AbstractConsoleTextArea {

	private final Edisen edisen;


	/**
	 * Constructor.
	 */
	OutputTextPane(Edisen edisen) {
		this.edisen = edisen;
		installDefaultStyles(false);
		fixKeyboardShortcuts();
		Listener listener = new Listener();
		addMouseListener(listener);
	}


	@Override
	protected JPopupMenu createPopupMenu() {
		JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem(new CopyAllAction()));
		popup.addSeparator();
		popup.add(new JMenuItem(new ClearAllAction()));
		popup.addSeparator();
		popup.add(new JMenuItem(new ConfigureAction()));
		return popup;
	}


	/**
	 * Fixes the keyboard shortcuts for this text component so the user cannot
	 * accidentally delete any stdout or stderr, only stdin.
	 */
	private void fixKeyboardShortcuts() {

	    setEditable(false);
//		InputMap im = getInputMap();
//
//		// backspace
//		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "invalid");
//
//		// Just remove "delete previous word" for now, since DefaultEditorKit
//		// doesn't expose the delegate for us to call into.
//		int ctrl = InputEvent.CTRL_DOWN_MASK;
//		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, ctrl), "invalid");
//
//		// delete
//		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "invalid");
//
//		// Just remove "delete next word" for now, since DefaultEditorKit
//		// doesn't expose the delegate for us to call into.
//		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ctrl), "invalid");

	}

    /**
     * Overridden to return a monospaced font closer to the OS default.
     *
     * @return The font to use in this window.
     */
	@Override
    protected Font getDefaultFont() {
	    return RTextArea.getDefaultFont();
    }


	/**
	 * Returns whether the specified offset is on the last line of this text
	 * component.
	 *
	 * @param offs The offset.
	 * @return Whether the offset is on the last line.
	 */
	private boolean isOnLastLine(int offs) {
		Document doc = getDocument();
		Element root = doc.getDefaultRootElement();
		int lastLine = root.getElementCount() - 1;
		return root.getElementIndex(offs)==lastLine;
	}


	public void log(String level, String text, Object... arguments) {

	    Style style = getStyle(level);

	    int offs = getDocument().getLength();
	    if (!text.endsWith("\n")) {
	        text += '\n';
        }
	    text = MessageFormat.format(text, arguments);

	    try {
            getDocument().insertString(offs, text, style);
            setCaretPosition(getDocument().getLength());
        } catch (BadLocationException ble) { // Never happens
	        ble.printStackTrace();
        }
    }

	/**
	 * Overridden to only allow the user to edit text they have entered (i.e.
	 * they can only edit "stdin").
	 *
	 * @param text The text to replace the selection with.
	 */
	@Override
	public void replaceSelection(String text) {

		int start = getSelectionStart();
		int end = getSelectionEnd();
		Document doc = getDocument();

		// Don't let the user remove any text they haven't typed (stdin).
		if (!(isOnLastLine(start) && isOnLastLine(end))) {
			setCaretPosition(doc.getLength());
		}

		// JUST IN CASE we aren't an AbstractDocument (paranoid), use remove()
		// and insertString() separately.
		try {
			start = getSelectionStart();
			doc.remove(start, getSelectionEnd()-start);
			doc.insertString(start, text, getStyle(STYLE_STDIN));
		} catch (BadLocationException ble) {
			UIManager.getLookAndFeel().provideErrorFeedback(this);
			ble.printStackTrace();
		}

	}


	/**
	 * Clears all text from this text area.
	 */
	private class ClearAllAction extends StandardAction {

		ClearAllAction() {
			setName(edisen.getString("Window.Output.ClearAll"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			setText(null);
		}
	}


	/**
	 * Brings up the options dialog panel for this plugin.
	 */
	private class ConfigureAction extends AbstractAction {

		ConfigureAction() {
			putValue(NAME, edisen.getString("Window.Output.Configure"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			OptionsDialog od = edisen.getOptionsDialog();
			od.initialize();
			od.setSelectedOptionsPanel(edisen.getString("Plugin.Name"));
			od.setVisible(true);
		}

	}


	/**
	 * Copies all text from this text area.
	 */
	private class CopyAllAction extends StandardAction {

		CopyAllAction() {
			setName(edisen.getString("Window.Output.CopyAll"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			int dot = getSelectionStart();
			int mark = getSelectionEnd();
			setSelectionStart(0);
			setSelectionEnd(getDocument().getLength());
			copy();
			setSelectionStart(dot);
			setSelectionEnd(mark);
		}
	}

	/**
	 * Listens for events in this text area.
	 */
	private class Listener extends MouseAdapter {

		private void handleMouseEvent(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopupMenu(e);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			handleMouseEvent(e);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			handleMouseEvent(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			handleMouseEvent(e);
		}

	}
}
