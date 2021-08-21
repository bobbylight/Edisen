package org.fife.edisen.ui;

import org.fife.edisen.model.EdisenProject;
import org.fife.ui.rsyntaxtextarea.FileLocation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Listens for files being opened in the application, so anyone interested can easily
 * get this list.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class RecentFileManager implements PropertyChangeListener {

	private Edisen edisen;
	private List<FileLocation> files;

	/**
	 * The number of files we remember.
	 */
	private static final int MAX_FILE_COUNT = 75;


	/**
	 * Constructor.
	 *
	 * @param app The parent application.
	 * @param recentFiles The initial set of recent files.
	 */
	public RecentFileManager(Edisen app, List<String> recentFiles) {

		this.edisen = app;
		files = new ArrayList<>();

		app.addPropertyChangeListener(Edisen.PROPERTY_PROJECT, this);

		if (recentFiles != null) {
			for (String recentFile : recentFiles) {
				addFile(recentFile);
			}
		}
	}

	/**
	 * Adds a file to the list of recent files.
	 *
	 * @param file The file to add.
	 */
	private void addFile(String file) {

		if (file == null) {
			return;
		}

		// If we already are remembering this file, move it to the "top."
		int index = indexOf(file);
		if (index > -1) {
			FileLocation loc = files.remove(index);
			files.add(0, loc);
			return;
		}

		// Add our new file to the "top" of remembered files.
		FileLocation loc;
		try {
			loc = FileLocation.create(file);
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace(); // Malformed URL, shouldn't happen.
			return;
		}

		if (loc.isLocal() && !loc.isLocalAndExists()) {
			// When adding from saved history, some files may no longer
			// exist
			return;
		}
		files.add(0, loc);

		// Too many files?  Oust the file in history added least recently.
		if (files.size() > MAX_FILE_COUNT) {
			files.remove(files.size() - 1);
		}

	}

    /**
     * Returns the maximum number of files this file manager will remember.
     *
     * @return The maximum number of files.
     */
	public int getMaxFileCount() {
	    return MAX_FILE_COUNT;
    }

	/**
	 * Returns the current index of the specified file in this history.
	 *
	 * @param fileFullPath The file to look for.
	 * @return The index of the file, or <code>-1</code> if it is not
	 *         currently in the list.
	 */
	private int indexOf(String fileFullPath) {

	    File file = new File(fileFullPath);

		for (int i=0; i<files.size(); i++) {
			FileLocation loc = files.get(i);
			if (loc.isLocal() && file.equals(new File(loc.getFileFullPath()))) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the list of recent files.
	 *
	 * @return The list of recent files.
	 */
	public List<FileLocation> getRecentFiles() {
		return files;
	}


    /**
     * Called when a property in the application we are interested in
     * changes.
     *
     * @param e The event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {

        String prop = e.getPropertyName();

        if (Edisen.PROPERTY_PROJECT.equals(prop)) {
            EdisenProject project = (EdisenProject)e.getNewValue();
            addFile(project.getProjectFile().toString());
        }

    }
}
