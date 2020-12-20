package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.ui.OptionsDialog;
import org.fife.ui.rtextfilechooser.FileChooserFavoritesOptionPanel;
import org.fife.ui.rtextfilechooser.RTextFileChooserOptionPanel;

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

        RTextFileChooserOptionPanel chooserOptionPanel = new RTextFileChooserOptionPanel();
        chooserOptionPanel.addChildPanel(new FileChooserFavoritesOptionPanel());

        setOptionsPanels(Arrays.asList(
            new EmulationOptionPanel(),
            chooserOptionPanel,
            new UIOptionPanel()
        ));
    }
}
