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
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.util.Methods;
import java.nio.file.Paths;



/**
* Basic authentication utility
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
            
            resourceHandler = Handlers.resource(new ClassPathResourceManager(ResourceHandler.class.getClassLoader(), path));
        } else {
            if (path == null) {
                path = ".";
            }
            
            resourceHandler = Handlers.resource(new PathResourceManager(Paths.get(path), 10));
        }

        resourceHandler.setDirectoryListingEnabled(configuration.isDirectoryListingEnabled());
        routinrgHandler.add(Methods.GET, configuration.getResourcePath() + "*", BasicAuthenticationHttpHandler.addHandler(configuration, resourceHandler));
        
        return routinrgHandler;
    }
}
