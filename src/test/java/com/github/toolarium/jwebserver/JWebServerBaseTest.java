/*
 * JWebServerTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */

package com.github.toolarium.jwebserver;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;



/**
 * JWebServer general tests.
 */
public class JWebServerBaseTest extends AbstractJWebServerTest {
    
    /**
     * Test default behaviour.
     */
    @Test void testDefaultBehaviour() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setWelcomeFiles("index.html, index.htm");
        configuration.setDirectory("META-INF", Boolean.TRUE);
        run(configuration);

        RestAssured.port = configuration.getPort();
        given().when().get("/LICENSE").then().statusCode(200);
        given().when().get("/NOTICE").then().statusCode(200);
        given().when().get("/versions").then().statusCode(403); // is a directory
    }

    

    /**
     * Test MyApplication method.
     */
    @Test void testWebserverName() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(getNewPort());
        configuration.setIoThreads(1);
        configuration.setWebserverName("My Webserver");
        
        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();
        
        assertTrue(configuration.hasHealthCheck());
        RestAssured.port = configuration.getPort();
        given().when().get(configuration.getHealthPath()).then().statusCode(200).body(is("{ \"status\": \"UP\" }"));
    }
}
