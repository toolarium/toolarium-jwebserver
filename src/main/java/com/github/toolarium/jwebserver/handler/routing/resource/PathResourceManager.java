/*
 * PathResourceManager.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.routing.resource;

import com.github.toolarium.jwebserver.config.IResourceServerConfiguration;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.util.ResourceUtil;
import io.undertow.server.handlers.resource.Resource;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Path resource manager
 * 
 * @author patrick
 */
public class PathResourceManager extends io.undertow.server.handlers.resource.PathResourceManager {
    private static final Logger LOG = LoggerFactory.getLogger(PathResourceManager.class);
    private final IResourceServerConfiguration configuration;
    private List<String> welcomeFiles;

    
    /**
     * Constructor for PathResourceManager
     *
     * @param webServerConfiguration the web server configuration
     * @param base the base
     * @param transferMinSize the transfer min size
     */
    public PathResourceManager(final IWebServerConfiguration webServerConfiguration, final Path base, long transferMinSize) {
        super(base, transferMinSize);
        this.configuration = webServerConfiguration.getResourceServerConfiguration();
        this.welcomeFiles = Collections.emptyList();
    }

    
    /**
     * Set the welcome files
     *
     * @param welcomeFiles the welcome files
     */
    public void setWelcomeFiles(String[] welcomeFiles) {
        if (welcomeFiles != null) {
            this.welcomeFiles = Arrays.asList(welcomeFiles);
        }
    }


    /**
     * @see io.undertow.server.handlers.resource.PathResourceManager#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String path)  {
        Resource resource = super.getResource(path);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request resource [" + path + "]" + ResourceUtil.getInstance().toString(resource));
        }

        // in case no resource found, try with supported file extensions
        if (resource == null && path.indexOf('.') < 0 && configuration.getSupportedFileExtensions() != null && configuration.getSupportedFileExtensions().length > 0) {
            for (String supportedFileExtension : configuration.getSupportedFileExtensions()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Check resource [" + path + "] with extension [" + supportedFileExtension + "].");
                }
                
                resource = super.getResource(path + supportedFileExtension);
                if (resource != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Found resource [" + path + supportedFileExtension + "] in  [" + resource.getUrl() + "] " + resource.getContentLength());
                    }
                    
                    break;
                }
            }
        }

        if (resource != null && resource.isDirectory() && !path.endsWith("/")) {
            final String directoryPath = ResourceUtil.getInstance().slashify(path);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Test welcome files: " + welcomeFiles);
            }
            
            Resource indexResource = null;
            String[] directorySplit = directoryPath.split("/");
            for (int i = directorySplit.length - 1; i >= 0; i--) {
                String parentPath = ResourceUtil.getInstance().prepareString(directorySplit, i);
                indexResource = getIndexFiles(parentPath);
                if (indexResource != null) {
                    resource = indexResource;
                    break;
                }
            }
        }

        return resource;
    }


    /**
     * Get the index file
     *
     * @param base the base path
     * @return the resource
     */
    protected Resource getIndexFiles(final String base) {
        for (String possibility : welcomeFiles) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Test resource [" + base + "] [" + ResourceUtil.getInstance().canonicalize(ResourceUtil.getInstance().slashify(base) + possibility) + "]");
            }
            
            Resource indexResource = super.getResource(ResourceUtil.getInstance().canonicalize(ResourceUtil.getInstance().slashify(base) + possibility));
            if (indexResource != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Request resource index found [" + base + "]" + ResourceUtil.getInstance().toString(indexResource));
                }
                return indexResource;
            }
        }
        
        return null;
    }
}
