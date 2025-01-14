/*
 * ClassPathResourceManager.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.routing.resource;

import com.github.toolarium.jwebserver.config.IResourceServerConfiguration;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.util.ResourceUtil;
import io.undertow.server.handlers.resource.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Classpath resource manager to handle resource with missing slash
 *  
 * @author patrick
 */
public class ClassPathResourceManager extends io.undertow.server.handlers.resource.ClassPathResourceManager {
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathResourceManager.class);
    private final IResourceServerConfiguration configuration;
    private List<String> welcomeFiles;


    /**
     * Constructor for ClassPathResourceManager
     *
     * @param webServerConfiguration the web server configuration
     * @param classLoader the class loader
     * @param prefix the prefix
     */
    public ClassPathResourceManager(final IWebServerConfiguration webServerConfiguration, final ClassLoader classLoader, final String prefix) {
        super(classLoader, prefix);
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
     * @see io.undertow.server.handlers.resource.ClassPathResourceManager#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String path) throws IOException {
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

        resource = handleDirectory(path, resource);
        if (resource == null || (resource.isDirectory() && !path.endsWith("/"))) {
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
     * Handle directory resource proper
     *
     * @param path the path
     * @param resource the recognized resource
     * @return the resource
     * @throws IOException In case of an I/O error
     */
    protected Resource handleDirectory(String path, Resource resource) throws IOException {
        if (resource != null) {
            if (resource.isDirectory()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Found directory [" + resource.getPath() + "] in [" + resource.getUrl() + "].");
                }
            } else {
                // Issue and workaround: in some cases directory are not properly detected. In this cases the length is always zero.
                if ((resource.getContentLength() == null || resource.getContentLength().longValue() == 0L) && !path.endsWith("/")) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Check if it is a directory resource [" + resource.getPath() + "/]...");
                    }
                    
                    Resource subResource = super.getResource(path + "/");
                    if (subResource != null && subResource.isDirectory()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Found directory [" + resource.getPath() + "/] in [" + resource.getUrl() + "].");
                        }
                        return subResource;
                    }
                }
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Found resource [" + resource.getPath() + "] in [" + resource.getUrl() + "] " + resource.getContentLength());
                }
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resource not found [" + path + "].");
            }
        }
        
        return resource;
    }

    
    /**
     * Get the index file
     *
     * @param base the base path
     * @return the resource
     * @throws IOException In case of an I/O error
     */
    protected Resource getIndexFiles(final String base) throws IOException {
        for (String possibility : welcomeFiles) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Test resource [" + base + "] [" + ResourceUtil.getInstance().canonicalize(ResourceUtil.getInstance().slashify(base) + possibility) + "]");
            }
            
            Resource indexResource = handleDirectory(base, super.getResource(ResourceUtil.getInstance().canonicalize(ResourceUtil.getInstance().slashify(base) + possibility)));
            if (indexResource != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Request resource [" + base + "]" + ResourceUtil.getInstance().toString(indexResource));
                }
                return indexResource;
            }
        }
        
        return null;
    }
}
