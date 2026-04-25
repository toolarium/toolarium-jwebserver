/*
 * ResourceHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.routing;

import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.handler.auth.BasicAuthenticationHttpHandler;
import com.github.toolarium.jwebserver.handler.routing.proxy.ProxyHandler;
import com.github.toolarium.jwebserver.handler.routing.resource.ResourceHandler;
import io.undertow.util.Methods;


/**
* Basic authentication utility
* 
* @author patrick
*/
public final class RoutingHandler {
    /** SLASH */
    public static final String SLASH = "/";
    private static final String STAR = "*";


    /**
     * Constructor for ResourceHandler
     */
    private RoutingHandler() {
        // NOP
    }

    
    /**
     * Add handler
     *
     * @param webServerConfiguration the web server configuration
     * @param routinrgHandler the routing handler
     * @return the handler
     */
    public static io.undertow.server.RoutingHandler addHandler(final IWebServerConfiguration webServerConfiguration, final io.undertow.server.RoutingHandler routinrgHandler) { 
        String resourcePath = webServerConfiguration.getResourcePath();
        if (resourcePath == null || resourcePath.isBlank()) {
            resourcePath = SLASH;
        }

        if (webServerConfiguration.isProxyServer()) {
            // proxy mode: all HTTP methods are forwarded to the backend (wrapped with a single auth handler)
            io.undertow.server.handlers.proxy.ProxyHandler proxyHandler = ProxyHandler.createProxyHandler(webServerConfiguration);
            io.undertow.server.HttpHandler authWrappedProxy = BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler);
            String proxyPath = resourcePath + STAR;
            routinrgHandler.add(Methods.GET, proxyPath, authWrappedProxy);
            routinrgHandler.add(Methods.PUT, proxyPath, authWrappedProxy);
            routinrgHandler.add(Methods.PATCH, proxyPath, authWrappedProxy);
            routinrgHandler.add(Methods.POST, proxyPath, authWrappedProxy);
            routinrgHandler.add(Methods.DELETE, proxyPath, authWrappedProxy);
            routinrgHandler.add(Methods.HEAD, proxyPath, authWrappedProxy);
            routinrgHandler.add(Methods.OPTIONS, proxyPath, authWrappedProxy);
        } else {
            // file server mode: only read-only methods
            io.undertow.server.handlers.resource.ResourceHandler resourceHandler = ResourceHandler.createResourceHandler(webServerConfiguration);
            io.undertow.server.HttpHandler authWrappedResource = BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, resourceHandler);
            String filePath = resourcePath + STAR;
            routinrgHandler.add(Methods.GET, filePath, authWrappedResource);
            routinrgHandler.add(Methods.HEAD, filePath, authWrappedResource);
        }

        //routinrgHandler.setFallbackHandler(new RedirectHandler(resourcePath));
        return routinrgHandler;
    }
}
