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
import com.github.toolarium.jwebserver.handler.resource.ResourceHandler;
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
        
        assertEquals(ResourceHandler.SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();
        
        given().when().get(ResourceHandler.SLASH).then().statusCode(403);
        given().when().get(ResourceHandler.SLASH + "testfile.json").then().statusCode(200);
        given().when().get("testfile.json").then().statusCode(200);
        
        
        given().get(ResourceHandler.SLASH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);        
    }

    
    /**
     * Test redirect resource access
     */
    @Test void testRedirectResourceAccessWithoutAppendix() {
        WebServerConfiguration configuration = newConfiguration();
        configuration.setSupportedFileExtensions(".json");
        setDirectory(configuration);
        run(configuration);
        
        assertEquals(ResourceHandler.SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();
        
        given().when().get(ResourceHandler.SLASH).then().statusCode(403);
        given().when().get(ResourceHandler.SLASH + "testfile").then().statusCode(200);
        given().when().get("testfile").then().statusCode(200);
        
        
        given().get(ResourceHandler.SLASH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);        
    }

    
    /**
     * Test resource access with prefix.
     */
    @Test void testResourceAccessWithPrefix() {
        WebServerConfiguration configuration = newConfiguration();
        setDirectory(configuration);
        configuration.setResourcePath(ResourceHandler.SLASH + MYPATH);
        run(configuration);

        assertEquals(ResourceHandler.SLASH + MYPATH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(ResourceHandler.SLASH + MYPATH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + SUBPATH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH).then().statusCode(403);
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
    }

    
    /**
     * Test resource with prefix and adapted index.
     */
    @Test void testResourceAccessWithPrefixAndAdaptedIndex() {
        WebServerConfiguration configuration = newConfiguration();
        setDirectory(configuration);
        configuration.setResourcePath(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH);
        configuration.setWelcomeFiles("index.html, index.htm, index.json");
        run(configuration);
        
        assertEquals(ResourceHandler.SLASH + MYPATH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(ResourceHandler.SLASH + MYPATH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + SUBPATH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
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
        
        assertEquals(ResourceHandler.SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(ResourceHandler.SLASH + MYPATH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + SUBPATH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
    }

    
    
    /**
     * Set the base directory on the configuration
     *
     * @param configuration the configuration
     */
    protected abstract void setDirectory(WebServerConfiguration configuration);
}
