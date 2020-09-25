package org.fife.edisen.ui;

import org.fife.ui.OptionsDialogPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * Options pertaining to the user interface.
 */
class UIOptionPanel extends OptionsDialogPanel {

    private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");

    UIOptionPanel() {
        setName(MSG.getString("Options.UI"));
        createUI();
    }

    private void createUI() {
    }

    @Override
    protected void doApplyImpl(Frame owner) {

    }

    @Override
    protected OptionsPanelCheckResult ensureValidInputsImpl() {
        return null;
    }

    @Override
    public JComponent getTopJComponent() {
        return null;
    }

    @Override
    protected void setValuesImpl(Frame owner) {

    }
}
