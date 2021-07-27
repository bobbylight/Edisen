package org.fife.edisen.ui;

import java.awt.event.ActionEvent;
import java.io.File;

import org.fife.ui.app.AppAction;

/**
 * Opens a file selected from the "file history" in the application's {@code File} menu.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class OpenFileFromHistoryAction extends AppAction<Edisen> {

	private String fileFullPath;

	/**
	 * Creates a new <code>OpenFileFromHistoryAction</code>.
	 *
	 * @param owner The parent application.
	 * @see #setFileFullPath(String)
	 */
	OpenFileFromHistoryAction(Edisen owner) {
		super(owner);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    getApplication().openFile(new File(fileFullPath));
	}

	/**
	 * Sets the file to open.
	 *
	 * @param fileFullPath The full path of the file to open.
	 */
	public void setFileFullPath(String fileFullPath) {
		this.fileFullPath = fileFullPath;
	}
}
