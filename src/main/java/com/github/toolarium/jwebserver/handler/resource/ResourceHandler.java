/*
 * ResourceHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.resource;

import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.handler.auth.BasicAuthenticationHttpHandler;
import io.undertow.Handlers;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Methods;
import java.nio.file.Paths;


/**
* Basic authentication utility
* 
* @author patrick
*/
public final class ResourceHandler {
    /** SLASH */
    public static final String SLASH = "/";


    /**
     * Constructor for ResourceHandler
     */
    private ResourceHandler() {
        // NOP
    }

    
    /**
     * Add basic authentication
     *
     * @param configuration the configuration
     * @param routinrgHandler the routing handler
     * @return the handler
     */
    public static RoutingHandler addHandler(final IWebServerConfiguration configuration, final RoutingHandler routinrgHandler) { 
        String path = configuration.getDirectory();
        io.undertow.server.handlers.resource.ResourceHandler resourceHandler;
                
        if (configuration.readFromClasspath()) {
            if (path == null) {
                path = "";
            }
            
            resourceHandler = Handlers.resource(new com.github.toolarium.jwebserver.handler.resource.ClassPathResourceManager(configuration, ResourceHandler.class.getClassLoader(), path));
        } else {
            if (path == null) {
                path = ".";
            }
            
            resourceHandler = Handlers.resource(new PathResourceManager(configuration, Paths.get(path), 10));
        }

        if (configuration.getWelcomeFiles() != null) {
            resourceHandler.setWelcomeFiles(configuration.getWelcomeFiles());
        }
        
        resourceHandler.setDirectoryListingEnabled(configuration.isDirectoryListingEnabled());
        
        String resourcePath = configuration.getResourcePath();
        if (resourcePath == null || resourcePath.isBlank()) {
            resourcePath = SLASH;
        }

        routinrgHandler.add(Methods.GET, resourcePath + "*", BasicAuthenticationHttpHandler.addHandler(configuration, resourceHandler));
        //routinrgHandler.setFallbackHandler(new RedirectHandler(resourcePath));
        return routinrgHandler;
    }
}
