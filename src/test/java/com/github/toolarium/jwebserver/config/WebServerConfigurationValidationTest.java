/*
 * WebServerConfigurationValidationTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;


/**
 * Tests for WebServerConfiguration port validation.
 *
 * @author patrick
 */
public class WebServerConfigurationValidationTest {

    /**
     * Test valid port values are accepted.
     */
    @Test void testValidPorts() {
        WebServerConfiguration config = new WebServerConfiguration();
        assertDoesNotThrow(() -> config.setPort(1));
        assertEquals(1, config.getPort());

        assertDoesNotThrow(() -> config.setPort(8080));
        assertEquals(8080, config.getPort());

        assertDoesNotThrow(() -> config.setPort(65535));
        assertEquals(65535, config.getPort());
    }


    /**
     * Test port zero is rejected.
     */
    @Test void testPortZeroRejected() {
        WebServerConfiguration config = new WebServerConfiguration();
        assertThrows(IllegalArgumentException.class, () -> config.setPort(0));
    }


    /**
     * Test negative port is rejected.
     */
    @Test void testNegativePortRejected() {
        WebServerConfiguration config = new WebServerConfiguration();
        assertThrows(IllegalArgumentException.class, () -> config.setPort(-1));
    }


    /**
     * Test port above 65535 is rejected.
     */
    @Test void testPortAboveMaxRejected() {
        WebServerConfiguration config = new WebServerConfiguration();
        assertThrows(IllegalArgumentException.class, () -> config.setPort(65536));
    }


    /**
     * Test valid secure port values are accepted.
     */
    @Test void testValidSecurePorts() {
        WebServerConfiguration config = new WebServerConfiguration();
        assertDoesNotThrow(() -> config.setSecurePort(443));
        assertEquals(443, config.getSecurePort());
    }


    /**
     * Test invalid secure port is rejected.
     */
    @Test void testInvalidSecurePortRejected() {
        WebServerConfiguration config = new WebServerConfiguration();
        assertThrows(IllegalArgumentException.class, () -> config.setSecurePort(0));
        assertThrows(IllegalArgumentException.class, () -> config.setSecurePort(-100));
        assertThrows(IllegalArgumentException.class, () -> config.setSecurePort(70000));
    }


    /**
     * Test null port is accepted (no-op).
     */
    @Test void testNullPortAccepted() {
        WebServerConfiguration config = new WebServerConfiguration();
        assertDoesNotThrow(() -> config.setPort(null));
        assertDoesNotThrow(() -> config.setSecurePort(null));
    }
}
