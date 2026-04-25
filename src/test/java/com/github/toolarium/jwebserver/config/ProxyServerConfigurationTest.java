/*
 * ProxyServerConfigurationTest.java
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
 * Tests for ProxyServerConfiguration.
 *
 * @author patrick
 */
public class ProxyServerConfigurationTest {

    /**
     * Test default values.
     */
    @Test void testDefaultValues() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        assertTrue(config.rewriteHostHeader());
        assertTrue(config.reuseXForwarded());
        assertEquals(30000, config.getMaxRequestTime());
        assertEquals(20, config.getConnectionsPerThread());
        assertNull(config.getProxyHostNames());
    }


    /**
     * Test rewrite host header setter.
     */
    @Test void testSetRewriteHostHeader() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        config.setRewriteHostHeader(Boolean.FALSE);
        assertFalse(config.rewriteHostHeader());
    }


    /**
     * Test reuse X-Forwarded setter.
     */
    @Test void testSetReuseXForwarded() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        config.setReuseXForwarded(Boolean.FALSE);
        assertFalse(config.reuseXForwarded());
    }


    /**
     * Test max request time setter.
     */
    @Test void testSetMaxRequestTime() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        config.setMaxRequestTime(60000);
        assertEquals(60000, config.getMaxRequestTime());
    }


    /**
     * Test connections per thread setter.
     */
    @Test void testSetConnectionsPerThread() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        config.setConnectionsPerThread(50);
        assertEquals(50, config.getConnectionsPerThread());
    }


    /**
     * Test proxy host names from comma-separated string.
     */
    @Test void testSetProxyHostNamesFromString() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        config.setProxyHostNames("http://host1:8080, https://host2:8443");
        assertArrayEquals(new String[]{"http://host1:8080", "https://host2:8443"}, config.getProxyHostNames());
    }


    /**
     * Test proxy host names from array.
     */
    @Test void testSetProxyHostNamesFromArray() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        config.setProxyHostNames(new String[]{"http://backend1"});
        assertArrayEquals(new String[]{"http://backend1"}, config.getProxyHostNames());
    }


    /**
     * Test null setters are no-ops.
     */
    @Test void testNullSettersAreNoOps() {
        ProxyServerConfiguration config = new ProxyServerConfiguration();
        config.setRewriteHostHeader(null);
        config.setReuseXForwarded(null);
        config.setMaxRequestTime(null);
        config.setConnectionsPerThread(null);

        assertTrue(config.rewriteHostHeader());
        assertTrue(config.reuseXForwarded());
        assertEquals(30000, config.getMaxRequestTime());
        assertEquals(20, config.getConnectionsPerThread());
    }


    /**
     * Test copy constructor.
     */
    @Test void testCopyConstructor() {
        ProxyServerConfiguration original = new ProxyServerConfiguration();
        original.setMaxRequestTime(5000);
        original.setConnectionsPerThread(10);
        original.setRewriteHostHeader(Boolean.FALSE);
        original.setProxyHostNames("http://backend");

        ProxyServerConfiguration copy = new ProxyServerConfiguration(original);
        assertEquals(original, copy);
    }


    /**
     * Test equals and hashCode.
     */
    @Test void testEqualsAndHashCode() {
        ProxyServerConfiguration configOne = new ProxyServerConfiguration();
        ProxyServerConfiguration configTwo = new ProxyServerConfiguration();
        assertEquals(configOne, configTwo);
        assertEquals(configOne.hashCode(), configTwo.hashCode());

        configOne.setMaxRequestTime(99999);
        assertNotEquals(configOne, configTwo);
    }
}
