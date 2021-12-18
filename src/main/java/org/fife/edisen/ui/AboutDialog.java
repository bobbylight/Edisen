package org.fife.edisen.ui;

import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.HyperlinkEvent;

import org.fife.ui.EscapableDialog;
import org.fife.ui.ResizableFrameContentPane;
import org.fife.ui.SelectableLabel;
import org.fife.ui.UIUtil;
import org.fife.ui.app.AppTheme;
import org.fife.ui.app.themes.FlatDarkTheme;


/**
 * About dialog for the application.
 *
 * @author Robert Futrell
 * @version 1.0
 */
class AboutDialog extends EscapableDialog {

    private Edisen app;
    private Box box;
    private boolean uiCreated;

    AboutDialog(Edisen parent) {
        super(parent);
        if (parent != null) {
            createUI(parent);
        }
    }

    private Container createTitleAndDescPanel(AppTheme theme) {

        boolean lightTheme = !(theme instanceof FlatDarkTheme);
        Color descAreaBackground = lightTheme ? Color.WHITE : new Color(48, 50, 52);

        SelectableLabel descLabel = new SelectableLabel(
                app.getString("Dialog.About.Desc", app.getVersionString()));
        descLabel.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                UIUtil.browse(e.getURL().toString());
            }
        });
        this.box.add(descLabel);

        Box box = Box.createHorizontalBox();

        Icon icon = new ImageIcon(getClass().getResource("/icons/Nintendo-NES-icon-64x64.png"));
        box.add(new JLabel(icon));
        box.add(Box.createHorizontalStrut(15));

        box.add(descLabel);
        box.add(Box.createHorizontalGlue());

        box.setOpaque(true);
        box.setBackground(descAreaBackground);
        box.setBorder(new TopBorder());
        return box;
    }

    protected void createUI(Edisen parent) {

        this.app = parent;
        uiCreated = true;

        JPanel cp = new ResizableFrameContentPane(new BorderLayout());
        cp.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        box = Box.createVerticalBox();

        Container titleAndDescPanel = createTitleAndDescPanel(parent.getTheme());
        box.add(titleAndDescPanel);
        box.add(Box.createVerticalStrut(5));

        SpringLayout sl = new SpringLayout();
        JPanel temp = new JPanel(sl);
        UIUtil.addLabelValuePairs(temp, getComponentOrientation(),
            app.getString("Dialog.About.InstallRoot"), app.getInstallLocation(),
            app.getString("Desc.About.BuildVersion"), app.getVersionString(),
            app.getString("Desc.About.BuildDate"), getBuildDateString());

        UIUtil.makeSpringCompactGrid(temp, 3, 2, 5, 5, 15, 5);
        box.add(temp);

        box.add(Box.createVerticalStrut(10));
        box.add(Box.createVerticalGlue());

        cp.add(box, BorderLayout.NORTH);

        JButton okButton = UIUtil.newButton(app.getResourceBundle(), "Button.OK");
        okButton.addActionListener(e -> escapePressed());
        Container buttons = UIUtil.createButtonFooter(okButton);
        cp.add(buttons, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(okButton);
        setTitle(app.getString("Action.About"));
        setContentPane(cp);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setModal(true);

        // Since JEditorPanes showing HTML have trouble with their preferred
        // size, set preferred size on a random panel inside us to force a
        // minimum width (just to look a little nicer).
        Dimension size = temp.getPreferredSize();
        if (size.width < 420) {
            size.width = 420;
            temp.setPreferredSize(size);
        }
        pack();
        setLocationRelativeTo(app);
    }

    private String getBuildDateString() {
        Date buildDate = app.getBuildDate();
        if (buildDate != null) {
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            return df.format(buildDate);
        }
        return "(unknown)"; // Dev builds won't have a build date
    }

    void refreshForNewAppTheme(AppTheme theme) {

        SwingUtilities.updateComponentTreeUI(this);

        Container titleAndDescPanel = createTitleAndDescPanel(theme);
        box.remove(0);
        box.add(titleAndDescPanel, null, 0);
        pack();
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
        if (!uiCreated) {
            createUI(app);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible && !uiCreated) {
            createUI(app);
            uiCreated = true;
        }
        super.setVisible(visible);
    }

    /**
     * The border of the "top section" of the About dialog.
     */
    private static class TopBorder extends AbstractBorder {

        @Override
        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(0, 0, 0, 0));
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = insets.left = insets.right = 5;
            insets.bottom = 6;
            return insets;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y,
                                int width, int height) {
            Color color = UIManager.getColor("controlShadow");
            if (color == null) {
                color = SystemColor.controlShadow;
            }
            g.setColor(color);
            g.drawLine(x, y + height - 1, x + width, y + height - 1);
        }

    }
}
