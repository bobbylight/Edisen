package org.fife.edisen.ui;

import org.fife.edisen.ui.model.EdisenProject;
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
import org.fife.ui.app.AppTheme;
import org.fife.ui.app.GUIApplication;
import org.fife.ui.app.icons.IconGroup;
import org.fife.ui.app.icons.RasterImageIconGroup;
import org.fife.ui.app.icons.SvgIconGroup;
import org.fife.ui.dockablewindows.DockableWindow;
import org.fife.ui.dockablewindows.DockableWindowConstants;
import org.fife.ui.dockablewindows.DockableWindowPanel;
import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TextEditorPane;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextfilechooser.FileChooserOwner;
import org.fife.ui.rtextfilechooser.RTextFileChooser;
import org.fife.ui.rtextfilechooser.filters.ExtensionFileFilter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
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

    private final FileFilter projectFileFilter;
    private final FileFilter allSupportedFilesFileFilter;

    private Map<String, IconGroup> iconGroupMap;

    private static final String DEFAULT_ICON_GROUP_NAME = "IntelliJ Icons (Dark)";

    private static final String VERSION = "1.0.0";

    public Edisen(EdisenAppContext context, EdisenPrefs prefs) {
        super(context, "Edisen", prefs);
        setTitle(getString("MainWindow.Title.NoProjectOpen"));

        projectFileFilter = new EdisenConfigFileFilter(this);
        allSupportedFilesFileFilter = new ExtensionFileFilter(
            getString("FileFilter.AllSupportedFiles"), ExtensionFileFilter.CaseCheck.SYSTEM_CASE_CHECK, false,
                "chr", "inc", "json", "s");
    }

    public boolean closeTab(int index) {

        if (index < 0 || index >= tabbedPane.getTabCount()) {
            UIManager.getLookAndFeel().provideErrorFeedback(tabbedPane);
            return false;
        }

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

        addAction(Actions.OPEN_PROJECT_ACTION_KEY, new Actions.OpenProjectAction(this));
        addAction(Actions.CLOSE_PROJECT_ACTION_KEY, new Actions.CloseProjectAction(this));

        addAction(Actions.OPEN_ACTION_KEY, new Actions.OpenAction(this));
        addAction(Actions.SAVE_ACTION_KEY, new Actions.SaveAction(this));
        addAction(Actions.SAVE_AS_ACTION_KEY, new Actions.SaveAsAction(this));
        addAction(Actions.CLOSE_ACTION_KEY, new Actions.CloseAction(this));

        addAction(EXIT_ACTION_KEY, new GUIApplication.ExitAction<>(this, "Action.Exit"));

        addAction(Actions.FIND_ACTION_KEY, new Actions.FindAction(this));
        addAction(Actions.REPLACE_ACTION_KEY, new Actions.ReplaceAction(this));
        addAction(Actions.GOTO_ACTION_KEY, new Actions.GoToAction(this));
        addAction(Actions.OPTIONS_ACTION_KEY, new OptionsAction<>(this, "Action.Options"));

        addAction(Actions.COMPILE_ACTION_KEY, new Actions.BuildAction(this));
        addAction(Actions.EMULATE_ACTION_KEY, new Actions.EmulateAction(this));

        addAction(HELP_ACTION_KEY, new HelpAction<>(this, "Action.Help"));

        addAction(ABOUT_ACTION_KEY, new AboutAction<>(this, "Action.About"));
    }

    protected FindDialog createFindDialog() {
        return new FindDialog(this, tabbedPane.getSearchListener());
    }

    protected GoToDialog createGoToDialog() {
        return new GoToDialog(this);
    }

    @Override
    protected JMenuBar createMenuBar(EdisenPrefs prefs) {
        return new AppMenuBar(this);
    }

    protected ReplaceDialog createReplaceDialog() {
        return new ReplaceDialog(this, tabbedPane.getSearchListener());
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
        return new EdisenToolBar(this);
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

    void find() {

        if (searchContext == null) {
            searchContext = new SearchContext();
        }

        if (findDialog == null) {
            findDialog = createFindDialog();
        }

        findDialog.setVisible(true);
    }

    public String getAssemblerCommandLine() {
        return project.getAssemblerCommandLine();
    }

    public String getEmulatorCommandLine() {
        return project.getEmulatorCommandLine();
    }

    @Override
    public RTextFileChooser getFileChooser() {

        if (chooser == null) {
            File startDir = new File(System.getProperty("user.dir"));
            chooser = new RTextFileChooser(false, startDir);
            chooser.addChoosableFileFilter(projectFileFilter);
            chooser.addChoosableFileFilter(allSupportedFilesFileFilter);
        }

        return chooser;
    }

    @Override
    public HelpDialog getHelpDialog() {
        UIUtil.browse("https://github.com/bobbylight/Edisen");
        return null;
    }

    public String getLinkerCommandLine() {
        return project.getLinkCommandLine();
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

    @Override
    public String getVersionString() {
        return VERSION;
    }

    void goToLine() {

        if (goToDialog == null) {
            goToDialog = createGoToDialog();
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

        Container contentPane = getContentPane();
        DockableWindowPanel mainPanel = (DockableWindowPanel) mainContentPanel;

        ProjectTree tree = new ProjectTree(this);
        RScrollPane sp = new RScrollPane(tree);

        String title = getString("DockedWindow.Project");
        projectWindow = new DockableWindow(title, new BorderLayout());
        projectWindow.setIcon(getIconGroup().getIcon("projectStructure"));
        projectWindow.setPosition(DockableWindowConstants.LEFT);
        projectWindow.setActive(true);
        projectWindow.add(sp);
        mainPanel.addDockableWindow(projectWindow);
        mainPanel.setDividerLocation(DockableWindowPanel.LEFT, 150);
        mainPanel.setDockableWindowGroupExpanded(DockableWindowConstants.LEFT, true);

        title = getString("DockedWindow.Output");
        outputWindow = new DockableWindow(title, new BorderLayout());
        outputWindow.setIcon(getIconGroup().getIcon("console"));
        outputWindow.setPosition(DockableWindowConstants.BOTTOM);
        outputWindow.setActive(true);
        outputTextArea = new OutputTextPane(this);
        outputWindow.add(new RScrollPane(outputTextArea));
        mainPanel.addDockableWindow(outputWindow);
        mainPanel.setDividerLocation(DockableWindowPanel.BOTTOM, 240);
        mainPanel.setDockableWindowGroupExpanded(DockableWindowConstants.BOTTOM, true);

        tabbedPane = new GameFileTabbedPane(this);
        contentPane.add(tabbedPane);
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


    /**
     * Loads and validates the icon groups available to RText.
     */
    private void loadPossibleIconGroups() {

        iconGroupMap = new HashMap<>();
        String resourceRoot = "images/";

        iconGroupMap.put(DEFAULT_ICON_GROUP_NAME,
            new SvgIconGroup(this, DEFAULT_ICON_GROUP_NAME,
                resourceRoot + "intellij-icons-dark",
                resourceRoot + "intellij-icons-light")); // Proper contrast
        iconGroupMap.put("IntelliJ Icons (Light)",
            new SvgIconGroup(this, "IntelliJ Icons (Light)",
                resourceRoot + "intellij-icons-light",
                null));
        iconGroupMap.put("Eclipse Icons", new RasterImageIconGroup("Eclipse Icons",
            resourceRoot + "eclipse-icons",
            null));

    }

    public void log(String level, String text, Object... arguments) {
        outputTextArea.log(level, text, arguments);
    }

    /**
     * Opens a project file.  This should be called {@code openProject()} but
     * can't be because of the API contract.
     *
     * @param file The project file to open.
     */
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

        openProjectImpl(file);
    }

    void openFileForEditing(File file) {
        tabbedPane.openFile(file);
    }

    void openFileViaFileChooser() {

        RTextFileChooser chooser = getFileChooser();
        chooser.setFileFilter(allSupportedFilesFileFilter);
        int rc = chooser.showOpenDialog(this);

        if (rc == RTextFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            openFileForEditing(file);
        }
    }

    void openProjectViaFileChooser() {

        RTextFileChooser chooser = getFileChooser();
        chooser.setFileFilter(projectFileFilter);
        int rc = chooser.showOpenDialog(this);

        if (rc == RTextFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            openFile(file);
        }
    }

    private void openProjectImpl(File file) {

        EdisenProject previousProject = this.project;

        if (file != null) {
            try {

                EdisenProject project = EdisenProject.fromFile(file.toPath());
                this.project = project;
                setAssemblerCommandLine(project.getAssemblerCommandLine());
                setEmulatorCommandLine(project.getEmulatorCommandLine());
                setLinkerCommandLine(project.getLinkCommandLine());
            } catch (IOException ioe) {
                displayException(ioe);
            }
        }
        else {
            this.project = null;
        }

        refreshTitle();
        refreshProjectRelatedActions();
        firePropertyChange(PROPERTY_PROJECT, previousProject, project);
    }

    @Override
    public void preferences() {
        getAction(Actions.OPTIONS_ACTION_KEY).actionPerformed(null);
    }

    @Override
    protected void preCreateActions(EdisenPrefs prefs, SplashScreen splashScreen) {
        this.prefs = prefs;
        setIcons();
    }

    @Override
    protected void preDisplayInit(EdisenPrefs prefs, SplashScreen splashScreen) {

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        try {
            initUI(prefs);
        } catch (IOException ioe) {
            displayException(ioe);
        }

        refreshProjectRelatedActions();

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
        loadPossibleIconGroups();
        setIconGroupByName((String)getTheme().getExtraUiDefaults().get("edisen.iconGroupName"));
    }

    @Override
    protected void preToolBarInit(EdisenPrefs prefs, SplashScreen splashScreen) {

    }

    private void refreshProjectRelatedActions() {
        boolean activeProject = project != null;
        getAction(Actions.COMPILE_ACTION_KEY).setEnabled(activeProject);
        getAction(Actions.EMULATE_ACTION_KEY).setEnabled(activeProject);
    }

    private void refreshTitle() {
        String title = project != null ?
            getString("MainWindow.Title", project.getName()) :
            getString("MainWindow.Title.NoProjectOpen");
        setTitle(title);
    }

    void replace() {

        if (searchContext == null) {
            searchContext = new SearchContext();
        }

        if (replaceDialog == null) {
            replaceDialog = createReplaceDialog();
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
        project.setAssemblerCommandLine(commandLine);
    }

    public void setEmulatorCommandLine(String commandLine) {
        project.setEmulatorCommandLine(commandLine);
    }

    /**
     * Changes the style of icons used by the application.<p>
     *
     * This method fires a property change of type
     * <code>ICON_STYLE_PROPERTY</code>.
     *
     * @param name The name of the icon group to use.  If this name is not
     *        recognized, a default icon set will be used.
     */
    private void setIconGroupByName(String name) {

        IconGroup newGroup = iconGroupMap.get(name);
        if (newGroup==null) {
            newGroup = iconGroupMap.get(DEFAULT_ICON_GROUP_NAME);
        }
        IconGroup iconGroup = getIconGroup();
        if (iconGroup!=null && iconGroup.equals(newGroup)) {
            return;
        }

        setIconGroup(newGroup);
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

    public void setLinkerCommandLine(String commandLine) {
        project.setLinkCommandLine(commandLine);
    }

    @Override
    protected void setThemeAdditionalProperties(AppTheme theme) {

        super.setThemeAdditionalProperties(theme);

        if (iconGroupMap != null) {
            setIconGroupByName((String)theme.getExtraUiDefaults().get("edisen.iconGroupName"));
        }

        if (tabbedPane != null) {
            try {
                tabbedPane.updateRstaTheme(EdisenAppThemes.getRstaTheme(theme));
            } catch (IOException ioe) {
                displayException(ioe);
            }
        }
    }

    private void updateIconImpl(IconGroup iconGroup, String action, String icon) {
        getAction(action).putValue(Action.SMALL_ICON, iconGroup.getIcon(icon));
    }

    /**
     * Refreshes icons used by the application to better match the current theme.
     * Called after theme changes.
     */
    @Override
    protected void updateIconsForNewIconGroup(IconGroup iconGroup) {

        super.updateIconsForNewIconGroup(iconGroup);

        // Ugh - needed to initialize RSTA's actions, which we shove into the menu bar
        new RSyntaxTextArea();

        Dimension size = getSize();

        // Text area icons
        updateTextAreaIcon(RTextArea.CUT_ACTION, "cut");
        updateTextAreaIcon(RTextArea.COPY_ACTION, "copy");
        updateTextAreaIcon(RTextArea.PASTE_ACTION, "paste");
        updateTextAreaIcon(RTextArea.DELETE_ACTION, "delete");
        updateTextAreaIcon(RTextArea.UNDO_ACTION, "undo");
        updateTextAreaIcon(RTextArea.REDO_ACTION, "redo");
        updateTextAreaIcon(RTextArea.SELECT_ALL_ACTION, "selectall");

        // All other icons
        updateIconImpl(iconGroup, Actions.OPEN_PROJECT_ACTION_KEY, "projectDirectory");
        updateIconImpl(iconGroup, Actions.OPEN_ACTION_KEY, "open");
        updateIconImpl(iconGroup, Actions.SAVE_ACTION_KEY, "save");
        updateIconImpl(iconGroup, Actions.SAVE_AS_ACTION_KEY, "saveas");
        updateIconImpl(iconGroup, Actions.FIND_ACTION_KEY, "find");
        updateIconImpl(iconGroup, Actions.REPLACE_ACTION_KEY, "replace");
        updateIconImpl(iconGroup, Actions.COMPILE_ACTION_KEY, "compile");
        updateIconImpl(iconGroup, Actions.EMULATE_ACTION_KEY, "run_anything");
        updateIconImpl(iconGroup, HELP_ACTION_KEY, "help");
        updateIconImpl(iconGroup, ABOUT_ACTION_KEY, "about");

        // Do this because the toolbar has changed it's size.
        if (isDisplayable()) {
            pack();
            setSize(size);
        }

        if (projectWindow != null) {
            projectWindow.setIcon(iconGroup.getIcon("projectStructure"));
        }
        if (outputWindow != null) {
            outputWindow.setIcon(iconGroup.getIcon("console"));
        }
    }

    @Override
    public void updateLookAndFeel(LookAndFeel lnf) {

        super.updateLookAndFeel(lnf);

        // Our About dialog needs a little extra nudge
        ((AboutDialog)getAboutDialog()).refreshForNewAppTheme(getTheme());

        if (optionsDialog != null) {
            SwingUtilities.updateComponentTreeUI(optionsDialog);
        }
        if (findDialog != null) {
            SwingUtilities.updateComponentTreeUI(findDialog);
        }
        if (replaceDialog != null) {
            SwingUtilities.updateComponentTreeUI(replaceDialog);
        }
    }

    private void updateTextAreaIcon(int actionName, String iconName) {

        Icon icon = getIconGroup().getIcon(iconName);

        Action action = RTextArea.getAction(actionName);
        if (action != null) { // Can be null when the app is first starting up
            action.putValue(Action.SMALL_ICON, icon);
        }
    }
}
