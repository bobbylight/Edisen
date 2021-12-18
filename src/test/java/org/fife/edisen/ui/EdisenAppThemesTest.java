package org.fife.edisen.ui;

import org.fife.ui.app.AppTheme;
import org.fife.ui.app.themes.NativeTheme;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(SwingRunnerExtension.class)
public class EdisenAppThemesTest {

    @Test
    void testGet() {
        Assertions.assertEquals(3, EdisenAppThemes.get().size());
    }

    @Test
    void testGetRstaTheme() throws IOException {
        for (AppTheme theme : EdisenAppThemes.get()) {
            Assertions.assertNotNull(EdisenAppThemes.getRstaTheme(theme));
        }
    }
}
