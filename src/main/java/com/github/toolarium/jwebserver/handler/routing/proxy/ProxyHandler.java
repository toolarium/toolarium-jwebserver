/*
 * ProxyHandler.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.routing.proxy;

import com.github.toolarium.jwebserver.config.IProxyServerConfiguration;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import io.undertow.UndertowOptions;
import io.undertow.protocols.ssl.UndertowXnioSsl;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import java.net.URI;
import java.net.URISyntaxException;
import javax.net.ssl.SSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.OptionMap;
import org.xnio.Xnio;


/**
 * Defines the proxy handler
 * @author patrick
 */
public final class ProxyHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyHandler.class);

    
    /**
     * Constructor for ProxyHandler
     */
    private ProxyHandler() {
        // NOP
    }

    
    /**
     * Get the proxy handler
     *
     * @param webServerConfiguration the configuration
     * @return the proxy handler
     */
    public static io.undertow.server.handlers.proxy.ProxyHandler createProxyHandler(final IWebServerConfiguration webServerConfiguration) {
        IProxyServerConfiguration configuration = webServerConfiguration.getProxyServerConfiguration();
        LoadBalancingProxyClient loadBalancer = new LoadBalancingProxyClient()
                .setConnectionsPerThread(configuration.getConnectionsPerThread()); // TODO: timeToLive .setTtl(timeToLive)

        String[] hostNames = configuration.getProxyHostNames();
        if (hostNames != null && hostNames.length > 0) {
            for (String hostName : hostNames) {
                if (hostName != null && !hostName.isBlank()) {
                    try {
                        URI uri = new URI(hostName);
                        SSLContext sslContext = null;
                        if (uri.getScheme() == null || uri.getScheme().isBlank()) {
                            LOG.warn("Could not found schema in uri [" + hostName + "]!");
                        } else if (uri.getScheme().endsWith("s")) {
                            try {
                                sslContext = webServerConfiguration.getSSLServerConfiguration().getSSLContext();
                            } catch (Exception e) {
                                loadBalancer.addHost(uri);
                                LOG.warn("Could not initialize the SSL contect: " + e.getMessage(), e);
                            } 
                        }
                        
                        if (sslContext != null) {
                            loadBalancer.addHost(uri, null, new UndertowXnioSsl(Xnio.getInstance(), OptionMap.EMPTY, sslContext), OptionMap.create(UndertowOptions.ENABLE_HTTP2, true));
                        } else {
                            loadBalancer.addHost(uri);
                        }
                    } catch (URISyntaxException e) {
                        LOG.warn("Could not parse uri: " + hostName);
                    }
                }
            }
        }
        
        return io.undertow.server.handlers.proxy.ProxyHandler.builder()
                .setProxyClient(loadBalancer)
                .setMaxRequestTime(configuration.getMaxRequestTime())
                .setReuseXForwarded(configuration.reuseXForwarded())
                .setRewriteHostHeader(configuration.rewriteHostHeader())
                //.addRequestHeader(Headers.SSL_CLIENT_CERT, "%{SSL_CLIENT_CERT}", ProxyHandler.class.getClassLoader()) // ExchangeAttributes.constant(
                //.addRequestHeader(Headers.SSL_CIPHER, "%{SSL_CIPHER}", ProxyHandler.class.getClassLoader())
                //.addRequestHeader(Headers.SSL_SESSION_ID, "%{SSL_SESSION_ID}", ProxyHandler.class.getClassLoader())
                .build();
    }
}
