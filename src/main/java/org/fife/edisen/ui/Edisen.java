package org.fife.edisen.ui;

import org.fife.edisen.model.EdisenProject;
import org.fife.help.HelpDialog;
import org.fife.ui.CustomizableToolBar;
import org.fife.ui.RScrollPane;
import org.fife.ui.SplashScreen;
import org.fife.ui.StatusBar;
import org.fife.ui.app.AbstractPluggableGUIApplication;
import org.fife.ui.app.GUIApplication;
import org.fife.ui.dockablewindows.DockableWindow;
import org.fife.ui.dockablewindows.DockableWindowPanel;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rsyntaxtextarea.spell.SpellingParser;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextfilechooser.RTextFileChooser;
import org.fife.ui.rtextfilechooser.filters.ExtensionFileFilter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class Edisen extends AbstractPluggableGUIApplication<EdisenPrefs> {

    private TextEditorPane textArea;

    private static final String VERSION = "0.1.0-SNAPSHOT";

    public Edisen() {
        super("Edisen");
    }

    private void addSpellingParser() {

        String[] zipFileLocations = {
                "english_dic.zip", // production
                "src/main/dist/english_dic.zip", // development
                "edisen/src/main/dist/english_dic.zip" // alt development
        };

        try {
            for (String location : zipFileLocations) {
                File file = new File(location);
                if (file.isFile()) {
                    SpellingParser parser = SpellingParser.createEnglishSpellingParser(
                            file, true);
                    textArea.addParser(parser);
                    return;
                }
            }
        } catch (IOException ioe) {
            displayException(ioe);
        }

        System.out.println("Couldn't find dictionary for spell checking");
    }

    @Override
    protected void createActions(EdisenPrefs prefs) {

        super.createActions(prefs);

        addAction(Actions.OPEN_ACTION_KEY, new Actions.OpenAction(this));
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

    private void initUI(EdisenPrefs prefs) throws IOException {

        EdisenProject project = EdisenProject.fromFile(
                    Paths.get("/users/robert/dev/edisen/sample-project/sample-project.json"));

        Container contentPane = getContentPane();
        DockableWindowPanel mainPanel = (DockableWindowPanel)mainContentPanel;

        JTree tree = new JTree();
        RScrollPane sp = new RScrollPane(tree);

        DockableWindow wind = new DockableWindow("foo", new BorderLayout());
        wind.setPosition(DockableWindow.LEFT);
        wind.setActive(true);
        wind.add(sp);
        mainPanel.addDockableWindow(wind);
        mainPanel.setDividerLocation(DockableWindowPanel.LEFT, 150);

        textArea = new TextEditorPane();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
        addSpellingParser();
        RTextScrollPane sp2 = new RTextScrollPane(textArea);
        contentPane.add(sp2);

        openFile(project.getRoot().toFile());
    }

    @Override
    public void openFile(File file) {
        try {
            textArea.load(FileLocation.create(new File(file.getParentFile(), "game.s")),
                    StandardCharsets.UTF_8.name());
        } catch (IOException ioe) {
            displayException(ioe);
        }
    }

    void openProject() {

        File startDir = new File(System.getProperty("user.dir"));
        RTextFileChooser chooser = new RTextFileChooser(false, startDir);

        String desc = getResourceBundle().getString("FileType.JSON");
        chooser.addChoosableFileFilter(new ExtensionFileFilter(desc, "json"));

        int rc = chooser.showOpenDialog(this);

        if (rc == RTextFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            openFile(file);
        }
    }

    @Override
    public void preferences() {

    }

    @Override
    protected void preDisplayInit(EdisenPrefs prefs, SplashScreen splashScreen) {

        try {
            initUI(prefs);
        } catch (IOException ioe) {
            displayException(ioe);
        }
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
