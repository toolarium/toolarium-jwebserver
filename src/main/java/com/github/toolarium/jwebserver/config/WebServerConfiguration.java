/*
 * JWebServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import com.github.toolarium.common.security.ISecuredValue;
import com.github.toolarium.common.security.SecuredValue;
import com.github.toolarium.jwebserver.handler.routing.RoutingHandler;
import com.github.toolarium.jwebserver.logger.VerboseLevel;
import com.github.toolarium.jwebserver.util.ConfigurationUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Define the webserver configuration
 * 
 * @author patrick
 */
public class WebServerConfiguration implements IWebServerConfiguration {
    private static final String END_VALUE = "].";
    private static final String JWEBSERVER_PROPERTIES = "jwebserver.properties";
    private static final Logger LOG = LoggerFactory.getLogger(WebServerConfiguration.class);
    private String webserverName;
    private String hostname;
    private Integer port;
    private Integer securePort;
    private VerboseLevel verboseLevel;
    private String accessLogFormatString;
    private String accessLogFilePattern;
    private String basicAuthentication;
    private String healthPath;
    private int ioThreads;
    private int workerThreads;
    private String resourcePath;
    private SSLServerConfiguration sslServerConfiguration;
    private ResourceServerConfiguration resourceServerConfiguration;
    private ProxyServerConfiguration proxyServerConfiguration;

    
    /**
     * Constructor for WebServerConfiguration
     */
    public WebServerConfiguration() {
        this.webserverName = "";
        this.hostname = "0.0.0.0";
        this.port = null;
        this.securePort = null;
        this.verboseLevel = VerboseLevel.VERBOSE;
        this.accessLogFormatString = "combined";
        this.accessLogFilePattern = "logs/access-%d{yyyy-MM-dd}.log.gz"; // "logs/access-%d{yyyy-MM-dd}.%i.log.gz"
        this.basicAuthentication = null;
        this.healthPath = "/q/health";
        this.ioThreads = Math.max(Runtime.getRuntime().availableProcessors(), 2);
        this.workerThreads = ioThreads * 8;
        this.resourcePath = RoutingHandler.SLASH;
        this.sslServerConfiguration = new SSLServerConfiguration();
        this.resourceServerConfiguration = new ResourceServerConfiguration();
        this.proxyServerConfiguration = new ProxyServerConfiguration();
    }


    /**
     * Constructor for WebServerConfiguration
     * 
     * @param webServerConfiguration the web server configuration
     */
    public WebServerConfiguration(IWebServerConfiguration webServerConfiguration) {
        this.webserverName = webServerConfiguration.getWebserverName();
        this.hostname = webServerConfiguration.getHostname();
        this.port = webServerConfiguration.getPort();
        this.securePort = webServerConfiguration.getSecurePort();
        this.verboseLevel = webServerConfiguration.getVerboseLevel();
        this.accessLogFormatString = webServerConfiguration.getAccessLogFormatString();
        this.accessLogFilePattern = webServerConfiguration.getAccessLogFilePattern();
        this.basicAuthentication = webServerConfiguration.getBasicAuthentication();
        this.healthPath = webServerConfiguration.getHealthPath();
        this.ioThreads = webServerConfiguration.getIoThreads();
        this.workerThreads = webServerConfiguration.getWorkerThreads();
        this.resourcePath = webServerConfiguration.getResourcePath();
        this.sslServerConfiguration = webServerConfiguration.getSSLServerConfiguration();
        this.resourceServerConfiguration = new ResourceServerConfiguration(webServerConfiguration.getResourceServerConfiguration());
        this.proxyServerConfiguration = new ProxyServerConfiguration(webServerConfiguration.getProxyServerConfiguration());
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getWebserverName()
     */
    @Override
    public String getWebserverName() {
        return webserverName;
    }

    
    /**
     * Set the webserver name
     *
     * @param webserverName the webserve name
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setWebserverName(String webserverName) {
        if (webserverName != null && !webserverName.isBlank()) {
            LOG.debug("Set webserver name: [" + webserverName + "].  ");
            this.webserverName = webserverName.trim();
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getHostname()
     */
    @Override
    public String getHostname() {
        return hostname;
    }

    
    /**
     * Set the host name
     *
     * @param hostname the hostname
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setHostname(String hostname) {
        if (hostname != null && !hostname.isBlank()) {
            LOG.debug("Set hostname: [" + hostname + "].  ");
            this.hostname = hostname.trim();
        }
        
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getPort()
     */
    @Override
    public Integer getPort() {
        return port;
    }

    
    /**
     * Set the port
     *
     * @param port the port
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setPort(Integer port) {
        if (port != null) {
            LOG.debug("Set port: [" + port + "].  ");            
            this.port = port.intValue();
        }
        
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getSecurePort()
     */
    @Override
    public Integer getSecurePort() {
        return securePort;
    }

    
    /**
     * Set the secure port
     *
     * @param securePort the secure port
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setSecurePort(Integer securePort) {
        if (securePort != null) {
            LOG.debug("Set secure port: [" + securePort + "].  ");            
            this.securePort = securePort.intValue();
        }
        
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getVerboseLevel()
     */
    @Override
    public VerboseLevel getVerboseLevel() {
        return verboseLevel;
    }

    
    /**
     * Set the verbose level
     *
     * @param verboseLevel the verbose level
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setVerboseLevel(VerboseLevel verboseLevel) {
        if (verboseLevel != null) {
            LOG.debug("Set verboseLevel: [" + verboseLevel + END_VALUE);
            this.verboseLevel = verboseLevel;
        }
        
        return this;
    }

        
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getAccessLogFormatString()
     */
    @Override
    public String getAccessLogFormatString() {
        return accessLogFormatString;
    }

    
    /**
     * Set the access log format string
     *
     * @param accessLogFormatString the access log format string
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setAccessLogFormatString(String accessLogFormatString) {
        if (accessLogFormatString != null && !accessLogFormatString.isBlank()) {
            LOG.debug("Set accessLogFormatString: [" + accessLogFormatString + END_VALUE);
            this.accessLogFormatString = accessLogFormatString;
        }
        
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getAccessLogFilePattern()
     */
    @Override
    public String getAccessLogFilePattern() {
        return accessLogFilePattern;
    }

    
    /**
     * Set the access log file pattern
     *
     * @param accessLogFilePattern the access log file pattern
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setAccessLogFilePattern(String accessLogFilePattern) {
        if (accessLogFilePattern != null && !accessLogFilePattern.isBlank()) {
            LOG.debug("Set accessLogFilePattern: [" + accessLogFilePattern + END_VALUE);
            this.accessLogFilePattern = accessLogFilePattern;
        }
        
        return this;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#hasBasicAuthentication()
     */
    @Override
    public boolean hasBasicAuthentication() {
        return basicAuthentication != null && !basicAuthentication.isBlank(); 
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getBasicAuthentication()
     */
    @Override
    public String getBasicAuthentication() {
        return basicAuthentication;
    }
    
    
    /**
     * Define if basic authentication is enabled
     *
     * @param basicAuthentication true if basic authentication is enabled
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setBasicAuthentication(String basicAuthentication) {
        if (basicAuthentication == null || basicAuthentication.isBlank()) {
            LOG.debug("Disable basicAuthentication.");
        } else { 
            LOG.debug("Enable basicAuthentication.");
        }
            
        this.basicAuthentication = basicAuthentication;
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#hasHealthCheck()
     */
    @Override
    public boolean hasHealthCheck() {
        return healthPath != null && !healthPath.isBlank(); 
    }
    

    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getHealthPath()
     */
    @Override
    public String getHealthPath() {
        return healthPath;
    }

    
    /**
     * Set the health path 
     *
     * @param healthPath the resource path
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setHealthPath(String healthPath) {
        if (healthPath == null || healthPath.isBlank()) {
            LOG.debug("Disable health check.");
        } else { 
            LOG.debug("Enable health check: [" + healthPath + END_VALUE);
        }
            
        this.healthPath = healthPath;
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getIoThreads()
     */
    @Override
    public int getIoThreads() {
        return ioThreads;
        
    }

    
    
    /**
     * Set the I/O threads
     *
     * @param ioThreads the io threads
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setIoThreads(Integer ioThreads) {
        if (ioThreads != null && ioThreads.intValue() > 0) {
            LOG.debug("Set ioThreads: [" + ioThreads + END_VALUE);            
            this.ioThreads = ioThreads;
            setWorkerThreads(ioThreads * 8);
        }
        return this;
    }


    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getWorkerThreads()
     */
    @Override
    public int getWorkerThreads() {
        return workerThreads;
    }

    
    /**
     * Set the worker threads
     *
     * @param workerThreads the worker threads
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setWorkerThreads(Integer workerThreads) {
        if (workerThreads != null && workerThreads.intValue() > 0) {
            LOG.debug("Set workerThreads: [" + workerThreads + END_VALUE);            
            this.workerThreads = workerThreads;
        }
        return this;
    }
    

    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getResourcePath()
     */
    @Override
    public String getResourcePath() {
        return resourcePath;
    }

    
    /**
     * Set the resource path 
     *
     * @param resourcePath the resource path
     * @return the ResourceServerConfiguration
     */
    public WebServerConfiguration setResourcePath(String resourcePath) {
        if (resourcePath != null && !resourcePath.isBlank()) {
            LOG.debug("Set resourcePath: [" + resourcePath + END_VALUE);            
            this.resourcePath = resourcePath;
            
            if (resourcePath.endsWith(RoutingHandler.SLASH)) {
                this.resourcePath = resourcePath.substring(0, resourcePath.length() - 1);
            }
        }
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getSSLServerConfiguration()
     */
    @Override
    public SSLServerConfiguration getSSLServerConfiguration() {
        return sslServerConfiguration;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getResourceServerConfiguration()
     */
    @Override
    public ResourceServerConfiguration getResourceServerConfiguration() {
        return resourceServerConfiguration;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getProxyServerConfiguration()
     */
    @Override
    public ProxyServerConfiguration getProxyServerConfiguration() {
        return proxyServerConfiguration;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#isProxyServer()
     */
    @Override
    public boolean isProxyServer() {
        return proxyServerConfiguration != null && proxyServerConfiguration.getProxyHostNames() != null && proxyServerConfiguration.getProxyHostNames().length > 0 && !proxyServerConfiguration.getProxyHostNames()[0].isBlank();
    }

    
    /**
     * Read the configuration from the classpath
     * 
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration readProperties() {
        Properties properties = readPropertiesFromClasspath();
        if (properties == null) {
            return this;
        }
        
        setWebserverName(readProperty(properties, "webserverName", webserverName, false));
        setHostname(readProperty(properties, "hostname", hostname, false));
        setPort(readProperty(properties, "port", port, true));
        setSecurePort(readProperty(properties, "securePort", securePort, true));

        setIoThreads(readProperty(properties, "ioThreads", ioThreads, false));
        setWorkerThreads(readProperty(properties, "workerThreads", workerThreads, false));
        
        setVerboseLevel(readProperty(properties, "verboseLevel", verboseLevel, false));
        setAccessLogFormatString(readProperty(properties, "accessLogFormatString", accessLogFormatString, false));
        setAccessLogFilePattern(readProperty(properties, "accessLogFilePattern", accessLogFilePattern, false));
        
        setBasicAuthentication(readProperty(properties, "basicAuthentication", basicAuthentication, true));
        setHealthPath(readProperty(properties, "healthPath", healthPath, true));
        setResourcePath(readProperty(properties, "resourcePath", getResourcePath(), false));

        sslServerConfiguration.setTrustAnyCertificate(readProperty(properties, "trustAnyCertificate", sslServerConfiguration.trustAnyCertificate(), true));
        sslServerConfiguration.setTrustKeyStoreFile(readProperty(properties, "trustKeyStoreFile", sslServerConfiguration.getTrustKeyStoreFile(), true));
        sslServerConfiguration.setKeyStoreFile(readProperty(properties, "keyStoreFile", sslServerConfiguration.getKeyStoreFile(), true));
        sslServerConfiguration.setKeyStoreAlias(readProperty(properties, "keyStoreAlias", sslServerConfiguration.getKeyStoreAlias(), true));
        sslServerConfiguration.setKeyStorePassword(readProperty(properties, "keyStorePassword", sslServerConfiguration.getKeyStorePassword(), true));
        sslServerConfiguration.setKeyStoreType(readProperty(properties, "keysStoreType", sslServerConfiguration.getKeyStoreType(), true));

        resourceServerConfiguration.setDirectory(readProperty(properties, "directory", resourceServerConfiguration.getDirectory(), false), readProperty(properties, "readFromClasspath", resourceServerConfiguration.readFromClasspath(), false));
        if (!resourceServerConfiguration.readFromClasspath()) {
            resourceServerConfiguration.setDirectoryListingEnabled(readProperty(properties, "directoryListingEnabled", resourceServerConfiguration.isDirectoryListingEnabled(), false));
        } else {
            resourceServerConfiguration.setDirectoryListingEnabled(Boolean.FALSE);
        }
        resourceServerConfiguration.setWelcomeFiles(readProperty(properties, "welcomeFiles", ConfigurationUtil.getInstance().formatArrayAsString(resourceServerConfiguration.getWelcomeFiles()), false));
        resourceServerConfiguration.setSupportedFileExtensions(readProperty(properties, "supportedFileExtensions", ConfigurationUtil.getInstance().formatArrayAsString(resourceServerConfiguration.getSupportedFileExtensions()), false));
        
        proxyServerConfiguration.setRewriteHostHeader(readProperty(properties, "rewriteHostHeader", proxyServerConfiguration.rewriteHostHeader(), true));
        proxyServerConfiguration.setReuseXForwarded(readProperty(properties, "reuseXForwarded", proxyServerConfiguration.reuseXForwarded(), true));
        proxyServerConfiguration.setMaxRequestTime(readProperty(properties, "maxRequestTime", proxyServerConfiguration.getMaxRequestTime(), true));
        proxyServerConfiguration.setConnectionsPerThread(readProperty(properties, "connectionsPerThread", proxyServerConfiguration.getConnectionsPerThread(), true));
        proxyServerConfiguration.setProxyHostNames(readProperty(properties, "proxy", ConfigurationUtil.getInstance().formatArrayAsString(proxyServerConfiguration.getProxyHostNames()), true));
        return this;
    }

    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WebServerConfiguration [webserverName=" + webserverName + ", hostname=" + hostname + ", port=" + port + ", securePort=" + securePort
                + ", verboseLevel=" + verboseLevel + ", accessLogFormatString=" + accessLogFormatString
                + ", accessLogFilePattern=" + accessLogFilePattern + ", basicAuthentication=" + basicAuthentication
                + ", healthPath=" + healthPath + ", ioThreads=" + ioThreads + ", workerThreads=" + workerThreads
                + ", resourcePath=" + resourcePath + ", resourceServerConfiguration=" + resourceServerConfiguration
                + ", proxyServerConfiguration=" + proxyServerConfiguration + "]";
    }


    /**
     * Read properties from classpath
     *
     * @return the read properties
     */
    private Properties readPropertiesFromClasspath() {
        Properties properties = null;
        
        try {
            try (InputStream stream = this.getClass().getResourceAsStream(RoutingHandler.SLASH + JWEBSERVER_PROPERTIES)) {
                int countEntries = 0;
                if (stream != null) {
                    LOG.debug("Found " + JWEBSERVER_PROPERTIES + "...");
                    properties = new Properties();
                    String line;
                    InputStreamReader inputStreamReader = new InputStreamReader(stream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    while ((line = bufferedReader.readLine()) != null) {
                        if (!line.isBlank() && !line.trim().startsWith("#")) {
                            String c = line.trim();
                            int idx = c.indexOf('=');
                            if (idx > 0) {
                                String key = c.substring(0, idx).trim();
                                String value = c.substring(idx + 1).trim();
                                properties.setProperty(key, value);
                                countEntries++;
                            }
                        }
                    }
                }
                
                if (countEntries == 0) {
                    properties = null;
                }
            }
        } catch (IOException ex) { 
            LOG.info("Could not read and parse confuguration jwebserver.properties from classpath.");
        }
        
        return properties;
    }


    /**
     * Read boolean property
     *
     * @param properties the properties
     * @param name the name
     * @param defaultValue the default value
     * @param allowEmptyValue true to allow empty values otherwise in case of an empty value the default value will be taken
     * @return the result
     */
    private String readProperty(Properties properties, String name, String defaultValue, boolean allowEmptyValue) {
        String result = properties.getProperty(name, defaultValue);
        
        if (result == null || result.isBlank()) {
            if (allowEmptyValue) {
                LOG.debug("Assign property [" + name + "] = [" + result + "] from " + JWEBSERVER_PROPERTIES + ".");
                return result;
            } else {
                LOG.debug("Assign default property [" + name + "] = [" + defaultValue + "] from " + JWEBSERVER_PROPERTIES + ".");
                return defaultValue;
            }
        } else {
            if (!result.equals(defaultValue)) {
                LOG.debug("Assign property [" + name + "] = [" + result + "] from " + JWEBSERVER_PROPERTIES + ".");
            }
        }
        
        return result;
    }

    
    /**
     * Read boolean property
     *
     * @param properties the properties
     * @param name the name
     * @param defaultValue the default value
     * @param allowEmptyValue true to allow empty values otherwise in case of an empty value the default value will be taken
     * @return the result
     */
    private Boolean readProperty(Properties properties, String name, Boolean defaultValue, boolean allowEmptyValue) {
        String result = readProperty(properties, name, "" + defaultValue, allowEmptyValue);
        if ((result == null || result.isBlank())) {
            if (allowEmptyValue) {
                return null;
            } else {
                return defaultValue;
            }
        }
        
        try {
            return Boolean.valueOf(result);
        } catch (Exception e) {
            LOG.warn("Invalid value [" + result + "] for attribute [" + name + "], keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
    }

    
    /**
     * Read boolean property
     *
     * @param properties the properties
     * @param name the name
     * @param defaultValue the default value
     * @param allowEmptyValue true to allow empty values otherwise in case of an empty value the default value will be taken
     * @return the result
     */
    private Integer readProperty(Properties properties, String name, Integer defaultValue, boolean allowEmptyValue) {
        String result = readProperty(properties, name, "" + defaultValue, allowEmptyValue);
        if ((result == null || result.isBlank())) {
            if (allowEmptyValue) {
                return null;
            } else {
                return defaultValue;
            }
        }
        
        try {
            return Integer.valueOf(result);
        } catch (Exception e) {
            LOG.warn("Invalid value [" + result + "] for attribute [" + name + "], keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
    }


    /**
     * Read boolean property
     *
     * @param properties the properties
     * @param name the name
     * @param defaultValue the default value
     * @param allowEmptyValue true to allow empty values otherwise in case of an empty value the default value will be taken
     * @return the result
     */
    private VerboseLevel readProperty(Properties properties, String name, VerboseLevel defaultValue, boolean allowEmptyValue) {
        String result = readProperty(properties, name, "" + defaultValue, allowEmptyValue);
        if ((result == null || result.isBlank())) {
            if (allowEmptyValue) {
                return null;
            } else {
                return defaultValue;
            }
        }
        
        try {
            return VerboseLevel.valueOf(result);
        } catch (Exception e) {
            LOG.warn("Invalid value [" + result + "] for attribute [" + name + "], keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
    }


    /**
     * Read secured value property
     *
     * @param properties the properties
     * @param name the name
     * @param defaultValue the default value
     * @param allowEmptyValue true to allow empty values otherwise in case of an empty value the default value will be taken
     * @return the result
     */
    private ISecuredValue<String> readProperty(Properties properties, String name, ISecuredValue<String> defaultValue, boolean allowEmptyValue) {
        String result = readProperty(properties, name, "" + defaultValue, allowEmptyValue);
        if ((result == null || result.isBlank())) {
            if (allowEmptyValue) {
                return null;
            } else {
                return defaultValue;
            }
        }
        
        try {
            return new SecuredValue<String>(result);
        } catch (Exception e) {
            LOG.warn("Invalid value [" + result + "] for attribute [" + name + "], keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
    }
}
