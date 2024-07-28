/*
 * ResourceServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import com.github.toolarium.jwebserver.handler.routing.RoutingHandler;
import com.github.toolarium.jwebserver.util.ConfigurationUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the {@link IResourceServerConfiguration}.
 * 
 * @author patrick
 */
public class ResourceServerConfiguration implements IResourceServerConfiguration, Serializable {
    private static final long serialVersionUID = 2768362014487262782L;
    private static final String DOT = ".";
    private static final String END_VALUE = "].";
    private static final Logger LOG = LoggerFactory.getLogger(ResourceServerConfiguration.class);
    private static final String USER_HOME = System.getProperty("user.home");
    
    private String directory;
    private boolean isLocalDirectory;
    private boolean readFromClasspath;
    private boolean directoryListingEnabled;
    private String[] welcomeFiles;
    private String[] supportedFileExtensions;
    
    
    /**
     * Constructor for ResourceServerConfiguration
     */
    public ResourceServerConfiguration() {
        this.directory = DOT;
        this.isLocalDirectory = true;
        this.readFromClasspath = false;
        this.directoryListingEnabled = false;
        this.welcomeFiles = new String[] {"index.html", "index.htm", "default.html", "default.htm"};
        this.supportedFileExtensions = null;
    }


    /**
     * Constructor for ResourceServerConfiguration
     * 
     * @param configuration the configuration
     */
    public ResourceServerConfiguration(IResourceServerConfiguration configuration) {
        this.directory = configuration.getDirectory();
        this.isLocalDirectory = configuration.isLocalDirectory();
        this.readFromClasspath = configuration.readFromClasspath();
        this.directoryListingEnabled = configuration.isDirectoryListingEnabled();
        this.welcomeFiles = configuration.getWelcomeFiles();
        this.supportedFileExtensions = configuration.getSupportedFileExtensions();
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IResourceServerConfiguration#getDirectory()
     */
    @Override
    public String getDirectory() {
        return directory;
    }

    
    /**
     * Set the directory
     *
     * @param directory the directory
     * @return the ResourceServerConfiguration
     */
    public ResourceServerConfiguration setDirectory(String directory) {
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
     * @return the ResourceServerConfiguration
     */
    public ResourceServerConfiguration setDirectory(String directory, Boolean readFromClasspath) {
        LOG.debug("Set directory: [" + directory + "], readFromClasspath: [" + readFromClasspath + END_VALUE);
        
        if (directory != null) {
            this.directory = directory.trim();

            if ("%HOME%".equals(directory) || "$HOME".equals(directory)) {
                this.directory = USER_HOME;
            }
        }
        
        if (readFromClasspath != null) {
            this.readFromClasspath = readFromClasspath.booleanValue();
        }

        LOG.debug("Set isLocalDirectory: " + this.directory.startsWith(RoutingHandler.SLASH) + RoutingHandler.SLASH
                + this.directory.startsWith("\\") + RoutingHandler.SLASH
                + (this.directory.length() > 2 && this.directory.substring(1).startsWith(":")));
        this.isLocalDirectory = !(this.readFromClasspath 
                || this.directory.startsWith(RoutingHandler.SLASH) 
                || this.directory.startsWith("\\") 
                || (this.directory.length() > 2 && this.directory.substring(1).startsWith(":")));

        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IResourceServerConfiguration#readFromClasspath()
     */
    @Override
    public boolean readFromClasspath() {
        return readFromClasspath;
    }

    
    /**
     * @see com.github.toolarium.jwebserver.config.IResourceServerConfiguration#isLocalDirectory()
     */
    @Override
    public boolean isLocalDirectory() {
        return isLocalDirectory;
    }

    
    
    /**
     * @see com.github.toolarium.jwebserver.config.IResourceServerConfiguration#isDirectoryListingEnabled()
     */
    @Override
    public boolean isDirectoryListingEnabled() {
        return directoryListingEnabled;
    }


    /**
     * Define if the directory listing is enabled  
     *
     * @param directoryListingEnabled turn true if it is enabled
     * @return the ResourceServerConfiguration
     */
    public ResourceServerConfiguration setDirectoryListingEnabled(Boolean directoryListingEnabled) {
        if (directoryListingEnabled != null) {
            LOG.debug("Set directoryListingEnabled: [" + directoryListingEnabled + END_VALUE);
            this.directoryListingEnabled = directoryListingEnabled.booleanValue();
        }
        
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IResourceServerConfiguration#getWelcomeFiles()
     */
    @Override
    public String[] getWelcomeFiles() {
        return welcomeFiles;
    }

    
    /**
     * Set the welcome files
     *
     * @param welcomeFiles the welcome files
     * @return the ResourceServerConfiguration
     */
    public ResourceServerConfiguration setWelcomeFiles(String welcomeFiles) {
        if (welcomeFiles != null) {
            setWelcomeFiles(ConfigurationUtil.getInstance().parseStringArray(welcomeFiles));
        }
        return this;
    }

    
    /**
     * Set the welcome files
     *
     * @param welcomeFiles the welcome files
     * @return the ResourceServerConfiguration
     */
    public ResourceServerConfiguration setWelcomeFiles(String[] welcomeFiles) {
        if (welcomeFiles != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Set welcomeFiles: [" + ConfigurationUtil.getInstance().formatArrayAsString(welcomeFiles) + END_VALUE);            
            }
            
            this.welcomeFiles = welcomeFiles;
        }
        return this;
    }


    /**
     * @see com.github.toolarium.jwebserver.config.IResourceServerConfiguration#getSupportedFileExtensions()
     */
    @Override
    public String[] getSupportedFileExtensions() {
        return this.supportedFileExtensions;
    }

    
    /**
     * Set the supported file extensions
     *
     * @param supportedFileExtensions the supported file extensions
     * @return the ResourceServerConfiguration
     */
    public ResourceServerConfiguration setSupportedFileExtensions(String supportedFileExtensions) {
        if (supportedFileExtensions != null) {
            setSupportedFileExtensions(ConfigurationUtil.getInstance().parseStringArray(supportedFileExtensions));
        }
        return this;
    }

    
    /**
     * Set the supported file extensions
     *
     * @param supportedFileExtensions the supported file extensions
     * @return the ResourceServerConfiguration
     */
    public ResourceServerConfiguration setSupportedFileExtensions(String[] supportedFileExtensions) {
        if (supportedFileExtensions != null) {
            this.supportedFileExtensions = supportedFileExtensions;
            for (int i = 0; i < this.supportedFileExtensions.length; i++) {
                if (this.supportedFileExtensions[i] != null && !this.supportedFileExtensions[i].startsWith(DOT)) {
                    this.supportedFileExtensions[i] = DOT + this.supportedFileExtensions[i];
                }
            }
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Set supportedFileExtensions: [" + ConfigurationUtil.getInstance().formatArrayAsString(this.supportedFileExtensions) + END_VALUE);            
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
        result = prime * result + Arrays.hashCode(supportedFileExtensions);
        result = prime * result + Arrays.hashCode(welcomeFiles);
        result = prime * result + Objects.hash(directory, directoryListingEnabled, isLocalDirectory, readFromClasspath);
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
        
        ResourceServerConfiguration other = (ResourceServerConfiguration) obj;
        return Objects.equals(directory, other.directory) && directoryListingEnabled == other.directoryListingEnabled
                && isLocalDirectory == other.isLocalDirectory && readFromClasspath == other.readFromClasspath
                && Arrays.equals(supportedFileExtensions, other.supportedFileExtensions)
                && Arrays.equals(welcomeFiles, other.welcomeFiles);
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResourceServerConfiguration [directory=" + directory + ", isLocalDirectory=" + isLocalDirectory
                + ", readFromClasspath=" + readFromClasspath + ", directoryListingEnabled=" + directoryListingEnabled
                + ", welcomeFiles=" + Arrays.toString(welcomeFiles)
                + ", supportedFileExtensions=" + Arrays.toString(supportedFileExtensions) + "]";
    }
}
