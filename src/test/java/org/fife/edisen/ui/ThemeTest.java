package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ThemeTest {

    @Test
    public void testFromKey_dark() {
        Assertions.assertEquals(Theme.DARK, Theme.fromKey("Theme.Dark"));
    }

    @Test
    public void testFromKey_light() {
        Assertions.assertEquals(Theme.LIGHT, Theme.fromKey("Theme.Light"));
    }

    @Test
    public void testFromKey_nord() {
        Assertions.assertEquals(Theme.NORD, Theme.fromKey("Theme.Nord"));
    }

    @Test
    public void testFromKey_unknown() {
        Assertions.assertEquals(Theme.DARK, Theme.fromKey("unknown"));
    }

    @Test
    public void testGetImageRoot() {
        for (Theme theme : Theme.values()) {
            Assertions.assertNotNull(theme.getImageRoot());
        }
    }

    @Test
    public void testGetKey() {
        for (Theme theme : Theme.values()) {
            Assertions.assertNotNull(theme.getKey());
        }
    }

    @Test
    public void testGetRstaTheme() {
        for (Theme theme : Theme.values()) {
            Assertions.assertNotNull(theme.getRstaTheme());
        }
    }
}
