package org.fife.edisen.ui.options;

import org.fife.edisen.ui.Edisen;
import org.fife.ui.OptionsDialogPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A base class for options panels.
 */
abstract class AbstractEdisenOptionPanel extends OptionsDialogPanel {

    protected Edisen parent;
    protected JButton defaultsButton;

    private static final String RESTORE_DEFAULTS = "restoreDefaults";

    AbstractEdisenOptionPanel(Edisen parent, String titleKey) {
        this.parent = parent;
        setName(parent.getString(titleKey));
    }

    /**
     * Adds the "Restore Defaults" button to the UI.
     *
     * @param parent The container to add it to.
     */
    protected void addRestoreDefaultsButton(Container parent) {
        defaultsButton = new JButton(getString("RestoreDefaults"));
        defaultsButton.setActionCommand(RESTORE_DEFAULTS);
        defaultsButton.addActionListener(new Listener());
        addLeftAligned(parent, defaultsButton);
    }

    protected String getString(String key, String... params) {
        return parent.getString(key, (Object[])params);
    }

    /**
     * Resets this option panel to its initial state.
     *
     * @return Whether anything changed.  If this is {@code true}, the
     *         options dialog's "Apply" button will be enabled.
     */
    abstract boolean restoreDefaults();

    /**
     * Listens for events in this option panel.
     */
    public class Listener implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();

            if (RESTORE_DEFAULTS.equals(command)) {
                if (restoreDefaults()) {
                    setDirty(true);
                }
            }
        }
    }
}
