package org.fife.edisen.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

public class EdisenConfigFileFilterTest {

    private EdisenConfigFileFilter filter;

    private static final String TEST_DESCRIPTION = "test";

    @BeforeEach
    public void setUp() {
        Edisen mockEdisen = Mockito.mock(Edisen.class);
        doReturn(TEST_DESCRIPTION).when(mockEdisen).getString(anyString());
        filter = new EdisenConfigFileFilter(mockEdisen);
    }

    @Test
    public void testAccept_edisenConfigFile() {
        Assertions.assertTrue(filter.accept(new File("test.edisen.json")));
    }

    @Test
    public void testAccept_someOtherFile() {
        Assertions.assertFalse(filter.accept(new File("test.s")));
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(TEST_DESCRIPTION, filter.getDescription());
    }

    @Test
    public void testToString() {
        Assertions.assertEquals(TEST_DESCRIPTION, filter.toString());
    }
}
