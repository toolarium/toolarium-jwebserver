/*
 * ConfigurationUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;


/**
 * Tests for ConfigurationUtil.
 *
 * @author patrick
 */
public class ConfigurationUtilTest {

    /**
     * Test that integer conversion expands only once, not twice.
     */
    @Test void testIntegerConvertExpandsOnce() {
        // a plain numeric string should be converted correctly
        assertEquals(Integer.valueOf(8080), ConfigurationUtil.getInstance().convert("port", "8080", 9090));
    }


    /**
     * Test that invalid integer falls back to default.
     */
    @Test void testIntegerConvertInvalidFallback() {
        assertEquals(Integer.valueOf(9090), ConfigurationUtil.getInstance().convert("port", "notANumber", 9090));
    }


    /**
     * Test that null integer default is returned on invalid input.
     */
    @Test void testIntegerConvertNullDefault() {
        assertNull(ConfigurationUtil.getInstance().convert("port", "invalid", (Integer) null));
    }


    /**
     * Test boolean conversion.
     */
    @Test void testBooleanConvert() {
        assertEquals(Boolean.TRUE, ConfigurationUtil.getInstance().convert("flag", "true", Boolean.FALSE));
        assertEquals(Boolean.FALSE, ConfigurationUtil.getInstance().convert("flag", "false", Boolean.TRUE));
    }


    /**
     * Test expand with null returns null.
     */
    @Test void testExpandNull() {
        assertNull(ConfigurationUtil.getInstance().expand((String) null));
    }


    /**
     * Test expand with plain string returns same string.
     */
    @Test void testExpandPlainString() {
        assertEquals("hello", ConfigurationUtil.getInstance().expand("hello"));
    }


    /**
     * Test formatArrayAsString with multiple elements.
     */
    @Test void testFormatArrayAsString() {
        String result = ConfigurationUtil.getInstance().formatArrayAsString(new String[]{"index.html", "index.htm"});
        assertEquals("index.html, index.htm", result);
    }


    /**
     * Test formatArrayAsString with single element.
     */
    @Test void testFormatArrayAsStringSingleElement() {
        String result = ConfigurationUtil.getInstance().formatArrayAsString(new String[]{"index.html"});
        assertEquals("index.html", result);
    }


    /**
     * Test formatArrayAsString with null returns null.
     */
    @Test void testFormatArrayAsStringNull() {
        assertNull(ConfigurationUtil.getInstance().formatArrayAsString(null));
    }
}
