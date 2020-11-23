package org.fife.edisen.ui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * A file filter for Edisen config files.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class EdisenConfigFileFilter extends FileFilter {

    private static final String EDISEN_CONFIG_FILE_EXTENSION = ".edisen.json";

    private final Edisen app;

    EdisenConfigFileFilter(Edisen app) {
        this.app = app;
    }

    @Override
    public boolean accept(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(EDISEN_CONFIG_FILE_EXTENSION);
    }

    @Override
    public String getDescription() {
        return app.getString("FileFilter.EdisenConfig");
    }


    /**
     * Overridden to return the description of this file filter, that way we
     * render nicely in combo boxes.
     *
     * @return A string representation of this filter.
     */
    @Override
    public String toString() {
        return getDescription();
    }
}
