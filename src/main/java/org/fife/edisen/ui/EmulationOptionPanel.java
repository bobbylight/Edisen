package org.fife.edisen.ui;

import org.fife.rsta.ui.AssistanceIconPanel;
import org.fife.ui.OptionsDialogPanel;
import org.fife.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * General compile/emulation options.
 */
class EmulationOptionPanel extends OptionsDialogPanel {

    private JTextField assemblerCommandLineField;
    private JTextField emuCommandLineField;

    private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");

    EmulationOptionPanel() {

        setName(MSG.getString("Options.EmuOptions"));
        createUI();
    }

    private void createUI() {

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        SpringLayout layout = new SpringLayout();
        topPanel.setLayout(layout);
        add(topPanel, BorderLayout.NORTH);

        JLabel assemblerCommandLineLabel = new JLabel(MSG.getString("Options.EmuOptions.AssemblerCommandLine"));
        assemblerCommandLineField = new JTextField();
        JPanel assemblerFieldPanel = createAssistancePanel(assemblerCommandLineField);

        JLabel emuCommandLineLabel = new JLabel(MSG.getString("Options.EmuOptions.EmuCommandLine"));
        emuCommandLineField = new JTextField();
        JPanel emuFieldPanel = createAssistancePanel(emuCommandLineField);

        if (getComponentOrientation().isLeftToRight()) {
            topPanel.add(assemblerCommandLineLabel);
            topPanel.add(assemblerFieldPanel);
            topPanel.add(emuCommandLineLabel);
            topPanel.add(emuFieldPanel);
        }
        else {
            topPanel.add(assemblerFieldPanel);
            topPanel.add(assemblerCommandLineLabel);
            topPanel.add(emuFieldPanel);
            topPanel.add(emuCommandLineLabel);
        }

        UIUtil.makeSpringCompactGrid(topPanel,
                2, 2,
                0, 0,
                5, 5);
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

        Edisen app = (Edisen)owner;

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
    protected void setValuesImpl(Frame owner) {

        Edisen app = (Edisen)owner;

        assemblerCommandLineField.setText(app.getAssemblerCommandLine());
        emuCommandLineField.setText(app.getEmulatorCommandLine());
    }


}
