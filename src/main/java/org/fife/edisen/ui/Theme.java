package org.fife.edisen.ui;

import java.util.Objects;

/**
 * An enumeration of themes for the application.
 */
public enum Theme {

    LIGHT("Theme.Light", "/org/fife/ui/rsyntaxtextarea/themes/default-alt.xml"),
    DARK("Theme.Dark", "/org/fife/ui/rsyntaxtextarea/themes/dark.xml");

    private final String key;
    private final String rstaTheme;

    Theme(String key, String rstaTheme) {
        this.key = key;
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

    public String getKey() {
        return key;
    }

    public String getRstaTheme() {
        return rstaTheme;
    }
}
