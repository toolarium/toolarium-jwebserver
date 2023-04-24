/*
 * JWebServerClasspathAccessTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.resource;

import com.github.toolarium.jwebserver.config.WebServerConfiguration;


/**
 * JWebServer classpath tests
 * 
 * @author patrick
 */
public class JWebServerClasspathAccessTest extends AbstractJWebserverResourceAccessTest {

    /**
     * @see com.github.toolarium.jwebserver.resource.AbstractJWebserverResourceAccessTest#setDirectory(com.github.toolarium.jwebserver.config.WebServerConfiguration)
     */
    @Override
    protected void setDirectory(WebServerConfiguration configuration) {
        configuration.setDirectory("", Boolean.TRUE);
    }

}
