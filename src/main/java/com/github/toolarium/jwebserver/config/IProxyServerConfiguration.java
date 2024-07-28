/*
 * IProxyServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;


/**
 * Defines the proxy server configuration
 *  
 * @author patrick
 */
public interface IProxyServerConfiguration {
    
    /**
     * Define if the HOST header should be rewritten to use the target host of the call.
     *
     * @return true to reqrite the host header
     */
    boolean rewriteHostHeader();
    
    
    /**
     * Define if any existing X-Forwarded-For header should be used or should it be overwritten.
     *
     * @return true to reuse
     */
    boolean reuseXForwarded();
    
    
    /**
     * Ger the max request time 
     *
     * @return the max request time
     */
    int getMaxRequestTime();
    
    
    /**
     * Get the connections per thread
     *
     * @return the connections per thread
     */
    int getConnectionsPerThread();
    
    
    /**
     * Get the proxy host names
     * 
     * @return the proxy host names
     */
    String[] getProxyHostNames();
}
