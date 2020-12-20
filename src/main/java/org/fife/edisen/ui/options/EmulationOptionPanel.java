package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.rsta.ui.AssistanceIconPanel;
import org.fife.ui.OptionsDialogPanel;
import org.fife.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * General compile/emulation options.
 */
public class EmulationOptionPanel extends OptionsDialogPanel {

    private JTextField assemblerCommandLineField;
    private JTextField emuCommandLineField;

    private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");

    public EmulationOptionPanel() {
        setName(MSG.getString("Options.EmuOptions"));
        createUI();
    }

    private void createUI() {

        setBorder(UIUtil.getEmpty5Border());
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBorder(new OptionPanelBorder(MSG.getString("Options.EmuOptions.Category.General")));
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
    protected void setValuesImpl(Frame owner) {

        Edisen app = (Edisen) owner;

        assemblerCommandLineField.setText(app.getAssemblerCommandLine());
        emuCommandLineField.setText(app.getEmulatorCommandLine());
    }


}
