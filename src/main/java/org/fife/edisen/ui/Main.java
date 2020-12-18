package org.fife.edisen.ui;


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
        context.startApplication(args);
    }
}
