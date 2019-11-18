package org.fife.edisen.ui;

import org.fife.help.HelpDialog;
import org.fife.ui.CustomizableToolBar;
import org.fife.ui.RScrollPane;
import org.fife.ui.SplashScreen;
import org.fife.ui.StatusBar;
import org.fife.ui.app.AbstractPluggableGUIApplication;
import org.fife.ui.app.GUIApplication;
import org.fife.ui.dockablewindows.DockableWindow;
import org.fife.ui.dockablewindows.DockableWindowPanel;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Edisen extends AbstractPluggableGUIApplication<EdisenPrefs> {

    private static final String VERSION = "0.1.0-SNAPSHOT";

    public Edisen() {
        super("Edisen");
    }

    @Override
    protected void createActions(EdisenPrefs prefs) {

        super.createActions(prefs);

        addAction(EXIT_ACTION_KEY, new GUIApplication.ExitAction<>(this, "Action.Exit"));

        addAction(HELP_ACTION_KEY, new HelpAction<>(this, "Action.Help"));
        addAction(ABOUT_ACTION_KEY, new AboutAction<>(this, "Action.About"));
    }

    @Override
    protected JMenuBar createMenuBar(EdisenPrefs prefs) {
        return new AppMenuBar(this);
    }

    @Override
    protected SplashScreen createSplashScreen() {
        return null;
    }

    @Override
    protected StatusBar createStatusBar(EdisenPrefs prefs) {
        return new StatusBar();
    }

    @Override
    protected CustomizableToolBar createToolBar(EdisenPrefs prefs) {
        return null;
    }

    @Override
    public HelpDialog getHelpDialog() {
        return null;
    }

    @Override
    protected String getPreferencesClassName() {
        return "org.fife.edisen.ui.EdisenPrefs";
    }

    @Override
    public String getResourceBundleClassName() {
        return "org.fife.edisen.ui.Edisen";
    }

    @Override
    public String getVersionString() {
        return VERSION;
    }

    private void initUI(EdisenPrefs prefs) {

        Container contentPane = getContentPane();

        JTree tree = new JTree();
        RScrollPane sp = new RScrollPane(tree);
//        contentPane.add(sp, BorderLayout.LINE_START);

        DockableWindow wind = new DockableWindow("foo", new BorderLayout());
        wind.setPosition(DockableWindow.LEFT);
        wind.setActive(true);
        wind.add(sp);
        ((DockableWindowPanel)mainContentPanel).addDockableWindow(wind);

        TextEditorPane textArea = new TextEditorPane();
        RTextScrollPane sp2 = new RTextScrollPane(textArea);
        contentPane.add(sp2);
    }

    @Override
    public void openFile(File file) {

    }

    @Override
    public void preferences() {

    }

    @Override
    protected void preDisplayInit(EdisenPrefs prefs, SplashScreen splashScreen) {

        initUI(prefs);
    }

    @Override
    protected void preMenuBarInit(EdisenPrefs prefs, SplashScreen splashScreen) {

    }

    @Override
    protected void preStatusBarInit(EdisenPrefs prefs, SplashScreen splashScreen) {

    }

    @Override
    protected void preToolBarInit(EdisenPrefs prefs, SplashScreen splashScreen) {

    }
}
