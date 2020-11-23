package org.fife.edisen.ui;

import com.formdev.flatlaf.FlatLightLaf;
import org.fife.jgoodies.looks.common.ShadowPopupFactory;
import org.fife.ui.UIUtil;
import org.fife.ui.WebLookAndFeelUtils;
import org.fife.ui.app.AbstractGUIApplication;
import org.fife.ui.app.ThirdPartyLookAndFeelManager;
import org.fife.util.SubstanceUtil;

import javax.swing.*;

public class Main {

    private static ThirdPartyLookAndFeelManager createLookAndFeelManager(String lafName) {

        String rootDir = AbstractGUIApplication.getLocationOfJar();
        ThirdPartyLookAndFeelManager lafManager =
            new ThirdPartyLookAndFeelManager(rootDir);

        try {

            ClassLoader cl = lafManager.getLAFClassLoader();
            // Set these properties before instantiating WebLookAndFeel
            if (WebLookAndFeelUtils.isWebLookAndFeel(lafName)) {
                WebLookAndFeelUtils.installWebLookAndFeelProperties(cl);
            }
            else {
                ShadowPopupFactory.install();
            }

            // Must set UIManager's ClassLoader before instantiating
            // the LAF.  Substance is so high-maintenance!
            UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
            Class<?> clazz;
            try {
                clazz = cl.loadClass(lafName);
            } catch (UnsupportedClassVersionError ucve) {
                // A LookAndFeel requiring Java X or later, but we're
                // now restarting with a Java version earlier than X
                lafName = UIManager.getSystemLookAndFeelClassName();
                clazz = cl.loadClass(lafName);
            }

            //LookAndFeel laf = (LookAndFeel)clazz.getDeclaredConstructor().newInstance();
            //UIManager.setLookAndFeel(laf);
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
            UIUtil.installOsSpecificLafTweaks();
        } catch (RuntimeException re) { // FindBugs
            throw re;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // The default speed of Substance animations is too slow
        // (200ms), looks bad moving through JMenuItems quickly.
        if (SubstanceUtil.isSubstanceInstalled()) {
            try {
                SubstanceUtil.setAnimationSpeed(100);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (lafName.contains(".Darcula")) {
            UIManager.getLookAndFeelDefaults().put("Tree.rendererFillBackground", Boolean.FALSE);
        }
        else {
            UIManager.getLookAndFeelDefaults().put("Tree.rendererFillBackground", null);
        }

        return lafManager;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            // Must be done before GUI creation
            String lafName = EdisenPrefs.getLookAndFeelToLoad();
            ThirdPartyLookAndFeelManager lafManager = createLookAndFeelManager(lafName);

            Edisen edisen = new Edisen();
            edisen.setLookAndFeelManager(lafManager);
        });
    }
}
