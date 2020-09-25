package org.fife.edisen.ui;

import org.fife.ui.OptionsDialog;

import java.util.Arrays;

/**
 * Options dialog for the application.
 */
public class EdisenOptionsDialog extends OptionsDialog {

    public EdisenOptionsDialog(Edisen app) {
        super(app);
        createUI();
    }

    private void createUI() {

        setOptionsPanels(Arrays.asList(
                new EmulationOptionPanel(),
                new UIOptionPanel()
        ));
    }
}
