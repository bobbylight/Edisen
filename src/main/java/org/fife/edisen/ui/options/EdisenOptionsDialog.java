package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.ui.OptionsDialog;
import org.fife.ui.app.options.ShortcutOptionPanel;
import org.fife.ui.rtextfilechooser.FileChooserFavoritesOptionPanel;
import org.fife.ui.rtextfilechooser.RTextFileChooserOptionPanel;

import java.util.Arrays;

/**
 * Options dialog for the application.
 */
public class EdisenOptionsDialog extends OptionsDialog {

    private Edisen edisen;
    private boolean uiCreated;

    public EdisenOptionsDialog(Edisen app) {
        super(app);
        if (app != null) {
            this.edisen = app;
            createUI();
        }
    }

    private void createUI() {

        RTextFileChooserOptionPanel chooserOptionPanel = new RTextFileChooserOptionPanel();
        chooserOptionPanel.addChildPanel(new FileChooserFavoritesOptionPanel());

        setOptionsPanels(Arrays.asList(
            new ProjectOptionPanel(edisen),
            chooserOptionPanel,
            new ShortcutOptionPanel(edisen),
            new UIOptionPanel(edisen)
        ));
        uiCreated = true;
    }

    /**
     * This method is only here to facilitate unit testing, ugh.
     * We can't pass the parent app into the constructor because
     * that causes downstream NPE's in private and package-private
     * {@code Window} code.
     *
     * @param app The parent application.
     */
    public void setApplication(Edisen app) {
        this.edisen = app;
        if (!uiCreated) {
            createUI();
        }
    }
}
