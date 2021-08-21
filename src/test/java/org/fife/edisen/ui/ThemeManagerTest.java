package org.fife.edisen.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SwingRunnerExtension.class)
public class ThemeManagerTest {

    @Test
    public void testApply_dark() {
        testApplyImpl(Theme.DARK);
    }

    @Test
    public void testApply_light() {
        testApplyImpl(Theme.LIGHT);
    }

    @Test
    public void testApply_nord() {
        testApplyImpl(Theme.NORD);
    }

    private void testApplyImpl(Theme theme) {
        Edisen mockEdisen = Mockito.mock(Edisen.class);
        ThemeManager.apply(mockEdisen, theme);
        verify(mockEdisen, times(1)).refreshLookAndFeel(eq(theme));
    }
}
