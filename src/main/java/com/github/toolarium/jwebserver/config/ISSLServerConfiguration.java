/*
 * ISSLServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import com.github.toolarium.common.security.ISecuredValue;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.net.ssl.SSLContext;


/**
 * Defines the SSL server configuration
 * 
 * @author patrick
 */
public interface ISSLServerConfiguration {
    
    /**
     * Check if server trust any certificate
     *
     * @return true to trust any certificate
     */
    Boolean trustAnyCertificate();
    
    
    /**
     * Get the trust key store file or null to use default
     *
     * @return the trust key store file
     */
    String getTrustKeyStoreFile();
        
    
    /**
     * Get the key store file or null to use a self signed certificate
     *
     * @return the key store file
     */
    String getKeyStoreFile();

    
    /**
     * Get the key store alias
     * 
     * @return the key store alias
     */
    String getKeyStoreAlias();

    
    /**
     * Get the key store password
     * 
     * @return the key store password
     */
    ISecuredValue<String> getKeyStorePassword();
    
    
    /**
     * Get the key store type
     * 
     * @return the key store type
     */
    String getKeyStoreType();
    
    
    /**
     * Get the SSL context
     *
     * @return the ssl context or null
     * @throws IOException In case a file can't be accessed
     * @throws GeneralSecurityException In case a self-signbed certificate can't be created
     */
    SSLContext getSSLContext() throws GeneralSecurityException, IOException;
}
