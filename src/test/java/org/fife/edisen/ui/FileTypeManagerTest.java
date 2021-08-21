package org.fife.edisen.ui;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTypeManagerTest {

    @Test
    public void testGetType_string_supportedTypes() {
        FileTypeManager ftm = FileTypeManager.get();
        Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_INI, ftm.get("test.ini"));
        Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_JSON, ftm.get("test.json"));
        Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_MARKDOWN, ftm.get("test.md"));
        Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502, ftm.get("test.s"));
    }

    @Test
    public void testGetType_file_supportedTypes() {
        FileTypeManager ftm = FileTypeManager.get();
        Assertions.assertEquals(SyntaxConstants.SYNTAX_STYLE_INI, ftm.get(new File("test.ini")));
    }
}
