/*
 * AbstractJWebServerTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver;

import com.github.toolarium.jwebserver.config.WebServerConfiguration;

/**
 * Base test class
 * @author patrick
 */
public abstract class AbstractJWebServerTest {
    protected static final String MYPATH = "mypath";
    protected static final String SUBPATH = "subpath";
    protected static final String INDEX_JSON = "index.json";
    private static final int PORT_BASE = 20000;
    private static int port = PORT_BASE;
    
    
    /**
     * Create a new configuration
     * 
     * @return the new configuration
     */
    protected WebServerConfiguration newConfiguration() {
        WebServerConfiguration configuration = new WebServerConfiguration();
        configuration.setIoThreads(1);
        configuration.setPort(getNewPort());
        configuration.setBasicAuthentication(null);
        configuration.setHealthPath(null);
        return configuration;
    }

    
    /**
     * Run a webserver configuration
     * 
     * @param configuration the configuration
     * @return the webserver
     */
    protected JWebServer run(WebServerConfiguration configuration) {
        JWebServer jwebserver = new JWebServer();
        jwebserver.setConfiguration(configuration);
        jwebserver.run();
        return jwebserver;
    }
   

    /**
     * Get new free port
     *
     * @return the port
     */
    protected int getNewPort() {
        return ++port;
    }
}
