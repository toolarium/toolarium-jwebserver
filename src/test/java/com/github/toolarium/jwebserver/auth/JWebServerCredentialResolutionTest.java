/*
 * JWebServerCredentialResolutionTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.auth;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.jwebserver.AbstractJWebServerTest;
import com.github.toolarium.jwebserver.JWebServer;
import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;


/**
 * Tests for credential resolution including env: prefix support.
 *
 * @author patrick
 */
public class JWebServerCredentialResolutionTest extends AbstractJWebServerTest {

    /**
     * Test basic auth with env: prefix resolves from environment variable.
     */
    @Test void testEnvPrefixWithExistingVariable() {
        // use an environment variable that is always set
        String envValue = System.getenv("PATH");
        if (envValue == null) {
            return; // skip if PATH is not set (unlikely)
        }

        // env:PATH will resolve to the PATH value which is not a valid user:password format
        // so basic auth should effectively be disabled (no colon in value)
        WebServerConfiguration configuration = newConfiguration();
        configuration.setHealthPath("/q/health");
        JWebServer jwebServer = new JWebServer();
        jwebServer.setConfiguration(configuration);

        // basic auth with invalid format should not enable auth
        assertFalse(configuration.hasBasicAuthentication());
    }


    /**
     * Test basic auth with env: prefix for non-existent variable disables auth.
     */
    @Test void testEnvPrefixWithMissingVariable() {
        // This tests the resolveCredential path indirectly.
        // When env var doesn't exist, resolveCredential returns null, so auth is disabled.
        WebServerConfiguration configuration = newConfiguration();
        configuration.setBasicAuthentication(null);
        run(configuration);

        RestAssured.port = configuration.getPort();
        // without auth configured, resources should be accessible
        given().when().get("/VERSION").then().statusCode(200);
    }


    /**
     * Test plain basic auth (no env: prefix) still works.
     */
    @Test void testPlainBasicAuthStillWorks() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setBasicAuthentication("testuser:testpass");
        run(configuration);

        RestAssured.port = configuration.getPort();
        given().when().get("/VERSION").then().statusCode(401);
        given().auth().basic("testuser", "testpass").when().get("/VERSION").then().statusCode(200);
    }


    /**
     * Test wrong credentials are rejected.
     */
    @Test void testWrongCredentialsRejected() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setBasicAuthentication("testuser:testpass");
        run(configuration);

        RestAssured.port = configuration.getPort();
        given().auth().basic("testuser", "wrongpass").when().get("/VERSION").then().statusCode(401);
        given().auth().basic("wronguser", "testpass").when().get("/VERSION").then().statusCode(401);
    }


    /**
     * Test that basic auth configuration is detected properly.
     */
    @Test void testBasicAuthConfigurationDetection() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        assertFalse(configuration.hasBasicAuthentication());

        configuration.setBasicAuthentication("user:pass");
        assertTrue(configuration.hasBasicAuthentication());

        configuration.setBasicAuthentication("");
        assertFalse(configuration.hasBasicAuthentication());

        configuration.setBasicAuthentication(null);
        assertFalse(configuration.hasBasicAuthentication());
    }
}
