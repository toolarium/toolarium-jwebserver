/*
 * JWebServerPathTraversalTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.resource;

import static io.restassured.RestAssured.given;

import com.github.toolarium.jwebserver.AbstractJWebServerTest;
import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;


/**
 * Tests for path traversal protection in resource managers.
 *
 * @author patrick
 */
public class JWebServerPathTraversalTest extends AbstractJWebServerTest {

    /**
     * Test path traversal with .. in path is blocked.
     */
    @Test void testPathTraversalBlockedOnPathAccess() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.getResourceServerConfiguration().setDirectory("src/test/resources");
        run(configuration);

        RestAssured.port = configuration.getPort();
        given().when().get("/../build.gradle").then().statusCode(404);
        given().when().get("/mypath/../../build.gradle").then().statusCode(404);
        given().when().get("/%2e%2e/build.gradle").then().statusCode(404);
    }


    /**
     * Test path traversal with .. in path is blocked on classpath access.
     */
    @Test void testPathTraversalBlockedOnClasspathAccess() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.getResourceServerConfiguration().setDirectory("", Boolean.TRUE);
        run(configuration);

        RestAssured.port = configuration.getPort();
        given().when().get("/../../../etc/passwd").then().statusCode(404);
        given().when().get("/mypath/../../etc/passwd").then().statusCode(404);
    }


    /**
     * Test that normal resource access still works alongside traversal protection.
     */
    @Test void testNormalAccessStillWorks() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.getResourceServerConfiguration().setDirectory("src/test/resources");
        run(configuration);

        RestAssured.port = configuration.getPort();
        given().when().get("/testfile.json").then().statusCode(200);
        given().when().get("/mypath/index.json").then().statusCode(200);
    }
}
