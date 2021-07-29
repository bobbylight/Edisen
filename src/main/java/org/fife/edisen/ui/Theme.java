package org.fife.edisen.ui;

import java.util.Objects;

/**
 * An enumeration of themes for the application.
 */
public enum Theme {

    LIGHT("Theme.Light", "light", "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml"),
    DARK("Theme.Dark", "light", "/org/fife/ui/rsyntaxtextarea/themes/dark.xml"),
    NORD("Theme.Nord", "light", "/org/fife/ui/rsyntaxtextarea/themes/dark.xml");

    private final String key;
    private final String imageRoot;
    private final String rstaTheme;

    Theme(String key, String imageRoot, String rstaTheme) {
        this.key = key;
        this.imageRoot = imageRoot;
        this.rstaTheme = rstaTheme;
    }

    public static Theme fromKey(String key) {
        for (Theme theme : Theme.values()) {
            if (Objects.equals(key, theme.key)) {
                return theme;
            }
        }
        return Theme.DARK;
    }

    public String getImageRoot() {
        return imageRoot;
    }

    public String getKey() {
        return key;
    }

    public String getRstaTheme() {
        return rstaTheme;
    }
}
