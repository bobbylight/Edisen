package org.fife.edisen.ui;

/**
 * An enumeration of themes for the application.
 */
enum Theme {

    LIGHT("Theme.Light"),
    DARK("Theme.Dark");

    private final String key;

    Theme(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
