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
        
        assertEquals(SLASH, configuration.getResourcePath());
        RestAssured.port = configuration.getPort();

        given().get(SLASH + MYPATH).body();
        given().get(SLASH + MYPATH).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(SLASH + MYPATH + SLASH).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(SLASH + MYPATH + SLASH).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(MYPATH + SLASH + INDEX_JSON).then().statusCode(200);
        given().get(SLASH + MYPATH + SLASH + SUBPATH).then().log().all().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(SLASH + MYPATH  + SLASH + SUBPATH + SLASH + INDEX_JSON).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
        given().get(MYPATH + SLASH + SUBPATH + SLASH + INDEX_JSON).then().log().ifValidationFails().assertThat().contentType(ContentType.JSON).statusCode(200).body(A, equalTo(B));
    }
}
