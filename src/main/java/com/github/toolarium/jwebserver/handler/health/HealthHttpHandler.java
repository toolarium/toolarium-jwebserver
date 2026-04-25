/*
 * HealthHttpHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.health;

import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.handler.auth.BasicAuthenticationHttpHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;



/**
 * Implements the health handler
 *  
 * @author patrick
 */
public final class HealthHttpHandler implements HttpHandler {
    
    
    /**
     * @see io.undertow.server.HttpHandler#handleRequest(io.undertow.server.HttpServerExchange)
     */
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange.getResponseSender().send("{ \"status\": \"UP\" }");

        /*
        {
            "status": "UP",
            "checks": [
                {
                    "name": "I'm alive",
                    "status": "UP"
                }
            ]
        }
        */
    }


    /**
     * Add basic authentication
     *
     * @param webServerConfiguration the web server configuration
     * @param routingHandler the routing handler
     * @return the handler
     */
    public static RoutingHandler addHandler(final IWebServerConfiguration webServerConfiguration, RoutingHandler routingHandler) {
        if (webServerConfiguration.hasHealthCheck()) {
            HttpHandler healthHandler = BasicAuthenticationHttpHandler.addHandler(webServerConfiguration, new HealthHttpHandler());
            routingHandler.get(webServerConfiguration.getHealthPath(), healthHandler);
        }
        return routingHandler;
    }
}
