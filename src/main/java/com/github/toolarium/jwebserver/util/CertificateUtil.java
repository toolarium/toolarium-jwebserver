/*
 * CertificateUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.util;

import com.github.toolarium.common.security.ISecuredValue;
import com.github.toolarium.common.security.SecuredValue;
import com.github.toolarium.common.util.ClassInstanceUtil;
import com.github.toolarium.security.certificate.CertificateUtilFactory;
import com.github.toolarium.security.certificate.dto.CertificateStore;
import com.github.toolarium.security.keystore.ISecurityManagerProvider;
import com.github.toolarium.security.keystore.impl.SecurityManagerProviderImpl;
import com.github.toolarium.security.keystore.util.KeyStoreUtil;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;


/**
 * Certificate util
 * 
 * @author patrick
 */
public final class CertificateUtil {

    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final CertificateUtil INSTANCE = new CertificateUtil();
    }

    
    /**
     * Constructor
     */
    private CertificateUtil() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static CertificateUtil getInstance() {
        return HOLDER.INSTANCE;
    }


    /**
     * Get the security manager provider either read by keystore or generated self-signed
     *
     * @param trustKeyStoreFilename the trust keystore filename or null to use default
     * @param keyStoreFilename the keystore filename or null to generate a self-signed certificate
     * @param keyStoreType the keystore type
     * @param keyStoreAlias the keystore alias
     * @param keyStorePassword the keystore password
     * @return the security manager provider
     * @throws IOException In case a file can't be accessed
     * @throws GeneralSecurityException In case a self-signbed certificate can't be created
     */
    public ISecurityManagerProvider getSecurityManagerProvider(String trustKeyStoreFilename, 
                                                               String keyStoreFilename, 
                                                               String keyStoreType,
                                                               String keyStoreAlias,
                                                               ISecuredValue<String> keyStorePassword) throws GeneralSecurityException, IOException {
        ISecurityManagerProvider securityManagerProvider = null;
        KeyStore trustManagerKeyStore = null;
        if (trustKeyStoreFilename != null && !trustKeyStoreFilename.isBlank()) {
            File trustKeyStoreFile = new File(trustKeyStoreFilename);
            if (trustKeyStoreFile.exists()) {
                trustManagerKeyStore = KeyStoreUtil.getInstance().readKeyStore(trustKeyStoreFile.getName(), keyStoreType, null /* getKeyStoreType() */, null /* password */);
            }
        }
        
        if (trustManagerKeyStore == null) {
            trustManagerKeyStore = KeyStoreUtil.getInstance().getDefaultTrustKeyStore();
        }
        
        if (keyStoreFilename != null && !keyStoreFilename.isBlank()) {
            File keyStoreFile = new File(keyStoreFilename);
            if (keyStoreFile.exists()) {
                final KeyStore keyManagerKeyStore = KeyStoreUtil.getInstance().readKeyStore(keyStoreFile.getPath(), keyStoreType, null, /* provider */ keyStorePassword);
                securityManagerProvider = new SecurityManagerProviderImpl(trustManagerKeyStore, keyManagerKeyStore, keyStorePassword);                        
            }
        }
        
        if (securityManagerProvider == null) {
            if (!ClassInstanceUtil.getInstance().isClassAvailable("org.bouncycastle.jce.provider.BouncyCastleProvider")) {
                throw new GeneralSecurityException();
            }
            
            // create self signed certificate
            ISecuredValue<String> pw = keyStorePassword;
            if (pw == null) {
                pw = new SecuredValue<String>("changeit");
            }
            
            String certificateStoreAlias = "toolarium";
            if (keyStoreAlias != null) {
                certificateStoreAlias = keyStoreAlias;
            }
            
            CertificateStore certificateStore = CertificateUtilFactory.getInstance().getGenerator().createCreateCertificate(certificateStoreAlias);
            final KeyStore keyManagerStore = certificateStore.toKeyStore(certificateStoreAlias, keyStorePassword.getValue());
            X509Certificate selfSignedCertificate = (X509Certificate)keyManagerStore.getCertificate(certificateStoreAlias);
            
            KeyStoreUtil.getInstance().addCertificateToKeystore(trustManagerKeyStore, certificateStoreAlias, new X509Certificate[] {selfSignedCertificate});
            securityManagerProvider = new SecurityManagerProviderImpl(trustManagerKeyStore, keyManagerStore, pw);
        }
        
        return securityManagerProvider;
    }

    
    /**
     * Get the SSL context
     *
     * @param securityManagerProvider the security manager provider
     * @param trustAnyCertificate true to trust any certificate
     * @return the ssl context
     * @throws GeneralSecurityException In case the context can't be initilaized 
     */
    public SSLContext createSSLContext(ISecurityManagerProvider securityManagerProvider, boolean trustAnyCertificate) throws GeneralSecurityException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        
        if (trustAnyCertificate) {
            sslContext.init(securityManagerProvider.getKeyManagers(), KeyStoreUtil.getInstance().getTrustAllCertificateManager(), SecureRandom.getInstanceStrong());
        } else { 
            sslContext.init(securityManagerProvider.getKeyManagers(), securityManagerProvider.getTrustManagers(), SecureRandom.getInstanceStrong());
        }
        
        return sslContext;
    }
}
