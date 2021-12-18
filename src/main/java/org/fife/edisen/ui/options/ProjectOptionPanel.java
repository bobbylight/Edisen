package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.edisen.ui.Util;
import org.fife.rsta.ui.AssistanceIconPanel;
import org.fife.ui.UIUtil;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * General compile/emulation options.
 */
public class ProjectOptionPanel extends AbstractEdisenOptionPanel {

    private JTextField assemblerCommandLineField;
    private JTextField linkCommandLineField;
    private JTextField emuCommandLineField;

    private static final ResourceBundle MSG = ResourceBundle.getBundle("org.fife.edisen.ui.Edisen");

    public ProjectOptionPanel(Edisen parent) {
        super(parent, "Options.ProjectOptions");
        createUI();
    }

    private void createUI() {

        Listener listener = new Listener();

        DefaultCompletionProvider provider = new DefaultCompletionProvider() {
            @Override
            public boolean isValidChar(char ch) {
                return super.isValidChar(ch) || ch == '$' || ch == '{';
            }
        };
        provider.setAutoActivationRules(true, "${");
        provider.addCompletion(new BasicCompletion(provider, "${rom}", "The name of the ROM file to generate, e.g. 'game.nes'"));
        provider.addCompletion(new BasicCompletion(provider, "${gameFile}", "The source file containing the game's entry point"));
        provider.addCompletion(new BasicCompletion(provider, "${objFile}", "The name of the object file to generate"));

        setBorder(UIUtil.getEmpty5Border());
        setLayout(new BorderLayout());

        JPanel generalPanel = new JPanel();
        generalPanel.setBorder(new OptionPanelBorder(MSG.getString("Options.ProjectOptions.Category.General")));
        SpringLayout layout = new SpringLayout();
        generalPanel.setLayout(layout);

        JLabel assemblerCommandLineLabel = new JLabel(MSG.getString("Options.ProjectOptions.AssemblerCommandLine"));
        assemblerCommandLineField = new JTextField();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.setAutoActivationEnabled(true);
        ac.install(assemblerCommandLineField);
        assemblerCommandLineField.getDocument().addDocumentListener(listener);
        JPanel assemblerFieldPanel = createAssistancePanel(assemblerCommandLineField);

        JLabel linkerCommandLineLabel = new JLabel(MSG.getString("Options.ProjectOptions.LinkerCommandLine"));
        linkCommandLineField = new JTextField();
        ac = new AutoCompletion(provider);
        ac.setAutoActivationEnabled(true);
        ac.install(linkCommandLineField);
        linkCommandLineField.getDocument().addDocumentListener(listener);
        JPanel linkerFieldPanel = createAssistancePanel(linkCommandLineField);

        JLabel emuCommandLineLabel = new JLabel(MSG.getString("Options.ProjectOptions.EmuCommandLine"));
        emuCommandLineField = new JTextField();
        ac = new AutoCompletion(provider);
        ac.setAutoActivationEnabled(true);
        ac.install(emuCommandLineField);
        emuCommandLineField.getDocument().addDocumentListener(listener);
        JPanel emuFieldPanel = createAssistancePanel(emuCommandLineField);

        UIUtil.addLabelValuePairs(generalPanel, getComponentOrientation(),
            assemblerCommandLineLabel, assemblerFieldPanel,
            linkerCommandLineLabel, linkerFieldPanel,
            emuCommandLineLabel, emuFieldPanel);

        UIUtil.makeSpringCompactGrid(generalPanel,
            3, 2,
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
        aip.setAssistanceEnabled(getContentAssistImage());
        getApplication().addPropertyChangeListener(Edisen.ICON_STYLE_PROPERTY, e ->
            aip.setAssistanceEnabled(getContentAssistImage())
        );
        aip.setToolTipText(MSG.getString("Options.ProjectOptions.ContentAssistAvailable"));
        JPanel temp = new JPanel(new BorderLayout());
        temp.add(aip, BorderLayout.LINE_START);
        temp.add(comp);
        return temp;
    }

    private Image getContentAssistImage() {
        return getApplication().getIconGroup().getImage("intentionBulb", 12, 12);
    }

    @Override
    protected void doApplyImpl(Frame owner) {

        Edisen app = (Edisen) owner;
        boolean projectOpen = app.getProject() != null;
        if (!projectOpen) {
            return;
        }

        app.setAssemblerCommandLine(assemblerCommandLineField.getText());
        app.setEmulatorCommandLine(emuCommandLineField.getText());
        app.setLinkerCommandLine(linkCommandLineField.getText());
        app.saveProject();
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

        boolean projectOpen = parent.getProject() != null;
        if (!projectOpen) {
            return false;
        }

        String defaultAssemblerCommandLine = Util.getDefaultAssemblerCommandLine();
        String defaultLinkerCommandLine = Util.getDefaultLinkerCommandLine();
        String defaultEmulatorCommandLine = Util.getDefaultEmulatorCommandLine();

        if (!defaultAssemblerCommandLine.equals(assemblerCommandLineField.getText()) ||
                !defaultLinkerCommandLine.equals(linkCommandLineField.getText()) ||
                !defaultEmulatorCommandLine.equals(emuCommandLineField.getText())) {

            assemblerCommandLineField.setText(defaultAssemblerCommandLine);
            linkCommandLineField.setText(defaultLinkerCommandLine);
            emuCommandLineField.setText(defaultEmulatorCommandLine);
            return true;
        }

        return false;
    }

    @Override
    protected void setValuesImpl(Frame owner) {

        Edisen app = (Edisen) owner;
        boolean projectOpen = app.getProject() != null;

        if (projectOpen) {
            assemblerCommandLineField.setText(app.getAssemblerCommandLine());
            emuCommandLineField.setText(app.getEmulatorCommandLine());
            linkCommandLineField.setText(app.getLinkerCommandLine());
        }
        else {
            assemblerCommandLineField.setText(null);
            emuCommandLineField.setText(null);
            linkCommandLineField.setText(null);
        }

        assemblerCommandLineField.setEnabled(projectOpen);
        emuCommandLineField.setEnabled(projectOpen);
        linkCommandLineField.setEnabled(projectOpen);
        defaultsButton.setEnabled(projectOpen);
    }

    class Listener implements DocumentListener {

        private void handleDocumentUpdate() {
            setDirty(true);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleDocumentUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleDocumentUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            handleDocumentUpdate();
        }
    }
}
