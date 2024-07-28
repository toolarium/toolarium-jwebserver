/*
 * ResourceHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.routing.resource;

import com.github.toolarium.jwebserver.config.IResourceServerConfiguration;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.handler.routing.RoutingHandler;
import io.undertow.Handlers;
import java.nio.file.Paths;


/**
 * Defines the resource handler
 * 
 * @author patrick
 */
public final class ResourceHandler {
    
    
    /**
     * Constructor for ResourceHandler
     */
    private ResourceHandler() {
        // NOP
    }

    
    /**
     * Create resource handler
     *
     * @param webServerConfiguration the web server configuration
     * @return the resource handler
     */
    public static io.undertow.server.handlers.resource.ResourceHandler createResourceHandler(final IWebServerConfiguration webServerConfiguration) {
        IResourceServerConfiguration configuration = webServerConfiguration.getResourceServerConfiguration();
        String path = configuration.getDirectory();
                
        io.undertow.server.handlers.resource.ResourceHandler resourceHandler;
        if (webServerConfiguration.getResourceServerConfiguration().readFromClasspath()) {
            if (path == null) {
                path = "";
            }
            
            resourceHandler = Handlers.resource(new com.github.toolarium.jwebserver.handler.routing.resource.ClassPathResourceManager(webServerConfiguration, ResourceHandler.class.getClassLoader(), path));
        } else {
            if (path == null) {
                path = ".";
            }
            
            resourceHandler = Handlers.resource(new PathResourceManager(webServerConfiguration, Paths.get(path), 10));
        }

        if (webServerConfiguration.getResourceServerConfiguration().getWelcomeFiles() != null) {
            resourceHandler.setWelcomeFiles(webServerConfiguration.getResourceServerConfiguration().getWelcomeFiles());
        }
        
        resourceHandler.setDirectoryListingEnabled(webServerConfiguration.getResourceServerConfiguration().isDirectoryListingEnabled());
        
        String resourcePath = webServerConfiguration.getResourcePath();
        if (resourcePath == null || resourcePath.isBlank()) {
            resourcePath = RoutingHandler.SLASH;
        }
        
        return resourceHandler;
    }
}
