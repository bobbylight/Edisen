package org.fife.edisen.ui;

import org.fife.ui.rtextarea.RTextArea;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SwingRunnerExtension.class)
@Disabled("Need to include a way to disable starting of app in AppContext")
public class ThemeManagerTest {

    private Edisen edisen;

    @BeforeEach
    public void setUp() {

        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();
        edisen = new Edisen(context, prefs);

        new RTextArea(); // Yuck - need to initialize this
    }

    @AfterEach
    public void tearDown() {
        if (edisen != null) {
            edisen.dispose();
        }
    }

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
        ThemeManager.apply(edisen, theme);
    }
}
