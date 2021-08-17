package org.fife.edisen.ui;

import org.fife.edisen.model.EdisenProject;
import org.fife.edisen.ui.options.EdisenOptionsDialog;
import org.fife.edisen.ui.tabbedpane.GameFileTabbedPane;
import org.fife.edisen.ui.tabbedpane.TabbedPaneContent;
import org.fife.help.HelpDialog;
import org.fife.rsta.ui.GoToDialog;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.ui.*;
import org.fife.ui.SplashScreen;
import org.fife.ui.app.AbstractPluggableGUIApplication;
import org.fife.ui.app.GUIApplication;
import org.fife.ui.dockablewindows.DockableWindow;
import org.fife.ui.dockablewindows.DockableWindowConstants;
import org.fife.ui.dockablewindows.DockableWindowPanel;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextfilechooser.FileChooserOwner;
import org.fife.ui.rtextfilechooser.RTextFileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The main window of the application.
 */
public class Edisen extends AbstractPluggableGUIApplication<EdisenPrefs>
        implements FileChooserOwner {

    public static final String PROPERTY_FILE_OPENED = "edisen.fileOpened";
    public static final String PROPERTY_PROJECT = "edisen.project";

    private GameFileTabbedPane tabbedPane;

    private EdisenPrefs prefs;
    private EdisenProject project;
    private EdisenOptionsDialog optionsDialog;

    private RTextFileChooser chooser;
    private RecentFileManager recentFileManager;
    private RecentFileManager recentProjectManager;

    private DockableWindow projectWindow;

    private DockableWindow outputWindow;
    private OutputTextPane outputTextArea;

    private SearchContext searchContext;
    private FindDialog findDialog;
    private ReplaceDialog replaceDialog;
    private GoToDialog goToDialog;

    private Theme theme;

    private static final String VERSION = "1.0.0";

    public Edisen(EdisenAppContext context, EdisenPrefs prefs) {
        super(context, "Edisen", prefs);
        this.prefs = prefs;
        setIcons();
        theme = Theme.fromKey(prefs.theme);
    }

    public boolean closeTab(int index) {

        TabbedPaneContent content = tabbedPane.getContentAt(index);
        if (content.isDirty()) {
            if (!isTabOkToClose(index)) {
                return false;
            }
        }

        tabbedPane.closeTab(index);
        return true;
    }

    @Override
    protected JDialog createAboutDialog() {
        return new AboutDialog(this);
    }

    @Override
    protected void createActions(EdisenPrefs prefs) {

        super.createActions(prefs);

        addAction(Actions.OPEN_ACTION_KEY, new Actions.OpenAction(this));
        Util.setIcon(Actions.OPEN_ACTION_KEY, "open.svg");
        addAction(Actions.SAVE_ACTION_KEY, new Actions.SaveAction(this));
        Util.setIcon(Actions.SAVE_ACTION_KEY, "save.svg");
        addAction(Actions.SAVE_AS_ACTION_KEY, new Actions.SaveAsAction(this));
        addAction(Actions.CLOSE_ACTION_KEY, new Actions.CloseAction(this));
        addAction(EXIT_ACTION_KEY, new GUIApplication.ExitAction<>(this, "Action.Exit"));

        addAction(Actions.FIND_ACTION_KEY, new Actions.FindAction(this));
        addAction(Actions.REPLACE_ACTION_KEY, new Actions.ReplaceAction(this));
        addAction(Actions.GOTO_ACTION_KEY, new Actions.GoToAction(this));
        addAction(Actions.OPTIONS_ACTION_KEY, new OptionsAction<>(this, "Action.Options"));

        addAction(Actions.COMPILE_ACTION_KEY, new Actions.CompileAction(this));
        addAction(Actions.EMULATE_ACTION_KEY, new Actions.EmulateAction(this));

        HelpAction<Edisen> helpAction = new HelpAction<>(this, "Action.Help");
        addAction(HELP_ACTION_KEY, helpAction);
        Util.setIcon(HELP_ACTION_KEY, "help.svg");

        AboutAction<Edisen> aboutAction = new AboutAction<>(this, "Action.About");
        addAction(ABOUT_ACTION_KEY, aboutAction);
        Util.setIcon(ABOUT_ACTION_KEY, "about.svg");
    }

    @Override
    protected JMenuBar createMenuBar(EdisenPrefs prefs) {

        // Ugh - needed to initialize RSTA's actions, which we shove into the menu bar
        new RSyntaxTextArea();

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

    @Override
    public void doExit() {

        // TODO: Prompt to save, ignore or cancel
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            if (tabbedPane.isDirty(i)) {
                tabbedPane.setSelectedIndex(i);
                tabbedPane.focusActiveEditor();
                UIManager.getLookAndFeel().provideErrorFeedback(tabbedPane);
                return;
            }
        }

        savePreferences();
        super.doExit();
    }

    /**
     * Returns the singleton instance of this application.
     *
     * @return The singleton instance of this application.
     */
    public static Edisen get() {
        return (Edisen)JFrame.getFrames()[0];
    }

    public String getAssemblerCommandLine() {
        return project.getAssembleCommandLine();
    }

    public String getEmulatorCommandLine() {
        return project.getEmulatorCommandLine();
    }

    @Override
    public RTextFileChooser getFileChooser() {

        if (chooser == null) {
            File startDir = new File(System.getProperty("user.dir"));
            chooser = new RTextFileChooser(false, startDir);
            chooser.addChoosableFileFilter(new EdisenConfigFileFilter(this));
        }

        return chooser;
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

    public EdisenProject getProject() {
        return project;
    }

    /**
     * Returns the list of most recently opened files, least-recently opened
     * first.
     *
     * @return The list of files.  This may be empty but will never be
     *         <code>null</code>.
     */
    List<FileLocation> getRecentFiles() {
        return recentFileManager.getRecentFiles();
    }

    /**
     * Returns the list of most recently opened projects, least-recently opened
     * first.
     *
     * @return The list of files.  This may be empty but will never be
     *         <code>null</code>.
     */
    List<FileLocation> getRecentProjects() {
        return recentProjectManager.getRecentFiles();
    }

    @Override
    public String getResourceBundleClassName() {
        return "org.fife.edisen.ui.Edisen";
    }

    public int getSelectedTabIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public String getVersionString() {
        return VERSION;
    }

    void goToLine() {

        if (goToDialog == null) {
            goToDialog = new GoToDialog(this);
        }

        TextEditorPane textArea = tabbedPane.getCurrentTextArea();
        if (textArea == null) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
            return;
        }

        goToDialog.setMaxLineNumberAllowed(textArea.getLineCount());
        goToDialog.setVisible(true);

        int line = goToDialog.getLineNumber();
        if (line > 0) {
            try {
                textArea.setCaretPosition(textArea.getLineStartOffset(line - 1));
            } catch (BadLocationException ble) {
                displayException(ble); // Never happens
            }
        }
    }

    /**
     * Initializes the "recent files" manager.
     *
     * @param prefs The preferences for the application.
     */
    private void initRecentFileManager(EdisenPrefs prefs) {

        String[] recentFiles = prefs.recentFiles;
        List<String> files = recentFiles == null ?
            Collections.emptyList() :
            new ArrayList<>(Arrays.asList(recentFiles));

        recentFileManager = new RecentFileManager(this, files);
    }

    /**
     * Initializes the "recent projects" manager.
     *
     * @param prefs The preferences for the application.
     */
    private void initRecentProjectManager(EdisenPrefs prefs) {

        String[] recentProjects = prefs.recentProjects;
        List<String> projectFiles = recentProjects == null ?
            Collections.emptyList() :
            new ArrayList<>(Arrays.asList(recentProjects));

        recentProjectManager = new RecentFileManager(this, projectFiles);
    }

    private void initUI(EdisenPrefs prefs) throws IOException {

        String path;
        if (OS.get() == OS.WINDOWS) {
            path = "D:/dev/Edisen/sample-projects/01-blinking-screen/sample-project.edisen.json";
        }
        else {
            path = "/users/robert/dev/edisen/sample-projects/01-blinking-screen/sample-project.edisen.json";
        }
        EdisenProject project = EdisenProject.fromFile(Paths.get(path));

        Container contentPane = getContentPane();
        DockableWindowPanel mainPanel = (DockableWindowPanel) mainContentPanel;

        ProjectTree tree = new ProjectTree(this, project.getProjectFile().getParent());
        RScrollPane sp = new RScrollPane(tree);

        String title = getString("DockedWindow.Project");
        projectWindow = new DockableWindow(title, new BorderLayout());
        projectWindow.setIcon(Util.getSvgIcon("projectStructure.svg", 16));
        projectWindow.setPosition(DockableWindowConstants.LEFT);
        projectWindow.setActive(true);
        projectWindow.add(sp);
        mainPanel.addDockableWindow(projectWindow);
        mainPanel.setDividerLocation(DockableWindowPanel.LEFT, 150);
        mainPanel.setDockableWindowGroupExpanded(DockableWindowConstants.LEFT, true);

        title = getString("DockedWindow.Output");
        outputWindow = new DockableWindow(title, new BorderLayout());
        outputWindow.setIcon(Util.getSvgIcon("console.svg", 16));
        outputWindow.setPosition(DockableWindowConstants.BOTTOM);
        outputWindow.setActive(true);
        outputTextArea = new OutputTextPane(this);
        outputWindow.add(new RScrollPane(outputTextArea));
        mainPanel.addDockableWindow(outputWindow);
        mainPanel.setDividerLocation(DockableWindowPanel.BOTTOM, 240);
        mainPanel.setDockableWindowGroupExpanded(DockableWindowConstants.BOTTOM, true);

        tabbedPane = new GameFileTabbedPane(this);
        contentPane.add(tabbedPane);

        File file = new File(project.getProjectFile().getParent().toFile(), project.getGameFile());
        tabbedPane.addEditorTab(file);

        openFile(project.getProjectFile().toFile());
    }

    public boolean isTabOkToClose(int index) {

        boolean okToClose = true;

        UIManager.getLookAndFeel().provideErrorFeedback(this);
        TabbedPaneContent content = tabbedPane.getContentAt(index);
        tabbedPane.setSelectedIndex(index);

        int rc = JOptionPane.showConfirmDialog(this,
            getString("Prompt.DirtyFile", content.getFile().getName()),
            "Edisen",
            JOptionPane.YES_NO_CANCEL_OPTION);

        switch (rc) {
            case JOptionPane.YES_OPTION:

                try {
                    content.saveChanges();
                    content.setDirty(false);
                } catch (IOException ioe) {
                    displayException(ioe);
                    okToClose = false;
                }
                break;
            case JOptionPane.NO_OPTION:
                break;
            default: // CANCEL_OPTION
                okToClose = false;
                break;
        }

        return okToClose;
    }

    public void log(String level, String text, Object... arguments) {
        outputTextArea.log(level, text, arguments);
    }

    @Override
    public void openFile(File file) {

        // Don't let the user open a new project if any open files aren't saved
        if (tabbedPane.hasDirtyFiles()) {
            int rc = JOptionPane.showConfirmDialog(this,
                getString("Prompt.DirtyFiles"),
                "Edisen",
                JOptionPane.YES_NO_CANCEL_OPTION);
            switch (rc) {
                case JOptionPane.YES_OPTION:
                    if (!saveAllDirtyFiles()) {
                        return;
                    }
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                default: // CANCEL_OPTION
                    return;
            }
        }

        try {

            EdisenProject project = EdisenProject.fromFile(file.toPath());
            EdisenProject previousProject = this.project;
            this.project = project;
            refreshTitle();
            setAssemblerCommandLine(project.getAssembleCommandLine());
            setEmulatorCommandLine(project.getEmulatorCommandLine());
            firePropertyChange(PROPERTY_PROJECT, previousProject, project);
        } catch (IOException ioe) {
            displayException(ioe);
        }
    }

    void openFileForEditing(File file) {
        tabbedPane.openFile(file);
    }

    void openProject() {

        RTextFileChooser chooser = getFileChooser();
        int rc = chooser.showOpenDialog(this);

        if (rc == RTextFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            openFile(file);
        }
    }

    @Override
    public void preferences() {
        getAction(Actions.OPTIONS_ACTION_KEY).actionPerformed(null);
    }

    @Override
    protected void preDisplayInit(EdisenPrefs prefs, SplashScreen splashScreen) {

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        try {
            initUI(prefs);
        } catch (IOException ioe) {
            displayException(ioe);
        }

        // Really only the RSTA action icons need this...
        refreshIcons();

        tabbedPane.focusActiveEditor();

        // Make the window draggable by the menu bar
        JMenuBar menuBar = getJMenuBar();
        StatusBar statusBar = getStatusBar();
        ComponentMover mover = new ComponentMover(this, menuBar, statusBar);
        mover.setChangeCursor(false);
    }

    @Override
    protected void preMenuBarInit(EdisenPrefs prefs, SplashScreen splashScreen) {
        initRecentFileManager(prefs);
        initRecentProjectManager(prefs);
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
        if (findDialog != null) {
            SwingUtilities.updateComponentTreeUI(findDialog);
        }
        if (replaceDialog != null) {
            SwingUtilities.updateComponentTreeUI(replaceDialog);
        }

        refreshIcons();
    }

    /**
     * Refreshes icons used by the application to better match the current theme.
     * Called after theme changes.
     */
    private void refreshIcons() {

        Util.setIcon(Actions.OPEN_ACTION_KEY, "open.svg");

        refreshTextAreaIcon(RSyntaxTextArea.UNDO_ACTION, "undo.svg");
        refreshTextAreaIcon(RSyntaxTextArea.REDO_ACTION, "redo.svg");
        refreshTextAreaIcon(RSyntaxTextArea.CUT_ACTION, "cut.svg");
        refreshTextAreaIcon(RSyntaxTextArea.COPY_ACTION, "copy.svg");
        refreshTextAreaIcon(RSyntaxTextArea.PASTE_ACTION, "paste.svg");

        Util.setIcon(HELP_ACTION_KEY, "help.svg");
        Util.setIcon(ABOUT_ACTION_KEY, "about.svg");

        projectWindow.setIcon(Util.getSvgIcon("projectStructure.svg", 16));
        outputWindow.setIcon(Util.getSvgIcon("console.svg", 16));
    }

    private void refreshTextAreaIcon(int icon, String resource) {
        Action action = RSyntaxTextArea.getAction(icon);
        action.putValue(Action.SMALL_ICON, Util.getSvgIcon(resource, 16));
    }

    private void refreshTitle() {
        setTitle(getString("MainWindow.Title", project.getName()));
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

    public boolean saveAllDirtyFiles() {

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {

            TabbedPaneContent content = tabbedPane.getContentAt(i);
            if (content.isDirty()) {

                try {
                    content.saveChanges();
                    content.setDirty(false);
                } catch (IOException ioe) {
                    displayException(ioe);
                    return false;
                }
            }
        }

        return true;
    }

    public void saveCurrentFile() {
        tabbedPane.saveCurrentFile();
    }

    public void saveCurrentFileAs() {
        JOptionPane.showMessageDialog(this, "Not yet implemented", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public void saveProject() {

        // TODO: Update *.edisen.json if it's open in a tab

        try {
            project.save();
        } catch (IOException ioe) {
            displayException(ioe);
        }
    }

    public void setAssemblerCommandLine(String commandLine) {
        project.setAssembleCommandLine(commandLine);
    }

    public void setEmulatorCommandLine(String commandLine) {
        project.setEmulatorCommandLine(commandLine);
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
