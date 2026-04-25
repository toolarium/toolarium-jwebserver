/*
 * ResourceServerConfigurationTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Tests for ResourceServerConfiguration.
 *
 * @author patrick
 */
public class ResourceServerConfigurationTest {

    /**
     * Test default values.
     */
    @Test void testDefaultValues() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        assertEquals(".", config.getDirectory());
        assertTrue(config.isLocalDirectory());
        assertFalse(config.readFromClasspath());
        assertFalse(config.isDirectoryListingEnabled());
        assertTrue(config.resolveParentResourceIfNotFound());
        assertArrayEquals(new String[]{"index.html", "index.htm", "default.html", "default.htm"}, config.getWelcomeFiles());
        assertNull(config.getSupportedFileExtensions());
    }


    /**
     * Test setting directory from classpath.
     */
    @Test void testSetDirectoryFromClasspath() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setDirectory("META-INF", Boolean.TRUE);
        assertEquals("META-INF", config.getDirectory());
        assertTrue(config.readFromClasspath());
        assertFalse(config.isLocalDirectory());
    }


    /**
     * Test setting directory from filesystem.
     */
    @Test void testSetDirectoryFromFilesystem() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setDirectory("src/test/resources", Boolean.FALSE);
        assertEquals("src/test/resources", config.getDirectory());
        assertFalse(config.readFromClasspath());
        assertTrue(config.isLocalDirectory());
    }


    /**
     * Test HOME variable expansion in directory.
     */
    @Test void testHomeDirectoryExpansion() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        String userHome = System.getProperty("user.home");

        config.setDirectory("$HOME", Boolean.FALSE);
        assertEquals(userHome, config.getDirectory());

        config.setDirectory("%HOME%", Boolean.FALSE);
        assertEquals(userHome, config.getDirectory());
    }


    /**
     * Test directory listing toggle.
     */
    @Test void testSetDirectoryListingEnabled() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setDirectoryListingEnabled(Boolean.TRUE);
        assertTrue(config.isDirectoryListingEnabled());

        config.setDirectoryListingEnabled(Boolean.FALSE);
        assertFalse(config.isDirectoryListingEnabled());
    }


    /**
     * Test null directory listing is ignored.
     */
    @Test void testSetDirectoryListingNull() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setDirectoryListingEnabled(null);
        assertFalse(config.isDirectoryListingEnabled());
    }


    /**
     * Test resolve parent resource toggle.
     */
    @Test void testSetResolveParentResourceIfNotFound() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setResolveParentResourceIfNotFound(Boolean.FALSE);
        assertFalse(config.resolveParentResourceIfNotFound());

        config.setResolveParentResourceIfNotFound(Boolean.TRUE);
        assertTrue(config.resolveParentResourceIfNotFound());
    }


    /**
     * Test welcome files from comma-separated string.
     */
    @Test void testSetWelcomeFilesFromString() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setWelcomeFiles("index.html, index.json");
        assertArrayEquals(new String[]{"index.html", "index.json"}, config.getWelcomeFiles());
    }


    /**
     * Test supported file extensions auto-prefix dot.
     */
    @Test void testSupportedFileExtensionsAutoPrefixDot() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setSupportedFileExtensions(new String[]{"json", "html"});
        assertArrayEquals(new String[]{".json", ".html"}, config.getSupportedFileExtensions());
    }


    /**
     * Test supported file extensions preserves existing dot.
     */
    @Test void testSupportedFileExtensionsPreservesDot() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        config.setSupportedFileExtensions(new String[]{".json"});
        assertArrayEquals(new String[]{".json"}, config.getSupportedFileExtensions());
    }


    /**
     * Test copy constructor.
     */
    @Test void testCopyConstructor() {
        ResourceServerConfiguration original = new ResourceServerConfiguration();
        original.setDirectory("testdir", Boolean.TRUE);
        original.setDirectoryListingEnabled(Boolean.TRUE);
        original.setWelcomeFiles("index.html");
        original.setSupportedFileExtensions(".json");

        ResourceServerConfiguration copy = new ResourceServerConfiguration(original);
        assertEquals(original, copy);
    }


    /**
     * Test equals and hashCode.
     */
    @Test void testEqualsAndHashCode() {
        ResourceServerConfiguration configOne = new ResourceServerConfiguration();
        ResourceServerConfiguration configTwo = new ResourceServerConfiguration();
        assertEquals(configOne, configTwo);
        assertEquals(configOne.hashCode(), configTwo.hashCode());

        configOne.setDirectoryListingEnabled(Boolean.TRUE);
        assertNotEquals(configOne, configTwo);
    }


    /**
     * Test equals edge cases.
     */
    @Test void testEqualsEdgeCases() {
        ResourceServerConfiguration config = new ResourceServerConfiguration();
        assertEquals(config, config);
        assertNotEquals(null, config);
    }
}
