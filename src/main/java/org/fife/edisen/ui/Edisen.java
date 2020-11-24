package org.fife.edisen.ui;

import org.fife.edisen.model.EdisenProject;
import org.fife.help.HelpDialog;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.ui.*;
import org.fife.ui.SplashScreen;
import org.fife.ui.app.AbstractPluggableGUIApplication;
import org.fife.ui.app.GUIApplication;
import org.fife.ui.dockablewindows.DockableWindow;
import org.fife.ui.dockablewindows.DockableWindowPanel;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextfilechooser.FileSystemTree;
import org.fife.ui.rtextfilechooser.RTextFileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;

public class Edisen extends AbstractPluggableGUIApplication<EdisenPrefs> {

    private GameFileTabbedPane tabbedPane;

    private EdisenPrefs prefs;
    private EdisenProject project;
    private EdisenOptionsDialog optionsDialog;

    private SearchContext searchContext;
    private FindDialog findDialog;
    private ReplaceDialog replaceDialog;
    private Theme theme;

    private static final String VERSION = "0.1.0-SNAPSHOT";

    public Edisen() {
        super("Edisen");
        setIcons();
        theme = Theme.DARK;
    }

    @Override
    protected JDialog createAboutDialog() {
        return new AboutDialog(this);
    }

    @Override
    protected void createActions(EdisenPrefs prefs) {

        this.prefs = prefs;
        super.createActions(prefs);

        addAction(Actions.OPEN_ACTION_KEY, new Actions.OpenAction(this));
        addAction(EXIT_ACTION_KEY, new GUIApplication.ExitAction<>(this, "Action.Exit"));

        addAction(Actions.FIND_ACTION_KEY, new Actions.FindAction(this));
        addAction(Actions.REPLACE_ACTION_KEY, new Actions.ReplaceAction(this));
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

    void find() {

        if (searchContext == null) {
            searchContext = new SearchContext();
        }

        if (findDialog == null) {
            findDialog = new FindDialog(this, tabbedPane.getSearchListener());
        }

        findDialog.setVisible(true);
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

    public EdisenProject getProject() {
        return project;
    }

    @Override
    public String getResourceBundleClassName() {
        return "org.fife.edisen.ui.Edisen";
    }

    Theme getTheme() {
        return theme;
    }

    @Override
    public String getVersionString() {
        return VERSION;
    }

    private void initUI(EdisenPrefs prefs) throws IOException {

        String path;
        if (OS.get() == OS.WINDOWS) {
            path = "D:/dev/edisen/sample-projects/01-blinking-screen/sample-project.edisen.json";
        }
        else {
            path = "/users/robert/dev/edisen/sample-projects/01-blinking-screen/sample-project.edisen.json";
        }
        EdisenProject project = EdisenProject.fromFile(Paths.get(path));

        Container contentPane = getContentPane();
        DockableWindowPanel mainPanel = (DockableWindowPanel) mainContentPanel;

        FileSystemTree tree = new FileSystemTree(project.getProjectFile().getParent().toFile());
        tree.setRootVisible(false);
        tree.setShowsRootHandles(false);
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    File selection = tree.getSelectedFile();
                    if (selection != null) {
                        openFileForEditing(selection);
                    }
                }
            }
        });
        RScrollPane sp = new RScrollPane(tree);

        String title = getString("DockedWindow.project");
        DockableWindow wind = new DockableWindow(title, new BorderLayout());
        wind.setIcon(Util.getSvgIcon("/images/projectStructure.svg", 16));
        wind.setPosition(DockableWindow.LEFT);
        wind.setActive(true);
        wind.add(sp);
        wind.add(tree);
        mainPanel.addDockableWindow(wind);
        mainPanel.setDividerLocation(DockableWindowPanel.LEFT, 150);
        mainPanel.setDockableWindowGroupExpanded(DockableWindow.LEFT, true);

        title = getString("DockedWindow.log");
        wind = new DockableWindow(title, new BorderLayout());
        wind.setIcon(Util.getSvgIcon("/images/console.svg", 16));
        wind.setPosition(DockableWindow.BOTTOM);
        wind.setActive(true);
        mainPanel.addDockableWindow(wind);
        mainPanel.setDividerLocation(DockableWindowPanel.BOTTOM, 240);
        mainPanel.setDockableWindowGroupExpanded(DockableWindow.BOTTOM, true);

        tabbedPane = new GameFileTabbedPane(this);
        contentPane.add(tabbedPane);

        File file = new File(project.getProjectFile().getParent().toFile(), project.getGameFile());
        tabbedPane.addEditorTab(file);

        openFile(project.getProjectFile().toFile());
    }

    @Override
    public void openFile(File file) {

        try {

            EdisenProject project = EdisenProject.fromFile(file.toPath());
            File projectRoot = project.getProjectFile().getParent().toFile();
            String gameFile = project.getGameFile();

            tabbedPane.addEditorTab(new File(projectRoot, gameFile));
            this.project = project;
        } catch (IOException ioe) {
            displayException(ioe);
        }
    }

    private void openFileForEditing(File file) {

        String fileName = file.getName().toLowerCase();

        if (fileName.endsWith(".chr")) {
            viewChrData(file);
        }
        else if (file.isFile()) { // i.e. not a directory
            tabbedPane.addEditorTab(file);
        }
    }

    void openProject() {

        File startDir = new File(System.getProperty("user.dir"));
        RTextFileChooser chooser = new RTextFileChooser(false, startDir);

        chooser.addChoosableFileFilter(new EdisenConfigFileFilter(this));

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

    void refreshLookAndFeel(Theme theme) {
        this.theme = theme;
        SwingUtilities.updateComponentTreeUI(this);
        ((AboutDialog)getAboutDialog()).refreshLookAndFeel(theme);
        if (optionsDialog != null) {
            SwingUtilities.updateComponentTreeUI(optionsDialog);
        }
    }

    void replace() {

        if (searchContext == null) {
            searchContext = new SearchContext();
        }

        if (replaceDialog == null) {
            replaceDialog = new ReplaceDialog(this, tabbedPane.getSearchListener());
        }

        replaceDialog.setVisible(true);
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

    private void viewChrData(File file) {

        ChrRomViewer viewer = new ChrRomViewer(this, file);

        String title = getString("Dialog.ChrData.Title");
        EscapableDialog dialog = new EscapableDialog(this, title, true);
        dialog.add(viewer);

        dialog.setVisible(true);
    }
}
