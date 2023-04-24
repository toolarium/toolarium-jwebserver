/*
 * JWebServerHealthTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.health;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.jwebserver.AbstractJWebServerTest;
import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;


/**
 * JWebServwer health tests
 *  
 * @author patrick
 */
public class JWebServerHealthTest extends AbstractJWebServerTest {
    
    /**
     * Test enabled health check.
     */
    @Test void testValidHealthCheck() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setHealthPath("/q/health");
        run(configuration);
        
        assertTrue(configuration.hasHealthCheck());
        RestAssured.port = configuration.getPort();
        given().when().get(configuration.getHealthPath()).then().statusCode(200).body(is("{ \"status\": \"UP\" }"));
    }


    /**
     * Test disable health check.
     */
    @Test void testDisabledHealthCheck() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setHealthPath("");
        run(configuration);
        
        assertFalse(configuration.hasHealthCheck());
        RestAssured.port = configuration.getPort();
        given().when().get(configuration.getHealthPath()).then().statusCode(403);

        WebServerConfiguration newConfiguration = newConfiguration();
        newConfiguration.setHealthPath(null);
        
        run(newConfiguration);
        assertFalse(newConfiguration.hasHealthCheck());
    }
}
