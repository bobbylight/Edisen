package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.EdisenAppThemes;
import org.fife.ui.LabelValueComboBox;
import org.fife.ui.UIUtil;
import org.fife.ui.app.AppTheme;
import org.fife.ui.app.themes.FlatDarkTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

/**
 * Options pertaining to the user interface.
 */
class UIOptionPanel extends AbstractEdisenOptionPanel {

    private LabelValueComboBox<String, AppTheme> themeCombo;

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
        for (AppTheme theme : EdisenAppThemes.get()) {
            themeCombo.addLabelValuePair(theme.getName(), theme);
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
        app.setTheme(themeCombo.getSelectedValue());
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

        int darkThemeIndex = 0;
        for (int i = 0; i < themeCombo.getItemCount(); i++) {
            if (FlatDarkTheme.ID.equals(themeCombo.getValueAt(i).getId())) {
                darkThemeIndex = i;
                break;
            }
        }

        if (darkThemeIndex != themeCombo.getSelectedIndex()) {
            themeCombo.setSelectedIndex(darkThemeIndex);
            return true;
        }

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
