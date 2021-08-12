package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.rsta.ui.AssistanceIconPanel;
import org.fife.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * General compile/emulation options.
 */
public class EmulationOptionPanel extends AbstractEdisenOptionPanel {

    private JTextField assemblerCommandLineField;
    private JTextField emuCommandLineField;

    private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");

    public EmulationOptionPanel(Edisen parent) {
        super(parent, "Options.EmuOptions");
        createUI();
    }

    private void createUI() {

        setBorder(UIUtil.getEmpty5Border());
        setLayout(new BorderLayout());

        JPanel generalPanel = new JPanel();
        generalPanel.setBorder(new OptionPanelBorder(MSG.getString("Options.EmuOptions.Category.General")));
        SpringLayout layout = new SpringLayout();
        generalPanel.setLayout(layout);

        JLabel assemblerCommandLineLabel = new JLabel(MSG.getString("Options.EmuOptions.AssemblerCommandLine"));
        assemblerCommandLineField = new JTextField();
        JPanel assemblerFieldPanel = createAssistancePanel(assemblerCommandLineField);

        JLabel emuCommandLineLabel = new JLabel(MSG.getString("Options.EmuOptions.EmuCommandLine"));
        emuCommandLineField = new JTextField();
        JPanel emuFieldPanel = createAssistancePanel(emuCommandLineField);

        addLabelValuePairs(generalPanel, getComponentOrientation(),
            assemblerCommandLineLabel, assemblerFieldPanel,
            emuCommandLineLabel, emuFieldPanel);

        UIUtil.makeSpringCompactGrid(generalPanel,
            2, 2,
            0, 0,
            5, 5);

        Box topPanel = Box.createVerticalBox();
        topPanel.add(generalPanel);
        topPanel.add(Box.createVerticalStrut(10));
        addRestoreDefaultsButton(topPanel);
        topPanel.add(Box.createVerticalGlue());
        add(topPanel, BorderLayout.NORTH);
    }

    private JPanel createAssistancePanel(JComponent comp) {
        AssistanceIconPanel aip = new AssistanceIconPanel(comp);
        JPanel temp = new JPanel(new BorderLayout());
        temp.add(aip, BorderLayout.LINE_START);
        temp.add(comp);
        return temp;
    }

    @Override
    protected void doApplyImpl(Frame owner) {

        Edisen app = (Edisen) owner;

        app.setAssemblerCommandLine(assemblerCommandLineField.getText());
        app.setEmulatorCommandLine(emuCommandLineField.getText());
    }

    @Override
    protected OptionsPanelCheckResult ensureValidInputsImpl() {
        return null;
    }

    @Override
    public JComponent getTopJComponent() {
        return assemblerCommandLineField;
    }

    @Override
    boolean restoreDefaults() {
        return false;
    }

    @Override
    protected void setValuesImpl(Frame owner) {

        Edisen app = (Edisen) owner;

        assemblerCommandLineField.setText(app.getAssemblerCommandLine());
        emuCommandLineField.setText(app.getEmulatorCommandLine());
    }


}
