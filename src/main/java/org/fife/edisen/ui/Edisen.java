package org.fife.edisen.ui;

import org.fife.edisen.model.EdisenProject;
import org.fife.help.HelpDialog;
import org.fife.ui.*;
import org.fife.ui.SplashScreen;
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

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;

public class Edisen extends AbstractPluggableGUIApplication<EdisenPrefs> {

    private EdisenPrefs prefs;
    private File projectDir;
    private TextEditorPane textArea;
    private EdisenOptionsDialog optionsDialog;

    private static final String VERSION = "0.1.0-SNAPSHOT";

    public Edisen() {
        super("Edisen");
        setIcons();
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

    protected JDialog createAboutDialog() {
        return new AboutDialog(this);
    }

    @Override
    protected void createActions(EdisenPrefs prefs) {

        this.prefs = prefs;
        super.createActions(prefs);

        addAction(Actions.OPEN_ACTION_KEY, new Actions.OpenAction(this));
        addAction(EXIT_ACTION_KEY, new GUIApplication.ExitAction<>(this, "Action.Exit"));

        addAction(Actions.OPTIONS_ACTION_KEY, new OptionsAction<>(this, "Action.Options"));

        addAction(Actions.COMPILE_ACTION_KEY, new Actions.CompileAction(this));
        addAction(Actions.EMULATE_ACTION_KEY, new Actions.EmulateAction(this));

        HelpAction<Edisen> helpAction = new HelpAction<>(this, "Action.Help");
        helpAction.setIcon(Util.getSvgIcon("/images/help.svg", 16));
        addAction(HELP_ACTION_KEY, helpAction);

        AboutAction<Edisen> aboutAction = new AboutAction<>(this, "Action.About");
        aboutAction.setIcon(Util.getSvgIcon("/images/about.svg", 16));
        addAction(ABOUT_ACTION_KEY, aboutAction);
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

    public String getAssemblerCommandLine() {
        return prefs.assemblerCommandLine;
    }

    public String getEmulatorCommandLine() {
        return prefs.emulatorCommandLine;
    }

    @Override
    public HelpDialog getHelpDialog() {
        UIUtil.browse("https://github.com/bobbylight/Edisen");
        return null;
    }

    @Override
    public OptionsDialog getOptionsDialog() {
        if (optionsDialog == null) {
            optionsDialog = new EdisenOptionsDialog(this);
        }
        return optionsDialog;
    }

    public EdisenPrefs getPreferences() {
        return prefs;
    }

    @Override
    protected String getPreferencesClassName() {
        return "org.fife.edisen.ui.EdisenPrefs";
    }

    public File getProjectRoot() {
        return projectDir;
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
                Paths.get("D:/dev/edisen/sample-project/sample-project.edisen.json"));
                    //Paths.get("/users/robert/dev/edisen/sample-project/sample-project.edisen.json"));

        Container contentPane = getContentPane();
        DockableWindowPanel mainPanel = (DockableWindowPanel)mainContentPanel;

        JTree tree = new JTree();
        RScrollPane sp = new RScrollPane(tree);

        String title = getString("DockedWindow.project");
        DockableWindow wind = new DockableWindow(title, new BorderLayout());
        wind.setIcon(Util.getSvgIcon("/images/projectStructure.svg", 16));
        wind.setPosition(DockableWindow.LEFT);
        wind.setActive(true);
        wind.add(sp);
        mainPanel.addDockableWindow(wind);
        mainPanel.setDividerLocation(DockableWindowPanel.LEFT, 150);
        mainPanel.setDockableWindowGroupExpanded(DockableWindow.LEFT, true);

        title = getString("DockedWindow.log");
        wind = new DockableWindow(title, new BorderLayout());
        wind.setIcon(Util.getSvgIcon("/images/console.svg", 16));
        wind.setPosition(DockableWindow.BOTTOM);
        wind.setActive(true);
        wind.add(sp);
        mainPanel.addDockableWindow(wind);
        mainPanel.setDividerLocation(DockableWindowPanel.BOTTOM, 240);
        mainPanel.setDockableWindowGroupExpanded(DockableWindow.BOTTOM, true);

        textArea = new TextEditorPane();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
        textArea.setCodeFoldingEnabled(true);
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
            projectDir = file.getParentFile();
        } catch (IOException ioe) {
            displayException(ioe);
        }
    }

    void openProject() {

        File startDir = new File(System.getProperty("user.dir"));
        RTextFileChooser chooser = new RTextFileChooser(false, startDir);

        String desc = getString("FileType.edisenJSON");
        chooser.addChoosableFileFilter(new ExtensionFileFilter(desc, "edisen.json"));

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

    public void setAssemblerCommandLine(String commandLine) {
        prefs.assemblerCommandLine = commandLine;
    }

    public void setEmulatorCommandLine(String commandLine) {
        prefs.emulatorCommandLine = commandLine;
    }

    private void setIcons() {

        try {

            Image image64 = ImageIO.read(getClass().getResource("/icons/Nintendo-NES-icon-64x64.png"));
            setIconImages(Collections.singletonList(
                    image64
            ));
        } catch (IOException ioe) {
            displayException(ioe); // Never happens
        }
    }
}
