package org.fife.edisen.ui;

import org.fife.rsta.ui.GoToDialog;
import org.fife.rsta.ui.search.FindDialog;
import org.fife.rsta.ui.search.ReplaceDialog;
import org.fife.ui.rtextfilechooser.RTextFileChooser;
import org.mockito.Mockito;

import java.awt.*;

/**
 * A "real" Edisen instance with delicate/untestable UI bits replaced.  This can be
 * used in tests for the {@code Edisen} class where we can't simply mock the entire
 * thing.
 */
public class TestableEdisen extends Edisen {

    public RTextFileChooser mockFileChooser;
    public FindDialog mockFindDialog;
    public ReplaceDialog mockReplaceDialog;
    public GoToDialog mockGoToDialog;
    public int exceptionCount;

    private TestableEdisen(EdisenAppContext context, EdisenPrefs prefs) {
        super(context, prefs);
    }

    /**
     * A simple factory method for when you don't need to access the context
     * or preferences that create an instance of this class.
     *
     * @return The instance.
     */
    public static TestableEdisen create() {
        EdisenAppContext context = new EdisenAppContext();
        EdisenPrefs prefs = new EdisenPrefs();
        TestableEdisen edisen = new TestableEdisen(context, prefs);
        return edisen;
    }

    @Override
    protected FindDialog createFindDialog() {
        if (mockFindDialog == null) {
            mockFindDialog = Mockito.mock(FindDialog.class);
        }
        return mockFindDialog;
    }

    @Override
    protected GoToDialog createGoToDialog() {
        if (mockGoToDialog == null) {
            mockGoToDialog = Mockito.mock(GoToDialog.class);
        }
        return mockGoToDialog;
    }

    @Override
    protected ReplaceDialog createReplaceDialog() {
        if (mockReplaceDialog == null) {
            mockReplaceDialog = Mockito.mock(ReplaceDialog.class);
        }
        return mockReplaceDialog;
    }

    /**
     * Overridden to *not* display an exception dialog and keep track of
     * the number of errors displayed.<p>
     *
     * Note there are other overloads of this method that also could be
     * problematic if they are called.
     *
     * @param owner The parent frame.
     * @param t The thrown error.
     * @param desc An optional description of the error.
     */
    @Override
    public void displayException(Frame owner, Throwable t, String desc) {
        exceptionCount++;
        t.printStackTrace();
    }

    @Override
    public RTextFileChooser getFileChooser() {
        if (mockFileChooser == null) {
            mockFileChooser = Mockito.mock(RTextFileChooser.class);
        }
        return mockFileChooser;
    }
}
