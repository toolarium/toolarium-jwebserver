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
            io.undertow.server.handlers.proxy.ProxyHandler proxyHandler = ProxyHandler.createProxyHandler(webServerConfiguration);
            routinrgHandler.add(Methods.GET, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler));
            routinrgHandler.add(Methods.PUT, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler));
            routinrgHandler.add(Methods.PATCH, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler));
            routinrgHandler.add(Methods.POST, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler));
            routinrgHandler.add(Methods.DELETE, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler));
            routinrgHandler.add(Methods.HEAD, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler));
            routinrgHandler.add(Methods.OPTIONS, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, proxyHandler));
        } else {
            io.undertow.server.handlers.resource.ResourceHandler resourceHandler = ResourceHandler.createResourceHandler(webServerConfiguration);
            routinrgHandler.add(Methods.GET, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, resourceHandler));
            routinrgHandler.add(Methods.HEAD, resourcePath + STAR, BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, resourceHandler));
        }

        //routinrgHandler.setFallbackHandler(new RedirectHandler(resourcePath));
        return routinrgHandler;
    }
}
