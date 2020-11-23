package org.fife.edisen.ui;

import org.fife.ui.LabelValueComboBox;
import org.fife.ui.OptionsDialogPanel;
import org.fife.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

/**
 * Options pertaining to the user interface.
 */
class UIOptionPanel extends OptionsDialogPanel {

    private LabelValueComboBox<String, Theme> themeCombo;

    private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");

    UIOptionPanel() {
        setName(MSG.getString("Options.UI"));
        createUI();
    }

    private void createUI() {

        Listener listener = new Listener();
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        topPanel.setLayout(layout);
        add(topPanel, BorderLayout.NORTH);

        JLabel themeLabel = new JLabel(MSG.getString("Options.UI.Theme"));
        themeCombo = new LabelValueComboBox<>();
        for (Theme theme : Theme.values()) {
            themeCombo.addLabelValuePair(MSG.getString(theme.getKey()), theme);
        }
        themeCombo.addItemListener(listener);

        if (getComponentOrientation().isLeftToRight()) {
            topPanel.add(themeLabel);
            topPanel.add(themeCombo);
        }
        else {
            topPanel.add(themeCombo);
            topPanel.add(themeLabel);
        }

        UIUtil.makeSpringCompactGrid(topPanel,
            1, 2,
            0, 0,
            5, 5);
    }

    @Override
    protected void doApplyImpl(Frame owner) {

        Edisen app = (Edisen) owner;
        ThemeManager.apply(app, themeCombo.getSelectedValue());
    }

    @Override
    protected OptionsPanelCheckResult ensureValidInputsImpl() {
        return null;
    }

    @Override
    public JComponent getTopJComponent() {
        return themeCombo;
    }

    @Override
    protected void setValuesImpl(Frame owner) {
        themeCombo.setSelectedValue(Theme.LIGHT);
    }

    private class Listener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            setUnsavedChanges(true);
        }
    }
}
