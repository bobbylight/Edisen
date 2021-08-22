package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SwingRunnerExtension.class)
@Disabled("AbstractGUIApplication needs to be more modular to better test this")
public class EdisenTest {

    @Test
    public void testFoo() {

        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();

        Edisen edisen = new Edisen(context, prefs);
        Assertions.assertNotNull(edisen.getTheme());
    }
}
