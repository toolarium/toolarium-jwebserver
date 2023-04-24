/*
 * AbstractJWebserverResourceAccessTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.resource;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.jwebserver.AbstractJWebServerTest;
import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;


/**
 * Defines the resource test cases
 * 
 * @author patrick
 */
public abstract class AbstractJWebserverResourceAccessTest extends AbstractJWebServerTest {
    
    
    /**
     * Test redirect resource access
     */
    @Test void testRedirectResourceAccess() {
        WebServerConfiguration configuration = newConfiguration();
        setDirectory(configuration);
        run(configuration);
        
        assertEquals(SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();
        
        given().when().get(SLASH).then().statusCode(403);
        given().when().get(SLASH + "testfile.json").then().statusCode(200);
        given().when().get("testfile.json").then().statusCode(200);
        
        
        given().get(SLASH).then().statusCode(403);
        given().get(SLASH + MYPATH).then().statusCode(403);
        given().get(SLASH + MYPATH + SLASH).then().statusCode(403);
        given().get(SLASH + MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        
    }

    
    /**
     * Test resource access with prefix.
     */
    @Test void testResourceAccessWithPrefix() {
        WebServerConfiguration configuration = newConfiguration();
        setDirectory(configuration);
        configuration.setResourcePath(SLASH + MYPATH);
        run(configuration);

        assertEquals(SLASH + MYPATH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(SLASH + MYPATH).then().statusCode(403);
        given().get(SLASH + MYPATH + SLASH).then().statusCode(403);
        given().get(SLASH + MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH + SUBPATH).then().statusCode(403);
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH).then().statusCode(403);
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + SUBPATH + SLASH + INDEX_JSON).then().statusCode(200);
    }

    
    /**
     * Test resource with prefix and adapted index.
     */
    @Test void testResourceAccessWithPrefixAndAdaptedIndex() {
        WebServerConfiguration configuration = newConfiguration();
        setDirectory(configuration);
        configuration.setResourcePath(SLASH + MYPATH + SLASH);
        configuration.setWelcomeFiles("index.html, index.htm, index.json");
        run(configuration);
        
        assertEquals(SLASH + MYPATH + SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(SLASH + MYPATH).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH + SUBPATH).then().statusCode(200);
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH).then().statusCode(200);
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + SUBPATH + SLASH + INDEX_JSON).then().statusCode(200);
    }

    
    /**
     * Test resource with adapted index.
     */
    @Test void testResourceAccessAdaptedIndex() {
        WebServerConfiguration configuration = newConfiguration();
        setDirectory(configuration);
        //configuration.setResourcePath("");
        configuration.setWelcomeFiles("index.html, index.htm, index.json");
        run(configuration);
        
        assertEquals(SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(SLASH + MYPATH).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH + SUBPATH).then().statusCode(200);
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH).then().statusCode(200);
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + SUBPATH + SLASH + INDEX_JSON).then().statusCode(200);
    }

    
    
    /**
     * Set the base directory on the configuration
     *
     * @param configuration the configuration
     */
    protected abstract void setDirectory(WebServerConfiguration configuration);
}
