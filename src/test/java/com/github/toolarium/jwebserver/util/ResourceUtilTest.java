/*
 * ResourceUtilTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;


/**
 * Tests for ResourceUtil.
 *
 * @author patrick
 */
public class ResourceUtilTest {

    /**
     * Test slashify adds trailing slash.
     */
    @Test void testSlashifyAddsTrailingSlash() {
        assertEquals("path/", ResourceUtil.getInstance().slashify("path"));
    }


    /**
     * Test slashify preserves existing trailing slash.
     */
    @Test void testSlashifyPreservesExistingSlash() {
        assertEquals("path/", ResourceUtil.getInstance().slashify("path/"));
    }


    /**
     * Test slashify with null returns null.
     */
    @Test void testSlashifyNull() {
        assertNull(ResourceUtil.getInstance().slashify(null));
    }


    /**
     * Test canonicalize removes path traversal sequences.
     */
    @Test void testCanonicalizeRemovesTraversal() {
        String result = ResourceUtil.getInstance().canonicalize("/a/b/../c");
        assertEquals("/a/c", result);
    }


    /**
     * Test canonicalize with clean path returns same.
     */
    @Test void testCanonicalizeCleanPath() {
        assertEquals("/a/b/c", ResourceUtil.getInstance().canonicalize("/a/b/c"));
    }


    /**
     * Test prepareString builds path from segments.
     */
    @Test void testPrepareString() {
        String[] input = {"", "mypath", "subpath"};
        assertEquals("/mypath/subpath/", ResourceUtil.getInstance().prepareString(input, 2));
    }


    /**
     * Test prepareString with zero size returns slash.
     */
    @Test void testPrepareStringZeroSize() {
        String[] input = {"a", "b"};
        assertEquals("/", ResourceUtil.getInstance().prepareString(input, 0));
    }


    /**
     * Test prepareString with null returns slash.
     */
    @Test void testPrepareStringNull() {
        assertEquals("/", ResourceUtil.getInstance().prepareString(null, 5));
    }


    /**
     * Test prepareString with empty array returns slash.
     */
    @Test void testPrepareStringEmptyArray() {
        assertEquals("/", ResourceUtil.getInstance().prepareString(new String[0], 5));
    }


    /**
     * Test toString with null resource returns empty string.
     */
    @Test void testToStringNullResource() {
        assertEquals("", ResourceUtil.getInstance().toString(null));
    }
}
