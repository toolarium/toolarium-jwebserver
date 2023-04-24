/*
 * JWebServerBasicAuthTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.auth;

import static io.restassured.RestAssured.given;

import com.github.toolarium.jwebserver.AbstractJWebServerTest;
import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;


/**
 * Test the basic authentication
 *  
 * @author patrick
 */
public class JWebServerBasicAuthTest extends AbstractJWebServerTest {
    
    /**
     * Test basic authentication.
     */
    @Test void testBasicAuth() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setHealthPath("/q/health");
        configuration.setBasicAuthentication("user:password");
        run(configuration);

        RestAssured.port = configuration.getPort();
        given().when().get("/VERSION").then().statusCode(401);
        given().auth().basic("user", "password").when().get("/VERSION").then().statusCode(200);
    }
}
