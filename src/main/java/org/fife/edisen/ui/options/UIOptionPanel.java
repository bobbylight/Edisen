package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.Theme;
import org.fife.edisen.ui.ThemeManager;
import org.fife.ui.LabelValueComboBox;
import org.fife.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Options pertaining to the user interface.
 */
class UIOptionPanel extends AbstractEdisenOptionPanel {

    private LabelValueComboBox<String, Theme> themeCombo;

    private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");

    UIOptionPanel(Edisen parent) {
        super(parent, "Options.UI");
        createUI();
    }

    private void createUI() {

        Listener listener = new Listener();
        setBorder(UIUtil.getEmpty5Border());
        setLayout(new BorderLayout());

        JPanel appearancePanel = new JPanel();
        appearancePanel.setBorder(new OptionPanelBorder(MSG.getString("Options.UI.Category.Appearance")));
        SpringLayout layout = new SpringLayout();
        appearancePanel.setLayout(layout);

        JLabel themeLabel = new JLabel(MSG.getString("Options.UI.Theme"));
        themeCombo = new LabelValueComboBox<>();
        for (Theme theme : Theme.values()) {
            themeCombo.addLabelValuePair(MSG.getString(theme.getKey()), theme);
        }
        themeCombo.addActionListener(listener);

        UIUtil.addLabelValuePairs(appearancePanel, getComponentOrientation(),
            themeLabel, themeCombo);

        UIUtil.makeSpringCompactGrid(appearancePanel,
            1, 2,
            0, 0,
            5, 5);

        Box topPanel = Box.createVerticalBox();
        topPanel.add(appearancePanel);
        topPanel.add(Box.createVerticalStrut(10));
        addRestoreDefaultsButton(topPanel);
        topPanel.add(Box.createVerticalGlue());
        add(topPanel, BorderLayout.NORTH);
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
    boolean restoreDefaults() {
        return false;
    }

    @Override
    protected void setValuesImpl(Frame owner) {

        Edisen app = (Edisen)owner;

        themeCombo.setSelectedValue(app.getTheme());
    }

    private class Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            setDirty(true);
        }
    }
}
