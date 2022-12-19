/*
 * JWebServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import com.github.toolarium.jwebserver.logger.access.VerboseLevel;
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
    private static final String SLASH = "/";
    private static final String JWEBSERVER_PROPERTIES = "jwebserver.properties";
    private static final Logger LOG = LoggerFactory.getLogger(WebServerConfiguration.class);
    private static final String USER_HOME = System.getProperty("user.home");
    private String hostname;
    private int port;
    private String directory;
    private boolean isLocalDirectory;
    private boolean readFromClasspath;
    private boolean directoryListingEnabled;
    private VerboseLevel verboseLevel;
    private String accessLogFormatString;
    private String basicAuthentication;
    private String healthPath;
    private String resourcePath;
    
    /**
     * Constructor for WebServerConfiguration
     */
    public WebServerConfiguration() {
        this.hostname = "localhost";
        this.port = 8080;
        this.directory = ".";
        this.isLocalDirectory = true;
        this.readFromClasspath = false;
        this.directoryListingEnabled = false;
        this.verboseLevel = VerboseLevel.INFO;
        this.accessLogFormatString = "combined";
        this.basicAuthentication = null;
        this.healthPath = "/q/health";
        this.resourcePath = SLASH;
    }


    /**
     * Constructor for WebServerConfiguration
     * 
     * @param configuration the configuration
     */
    public WebServerConfiguration(IWebServerConfiguration configuration) {
        this.hostname = configuration.getHostname();
        this.port = configuration.getPort();
        this.directory = configuration.getDirectory();
        this.isLocalDirectory = configuration.isLocalDirectory();
        this.readFromClasspath = configuration.readFromClasspath();
        this.directoryListingEnabled = configuration.isDirectoryListingEnabled();
        this.verboseLevel = configuration.getVerboseLevel();
        this.accessLogFormatString = configuration.getAccessLogFormatString();
        this.basicAuthentication = configuration.getBasicAuthentication();
        this.healthPath = configuration.getHealthPath();
        this.resourcePath = configuration.getResourcePath();
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
    public int getPort() {
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
            LOG.debug("Set port: [" + port + "]. ");            
            this.port = port.intValue();
        }
        
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#getDirectory()
     */
    @Override
    public String getDirectory() {
        return directory;
    }

    
    /**
     * Set the directory
     *
     * @param directory the directory
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setDirectory(String directory) {
        if (directory != null) {
            LOG.debug("Assign property [directory] = [" + directory + "] from CLI.");
            setDirectory(directory, Boolean.FALSE);
        }
        
        return this;
    }

    
    /**
     * Set the directory
     *
     * @param directory the directory
     * @param readFromClasspath the directory should be read from the classpath
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setDirectory(String directory, Boolean readFromClasspath) {
        LOG.debug("Set directory: [" + directory + "], readFromClasspath: [" + readFromClasspath + "]. ");
        
        if (directory != null) {
            this.directory = directory.trim();

            if ("%HOME%".equals(directory) || "$HOME".equals(directory)) {
                this.directory = USER_HOME;
            }
        }
        
        if (readFromClasspath != null) {
            this.readFromClasspath = readFromClasspath.booleanValue();
        }

        LOG.debug("Set isLocalDirectory: " + this.directory.startsWith(SLASH) + SLASH + this.directory.startsWith("\\") + SLASH + (this.directory.length() > 2 && this.directory.substring(1).startsWith(":")));
        this.isLocalDirectory = !(this.readFromClasspath 
                || this.directory.startsWith(SLASH) 
                || this.directory.startsWith("\\") 
                || (this.directory.length() > 2 && this.directory.substring(1).startsWith(":")));

        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#readFromClasspath()
     */
    @Override
    public boolean readFromClasspath() {
        return readFromClasspath;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#isLocalDirectory()
     */
    @Override
    public boolean isLocalDirectory() {
        return isLocalDirectory;
    }

    
    
    /**
     * @see com.github.toolarium.jwebserver.config.IWebServerConfiguration#isDirectoryListingEnabled()
     */
    @Override
    public boolean isDirectoryListingEnabled() {
        return directoryListingEnabled;
    }


    /**
     * Define if the directory listing is enabled  
     *
     * @param directoryListingEnabled turn true if it is enabled
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setDirectoryListingEnabled(Boolean directoryListingEnabled) {
        if (directoryListingEnabled != null) {
            LOG.debug("Set directoryListingEnabled: [" + directoryListingEnabled + "]. ");
            this.directoryListingEnabled = directoryListingEnabled.booleanValue();
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
            LOG.debug("Set verboseLevel: [" + verboseLevel + "]. ");
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
        if (accessLogFormatString != null) {
            LOG.debug("Set accessLogFormatString: [" + accessLogFormatString + "]. ");
            this.accessLogFormatString = accessLogFormatString;
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
            LOG.debug("Enable health check: [" + healthPath + "].");
        }
            
        this.healthPath = healthPath;
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
     * @return the WebServerConfiguration
     */
    public WebServerConfiguration setResourcePath(String resourcePath) {
        if (resourcePath != null && !resourcePath.isBlank()) {
            LOG.debug("Set resourcePath: [" + resourcePath + "].");            
            this.resourcePath = resourcePath;
        }
        return this;
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
        
        setHostname(readProperty(properties, "hostname", hostname, false));
        setPort(readProperty(properties, "port", port, false));
        setDirectory(readProperty(properties, "directory", directory, false), readProperty(properties, "readFromClasspath", readFromClasspath, false));
        if (!readFromClasspath()) {
            setDirectoryListingEnabled(readProperty(properties, "directoryListingEnabled", directoryListingEnabled, false));
        } else {
            setDirectoryListingEnabled(Boolean.FALSE);
        }

        setVerboseLevel(readProperty(properties, "verboseLevel", verboseLevel, false));
        setAccessLogFormatString(readProperty(properties, "accessLogFormatString", accessLogFormatString, false));
        setBasicAuthentication(readProperty(properties, "basicAuthentication", basicAuthentication, true));
        setHealthPath(readProperty(properties, "healthPath", healthPath, true));
        setResourcePath(readProperty(properties, "resourcePath", resourcePath, false));
        return this;
    }

    
    /**
     * Read properties from classpath
     *
     * @return the read properties
     */
    private Properties readPropertiesFromClasspath() {
        Properties properties = null;
        
        try {
            try (InputStream stream = this.getClass().getResourceAsStream(SLASH + JWEBSERVER_PROPERTIES)) {
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
            LOG.warn("Invalid value [" + result + "] for attribute [" + name + "], keep default value [" + defaultValue + "].");
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
            LOG.warn("Invalid value [" + result + "] for attribute [" + name + "], keep default value [" + defaultValue + "].");
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
            LOG.warn("Invalid value [" + result + "] for attribute [" + name + "], keep default value [" + defaultValue + "].");
            return defaultValue;
        }
    }
}
