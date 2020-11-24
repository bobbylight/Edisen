package org.fife.edisen.ui;

/**
 * An enumeration of themes for the application.
 */
enum Theme {

    LIGHT("Theme.Light", "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml"),
    DARK("Theme.Dark", "/org/fife/ui/rsyntaxtextarea/themes/dark.xml");

    private final String key;
    private final String rstaTheme;

    Theme(String key, String rstaTheme) {
        this.key = key;
        this.rstaTheme = rstaTheme;
    }

    public String getKey() {
        return key;
    }

    public String getRstaTheme() {
        return rstaTheme;
    }
}
