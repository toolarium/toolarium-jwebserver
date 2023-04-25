/*
 * JWebServerClasspathAccessTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.resource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import com.github.toolarium.jwebserver.handler.resource.ResourceHandler;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;


/**
 * JWebServer classpath tests
 * 
 * @author patrick
 */
public class JWebServerClasspathAccessTest extends AbstractJWebserverResourceAccessTest {
    private static final String B = "b";
    private static final String A = "a";


    /**
     * @see com.github.toolarium.jwebserver.resource.AbstractJWebserverResourceAccessTest#setDirectory(com.github.toolarium.jwebserver.config.WebServerConfiguration)
     */
    @Override
    protected void setDirectory(WebServerConfiguration configuration) {
        configuration.setDirectory("", Boolean.TRUE);
    }

    
    /**
     * Test resource with adapted index.
     */
    @Test void testResourceAccessAdaptedIndex2() {
        WebServerConfiguration configuration = newConfiguration();
        setDirectory(configuration);
        //configuration.setResourcePath("");
        configuration.setWelcomeFiles("index.html, index.htm, index.json");
        run(configuration);
        
        assertEquals(ResourceHandler.SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(ResourceHandler.SLASH + MYPATH).body();
        given().get(ResourceHandler.SLASH + MYPATH).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + ResourceHandler.SLASH + INDEX_JSON).then().statusCode(200);
        given().get(ResourceHandler.SLASH + MYPATH + ResourceHandler.SLASH + SUBPATH).then().log().all().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(ResourceHandler.SLASH + MYPATH  + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(MYPATH + ResourceHandler.SLASH + SUBPATH + ResourceHandler.SLASH + INDEX_JSON).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
    }
}
