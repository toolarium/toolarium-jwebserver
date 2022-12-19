/*
 * BasicAuthenticationHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.auth;

import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import io.undertow.server.HttpHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * Basic authentication utility
 * 
 * @author patrick
 */
public final class BasicAuthenticationHttpHandler {
    
    /**
     * Constructor
     */
    private BasicAuthenticationHttpHandler() {
        // NOP
    }

    
    /**
     * Add basic authentication
     *
     * @param configuration the configuration
     * @param handlerToWrap the handler to wrap
     * @return the handler
     */
    public static HttpHandler addHandler(final IWebServerConfiguration configuration, final HttpHandler handlerToWrap) {
        final Map<String, char[]> users = new HashMap<>(1);
        String basicAuth = configuration.getBasicAuthentication();
        if (configuration.hasBasicAuthentication() && basicAuth != null && !basicAuth.isBlank()) {
            int idx = basicAuth.indexOf(':');
            if (idx > 0) {
                users.put(basicAuth.substring(0, idx).trim(), basicAuth.substring(idx + 1).trim().toCharArray());
            }
        }
        
        return addHandler(configuration, handlerToWrap, "", new MemoryIdentityManager(users));
    }

    
    /**
     * Add basic authentication
     *
     * @param configuration the configuration
     * @param handlerToWrap the handler to wrap
     * @param realmName the realm name
     * @param identityManager the idendity provider
     * @return the handler
     */
    public static HttpHandler addHandler(final IWebServerConfiguration configuration,
                                         final HttpHandler handlerToWrap, 
                                         final String realmName,
                                         final IdentityManager identityManager) {
        HttpHandler handler = handlerToWrap;
        
        String basicAuth = configuration.getBasicAuthentication();
        if (configuration.hasBasicAuthentication() && basicAuth != null && !basicAuth.isBlank()) {
            handler = new AuthenticationCallHandler(handler);
            handler = new AuthenticationConstraintHandler(handler);
            final List<AuthenticationMechanism> mechanisms = Collections.<AuthenticationMechanism>singletonList(new BasicAuthenticationMechanism(realmName));
            handler = new AuthenticationMechanismsHandler(handler, mechanisms);
            handler = new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE, identityManager, handler);
        }
        
        return handler;
    }
}
