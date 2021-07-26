package org.fife.edisen.ui;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextfilechooser.Utilities;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * A registry of file extensions to {@code RSyntaxTextArea} syntax types.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class FileTypeManager {

    private static final FileTypeManager INSTANCE = new FileTypeManager();

    private final Map<String, String> map;

    /**
     * Private constructor to prevent instantiation.
     */
    private FileTypeManager() {
        map = new HashMap<>();
        initializeMap();
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance of this class.
     */
    public static FileTypeManager get() {
        return INSTANCE;
    }

    /**
     * Returns the syntax style to use for the specified file.
     *
     * @param file The file to inspect.
     * @return The syntax style.  This will be a value from {@code SyntaxConstants}, or
     *          {@code null} if the file type is not known.
     * @see #get(String)
     */
    public String get(File file) {
        return get(file.getName());
    }

    /**
     * Returns the syntax style to use for the specified file.
     *
     * @param fileName The file to inspect.
     * @return The syntax style.  This will be a value from {@code SyntaxConstants}, or
     *          {@code null} if the file type is not known.
     * @see #get(File)
     */
    public String get(String fileName) {
        String extension = Utilities.getExtension(fileName);
        return map.get(extension);
    }

    private void initializeMap() {

        map.put("ini", SyntaxConstants.SYNTAX_STYLE_INI);
        map.put("json", SyntaxConstants.SYNTAX_STYLE_JSON);
        map.put("md", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        map.put("s", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
    }
}
