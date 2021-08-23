package org.fife.edisen.ui;

import javax.swing.*;

/**
 * Program entry point.
 */
public final class Main {

    /**
     * Private constructor to prevent instantiation.
     */
    private Main() {
        // Do nothing - comment for Sonar
    }

    /**
     * Program entry point.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        EdisenAppContext context = new EdisenAppContext();
        SwingUtilities.invokeLater(() -> context.createApplication(args).setVisible(true));
    }
}
