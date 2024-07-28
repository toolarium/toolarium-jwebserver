/*
 * ProxyServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import com.github.toolarium.jwebserver.util.ConfigurationUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IProxyServerConfiguration}.
 * 
 * @author patrick
 */
public class ProxyServerConfiguration implements IProxyServerConfiguration, Serializable {
    private static final String END_VALUE = "].";
    
    private static final Logger LOG = LoggerFactory.getLogger(ProxyServerConfiguration.class);
    private static final long serialVersionUID = -6364554233103894914L;
    private boolean rewriteHostHeader;
    private boolean reuseXForwarded;
    private int maxRequestTime;
    private int connectionsPerThread;
    private String[] proxyHostNameList;
    

    /**
     * Constructor for ProxyServerConfiguration
     */
    public ProxyServerConfiguration() {
        this.rewriteHostHeader = true;
        this.reuseXForwarded = true;
        this.maxRequestTime = 30000;
        this.connectionsPerThread = 20;
        this.proxyHostNameList = null;
    }


    /**
     * Constructor for ProxyServerConfiguration
     *
     * @param proxyServerConfiguration the proxy server configuration
     */
    public ProxyServerConfiguration(IProxyServerConfiguration proxyServerConfiguration) {
        this.rewriteHostHeader = proxyServerConfiguration.rewriteHostHeader();
        this.reuseXForwarded = proxyServerConfiguration.reuseXForwarded();
        this.maxRequestTime = proxyServerConfiguration.getMaxRequestTime();
        this.connectionsPerThread = proxyServerConfiguration.getConnectionsPerThread();
        this.proxyHostNameList = proxyServerConfiguration.getProxyHostNames();
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IProxyServerConfiguration#rewriteHostHeader()
     */
    @Override
    public boolean rewriteHostHeader() {
        return rewriteHostHeader;
    }


    /**
     * Set the rewrite host header
     *
     * @param rewriteHostHeader the rewrite host header
     * @return this instance
     */
    public ProxyServerConfiguration setRewriteHostHeader(Boolean rewriteHostHeader) {
        if (rewriteHostHeader != null) {
            LOG.debug("Set rewriteHostHeader: [" + rewriteHostHeader + END_VALUE);            
            this.rewriteHostHeader = rewriteHostHeader;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IProxyServerConfiguration#reuseXForwarded()
     */
    @Override
    public boolean reuseXForwarded() {
        return reuseXForwarded;
    }

    
    /**
     * Set the reuse X forwarded
     *
     * @param reuseXForwarded the reuse X forwarded
     * @return this instance
     */
    public ProxyServerConfiguration setReuseXForwarded(Boolean reuseXForwarded) {
        if (reuseXForwarded != null) {
            LOG.debug("Set reuseXForwarded: [" + reuseXForwarded + END_VALUE);            
            this.reuseXForwarded = reuseXForwarded;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IProxyServerConfiguration#getMaxRequestTime()
     */
    @Override
    public int getMaxRequestTime() {
        return maxRequestTime;
    }

    
    /**
     * Set the max request time
     *
     * @param maxRequestTime the max request time
     * @return this instance
     */
    public ProxyServerConfiguration setMaxRequestTime(Integer maxRequestTime) {
        if (maxRequestTime != null) {
            LOG.debug("Set maxRequestTime: [" + reuseXForwarded + END_VALUE);            
            this.maxRequestTime = maxRequestTime;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IProxyServerConfiguration#getConnectionsPerThread()
     */
    @Override
    public int getConnectionsPerThread() {
        return connectionsPerThread;
    }

    
    /**
     * Set the connections per thread
     *
     * @param connectionsPerThread the connections per thread
     * @return this instance
     */
    public ProxyServerConfiguration setConnectionsPerThread(Integer connectionsPerThread) {
        if (connectionsPerThread != null) {
            LOG.debug("Set connectionsPerThread: [" + connectionsPerThread + END_VALUE);            
            this.connectionsPerThread = connectionsPerThread;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IProxyServerConfiguration#getProxyHostNames()
     */
    @Override
    public String[] getProxyHostNames() {
        return proxyHostNameList;
    }

    
    /**
     * Set the proxy host names
     *
     * @param proxyHostNameList the proxy host names
     * @return this instance
     */
    public ProxyServerConfiguration setProxyHostNames(String proxyHostNameList) {
        if (proxyHostNameList != null) {
            setProxyHostNames(ConfigurationUtil.getInstance().parseStringArray(proxyHostNameList));
        }
        return this;
    }

    
    /**
     * Set the proxy host names
     *
     * @param proxyHostNameList the proxy host names
     * @return this instance
     */
    public ProxyServerConfiguration setProxyHostNames(String[] proxyHostNameList) {
        if (proxyHostNameList != null) {
            this.proxyHostNameList = proxyHostNameList;
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Set proxyHostNameList: [" + ConfigurationUtil.getInstance().formatArrayAsString(this.proxyHostNameList) + END_VALUE);            
            }
        }
        return this;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(proxyHostNameList);
        result = prime * result + Objects.hash(connectionsPerThread, maxRequestTime, reuseXForwarded, rewriteHostHeader);
        return result;
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        ProxyServerConfiguration other = (ProxyServerConfiguration) obj;
        return connectionsPerThread == other.connectionsPerThread && maxRequestTime == other.maxRequestTime
                && Arrays.equals(proxyHostNameList, other.proxyHostNameList) && reuseXForwarded == other.reuseXForwarded
                && rewriteHostHeader == other.rewriteHostHeader;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ProxyServerConfiguration [rewriteHostHeader=" + rewriteHostHeader + ", reuseXForwarded="
                + reuseXForwarded + ", maxRequestTime=" + maxRequestTime + ", connectionsPerThread="
                + connectionsPerThread + ", proxyHostNameList=" + Arrays.toString(proxyHostNameList) + "]";
    }
}
