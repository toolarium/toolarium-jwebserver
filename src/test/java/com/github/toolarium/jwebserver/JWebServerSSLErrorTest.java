/*
 * JWebServerSSLErrorTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import org.junit.jupiter.api.Test;


/**
 * Tests for JWebServer error handling.
 *
 * @author patrick
 */
public class JWebServerSSLErrorTest extends AbstractJWebServerTest {

    /**
     * Test that hasError is set when SSL port is configured with invalid keystore.
     */
    @Test void testHasErrorOnSSLFailure() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setSecurePort(getNewPort());
        configuration.getSSLServerConfiguration().setKeyStoreFile("nonexistent.p12");

        JWebServer jwebServer = new JWebServer();
        jwebServer.setConfiguration(configuration);
        jwebServer.run();

        assertTrue(jwebServer.hasError());
    }


    /**
     * Test that hasError is false on successful startup.
     */
    @Test void testNoErrorOnNormalStartup() {
        WebServerConfiguration configuration = newConfiguration();
        JWebServer jwebServer = run(configuration);

        assertFalse(jwebServer.hasError());
    }


    /**
     * Test that server reports proxy mode correctly.
     */
    @Test void testProxyModeDetection() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        assertFalse(configuration.isProxyServer());

        configuration.getProxyServerConfiguration().setProxyHostNames("http://localhost:9090");
        assertTrue(configuration.isProxyServer());
    }
}
