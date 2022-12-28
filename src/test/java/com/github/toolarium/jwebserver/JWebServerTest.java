/*
 * JWebServerTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */

package com.github.toolarium.jwebserver;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;


/**
 * JWebServerTest.
 */
public class JWebServerTest {
    
    /**
     * Test MyApplication method.
     */
    @Test void testSomeLibraryMethod() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(10001);
        configuration.setWelcomeFiles("index.html, index.htm");
        configuration.setDirectory("META-INF", Boolean.TRUE);
        
        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();
        
        RestAssured.port = configuration.getPort();
        given().when().get("/LICENSE").then().statusCode(200);
        given().when().get("/NOTICE").then().statusCode(200);
        given().when().get("/versions").then().statusCode(403); // is a directory
        
    }
    
    
    /**
     * Test MyApplication method.
     */
    @Test void testValidHealthCheck() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(10002);
        configuration.setIoThreads(1);
        
        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();
        
        assertTrue(configuration.hasHealthCheck());
        RestAssured.port = configuration.getPort();
        given().when().get(configuration.getHealthPath()).then().statusCode(200).body(is("{ \"status\": \"UP\" }"));
    }

    
    /**
     * Test MyApplication method.
     */
    @Test void testWebserverName() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(10004);
        configuration.setIoThreads(1);
        configuration.setWebserverName("My Webserver");
        
        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();
        
        assertTrue(configuration.hasHealthCheck());
        RestAssured.port = configuration.getPort();
        given().when().get(configuration.getHealthPath()).then().statusCode(200).body(is("{ \"status\": \"UP\" }"));
    }

    
    /**
     * Test MyApplication method.
     */
    @Test void testDisabledHealthCheck() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(10005);
        configuration.setIoThreads(1);
        configuration.setHealthPath("");
        
        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();
        
        assertFalse(configuration.hasHealthCheck());
        RestAssured.port = configuration.getPort();
        given().when().get(configuration.getHealthPath()).then().statusCode(403);
        
        jwebserver = new JWebServer();
        configuration = new WebServerConfiguration();
        configuration.setPort(10006);
        configuration.setIoThreads(1);
        configuration.setHealthPath(null);
        jwebserver.setConfiguration(configuration);
        jwebserver.run();
        
        assertFalse(configuration.hasHealthCheck());
    }

    
    /**
     * Test MyApplication method.
     */
    @Test void testRedirectResourceAccess() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(10007);
        configuration.setIoThreads(1);
        configuration.setBasicAuthentication(null);
        configuration.setHealthPath(null);
        configuration.setDirectory("META-INF", Boolean.TRUE);

        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();

        assertEquals("/", configuration.getResourcePath());
        RestAssured.port = configuration.getPort();
        
        given().when().get("/").then().statusCode(403);
        given().when().get("/LICENSE").then().statusCode(200);
        given().when().get("LICENSE").then().statusCode(200);
    }

    
    /**
     * Test MyApplication method.
     */
    @Test void testResourceAccess() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(10008);
        configuration.setIoThreads(1);
        configuration.setBasicAuthentication(null);
        configuration.setHealthPath(null);
        configuration.setDirectory(".");

        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();

        assertEquals("/", configuration.getResourcePath());
        RestAssured.port = configuration.getPort();
        
        given().when().get("/").then().statusCode(403);
        given().get("/VERSION").then().statusCode(200);
        given().get("VERSION").then().statusCode(200);
    }

    
    /**
     * Test MyApplication method.
     */
    @Test void testBasicAuth() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setPort(10009);
        configuration.setIoThreads(1);
        configuration.setBasicAuthentication("user:password");
        
        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();

        RestAssured.port = configuration.getPort();
        given().when().get("/VERSION").then().statusCode(401);
        given().auth().basic("user", "password").when().get("/VERSION").then().statusCode(200);
    }
}
