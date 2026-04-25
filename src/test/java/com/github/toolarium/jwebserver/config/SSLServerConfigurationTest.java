/*
 * SSLServerConfigurationTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


/**
 * Tests for SSLServerConfiguration.
 *
 * @author patrick
 */
public class SSLServerConfigurationTest {

    /**
     * Test default values.
     */
    @Test void testDefaultValues() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        assertFalse(config.trustAnyCertificate());
        assertNull(config.getTrustKeyStoreFile());
        assertNull(config.getKeyStoreFile());
        assertNull(config.getKeyStoreAlias());
        assertNull(config.getKeyStorePassword());
        assertEquals("PKCS12", config.getKeyStoreType());
    }


    /**
     * Test trust any certificate setter.
     */
    @Test void testSetTrustAnyCertificate() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        config.setTrustAnyCertificate(Boolean.TRUE);
        assertTrue(config.trustAnyCertificate());

        config.setTrustAnyCertificate(Boolean.FALSE);
        assertFalse(config.trustAnyCertificate());
    }


    /**
     * Test null trust any certificate is ignored.
     */
    @Test void testSetTrustAnyCertificateNull() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        config.setTrustAnyCertificate(null);
        assertFalse(config.trustAnyCertificate());
    }


    /**
     * Test keystore file setter.
     */
    @Test void testSetKeyStoreFile() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        config.setKeyStoreFile("mykey.p12");
        assertEquals("mykey.p12", config.getKeyStoreFile());
    }


    /**
     * Test keystore alias setter.
     */
    @Test void testSetKeyStoreAlias() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        config.setKeyStoreAlias("myalias");
        assertEquals("myalias", config.getKeyStoreAlias());
    }


    /**
     * Test trust keystore file setter.
     */
    @Test void testSetTrustKeyStoreFile() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        config.setTrustKeyStoreFile("truststore.jks");
        assertEquals("truststore.jks", config.getTrustKeyStoreFile());
    }


    /**
     * Test keystore type setter.
     */
    @Test void testSetKeyStoreType() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        config.setKeyStoreType("JKS");
        assertEquals("JKS", config.getKeyStoreType());
    }


    /**
     * Test equals and hashCode.
     */
    @Test void testEqualsAndHashCode() {
        SSLServerConfiguration configOne = new SSLServerConfiguration();
        SSLServerConfiguration configTwo = new SSLServerConfiguration();
        assertEquals(configOne, configTwo);
        assertEquals(configOne.hashCode(), configTwo.hashCode());

        configOne.setKeyStoreAlias("alias1");
        assertNotEquals(configOne, configTwo);
    }


    /**
     * Test equals with same instance.
     */
    @Test void testEqualsSameInstance() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        assertEquals(config, config);
    }


    /**
     * Test equals with null.
     */
    @Test void testEqualsNull() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        assertNotEquals(null, config);
    }


    /**
     * Test toString contains relevant fields.
     */
    @Test void testToString() {
        SSLServerConfiguration config = new SSLServerConfiguration();
        String result = config.toString();
        assertNotNull(result);
        assertTrue(result.contains("trustAnyCertificate=false"));
        assertTrue(result.contains("PKCS12"));
    }
}
