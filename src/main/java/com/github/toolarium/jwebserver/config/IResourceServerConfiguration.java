/*
 * IWebServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;


/**
 * Defines the webserver resource interface
 * 
 * @author patrick
 */
public interface IResourceServerConfiguration {
    
    /**
     * Get the directory
     *
     * @return the directory
     */
    String getDirectory();

    
    /**
     * Define if the directory is local  
     *
     * @return true if the directory is local
     */
    boolean isLocalDirectory();

    
    /**
     * Define if the directory listing is enabled  
     *
     * @return true if it is enabled
     */
    boolean isDirectoryListingEnabled();

    
    /**
     * Define if the directory should be read from the classpath  
     *
     * @return true if the directory should be read from the classpath
     */
    boolean readFromClasspath();

    
    /**
     * Defines what happen in case a requested resource can't be found. In case of true (default) 
     * In case of true parent resources a resolved with the help of welcomeFiles.
     * In case of false the resolution of parent resources is disabled.
     *
     * @return true to resolve parent resource if the resource can't be found.
     */
    boolean resolveParentResourceIfNotFound();
    
    
    /**
     * Set the welcome files
     *
     * @return the welcome files
     */
    String[] getWelcomeFiles();


    /**
     * Set the supported file extensions
     *
     * @return the supported file extensions
     */
    String[] getSupportedFileExtensions();
}
