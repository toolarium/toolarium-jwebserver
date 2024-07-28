/*
 * SSLServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import com.github.toolarium.common.security.ISecuredValue;
import com.github.toolarium.jwebserver.util.CertificateUtil;
import com.github.toolarium.security.keystore.ISecurityManagerProvider;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Objects;
import javax.net.ssl.SSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link ISSLServerConfiguration}.
 * 
 * @author patrick
 */
public class SSLServerConfiguration implements ISSLServerConfiguration, Serializable {
    private static final String END_VALUE = "].";
    
    private static final Logger LOG = LoggerFactory.getLogger(SSLServerConfiguration.class);
    private static final long serialVersionUID = -566842518507270602L;
    private Boolean trustAnyCertificate;
    private String trustKeyStoreFile;
    private String keyStoreFile;
    private String keyStoreAlias;
    private ISecuredValue<String> keyStorePassword;
    private String keysStoreType;
    private ISecurityManagerProvider securityManagerProvider;
    private SSLContext sslContext = null;
    
    
    /**
     * Constructor for SSLServerConfiguration
     */
    public SSLServerConfiguration() {
        trustAnyCertificate = false;
        trustKeyStoreFile = null;
        keyStoreFile = null;
        keyStoreAlias = null;
        keyStorePassword = null;
        keysStoreType = "PKCS12";
        securityManagerProvider = null;
        sslContext = null;
    }
    

    /**
     * @see com.github.toolarium.jwebserver.config.ISSLServerConfiguration#trustAnyCertificate()
     */
    @Override
    public Boolean trustAnyCertificate() {
        return trustAnyCertificate;
    }

    
    /**
     * Set the trust any certificate
     *
     * @param trustAnyCertificate the trust any certificate
     * @return this instance
     */
    public SSLServerConfiguration setTrustAnyCertificate(Boolean trustAnyCertificate) {
        if (trustAnyCertificate != null) {
            LOG.debug("Set trustAnyCertificate: [" + trustAnyCertificate + END_VALUE);            
            this.trustAnyCertificate = trustAnyCertificate;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.ISSLServerConfiguration#getTrustKeyStoreFile()
     */
    @Override
    public String getTrustKeyStoreFile() {
        return trustKeyStoreFile;
    }

    
    /**
     * Set the trust key store file
     *
     * @param trustKeyStoreFile the trust key store file
     * @return this instance
     */
    public SSLServerConfiguration setTrustKeyStoreFile(String trustKeyStoreFile) {
        if (trustKeyStoreFile != null) {
            LOG.debug("Set trustKeyStoreFile: [" + trustKeyStoreFile + END_VALUE);            
            this.trustKeyStoreFile = trustKeyStoreFile;
        }

        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.ISSLServerConfiguration#getKeyStoreFile()
     */
    @Override
    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    
    /**
     * Set the key store file
     *
     * @param keyStoreFile the key store file
     * @return this instance
     */
    public SSLServerConfiguration setKeyStoreFile(String keyStoreFile) {
        if (keyStoreFile != null) {
            LOG.debug("Set keyStoreFile: [" + keyStoreFile + END_VALUE);            
            this.keyStoreFile = keyStoreFile;
        }
        
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.ISSLServerConfiguration#getKeyStoreAlias()
     */
    @Override
    public String getKeyStoreAlias() {
        return keyStoreAlias;
    }

    
    /**
     * Set the key store alias
     *
     * @param keyStoreAlias the key store alias
     * @return this instance
     */
    public SSLServerConfiguration setKeyStoreAlias(String keyStoreAlias) {
        if (keyStoreAlias != null) {
            LOG.debug("Set keyStoreAlias: [" + keyStoreAlias + END_VALUE);            
            this.keyStoreAlias = keyStoreAlias;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.ISSLServerConfiguration#getKeyStorePassword()
     */
    @Override
    public ISecuredValue<String> getKeyStorePassword() {
        return keyStorePassword;
    }

    
    /**
     * Set the key store password
     *
     * @param keyStorePassword the key store password
     * @return this instance
     */
    public SSLServerConfiguration setKeyStorePassword(ISecuredValue<String> keyStorePassword) {
        if (keyStorePassword != null) {
            LOG.debug("Set keyStorePassword: [" + keyStorePassword + END_VALUE);            
            this.keyStorePassword = keyStorePassword;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.ISSLServerConfiguration#getKeyStoreType()
     */
    @Override
    public String getKeyStoreType() {
        return keysStoreType;
    }
    
    
    /**
     * Set the key store type
     *
     * @param keysStoreType the key store type
     * @return this instance
     */
    public SSLServerConfiguration setKeyStoreType(String keysStoreType) {
        if (keysStoreType != null) {
            LOG.debug("Set keysStoreType: [" + keysStoreType + END_VALUE);            
            this.keysStoreType = keysStoreType;
        }
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.ISSLServerConfiguration#getSSLContext()
     */
    @Override
    public SSLContext getSSLContext() throws GeneralSecurityException, IOException {
        if (sslContext == null) {
            sslContext = CertificateUtil.getInstance().createSSLContext(getSecurityManagerProvider(), trustAnyCertificate() != null && trustAnyCertificate.booleanValue());
        }
        
        return sslContext;
    }

    
    /**
     * Get the security manager provider
     *
     * @return the security manager provider
     * @throws IOException In case a file can't be accessed
     * @throws GeneralSecurityException In case a self-signbed certificate can't be created
     */
    public ISecurityManagerProvider getSecurityManagerProvider() throws GeneralSecurityException, IOException {
        if (securityManagerProvider == null) {
            securityManagerProvider = CertificateUtil.getInstance().getSecurityManagerProvider(getTrustKeyStoreFile(), 
                                                                                               getKeyStoreFile(), 
                                                                                               getKeyStoreType(), 
                                                                                               getKeyStoreAlias(), 
                                                                                               getKeyStorePassword());
        }            
        
        return securityManagerProvider;
    }


    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(keyStoreAlias, keyStoreFile, keyStorePassword, keysStoreType,  trustAnyCertificate, trustKeyStoreFile);
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
        
        SSLServerConfiguration other = (SSLServerConfiguration) obj;
        return Objects.equals(keyStoreAlias, other.keyStoreAlias) && Objects.equals(keyStoreFile, other.keyStoreFile)
                && Objects.equals(keyStorePassword, other.keyStorePassword)
                && Objects.equals(keysStoreType, other.keysStoreType)
                && Objects.equals(trustAnyCertificate, other.trustAnyCertificate)
                && Objects.equals(trustKeyStoreFile, other.trustKeyStoreFile);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SSLServerConfiguration [trustAnyCertificate=" + trustAnyCertificate + ", trustKeyStoreFile="
                + trustKeyStoreFile + ", keyStoreFile=" + keyStoreFile + ", keyStoreAlias=" + keyStoreAlias
                + ", keyStorePassword=" + keyStorePassword + ", keysStoreType=" + keysStoreType + "]";
    }
}
