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
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Path resource manager
 * 
 * @author patrick
 */
public class PathResourceManager extends io.undertow.server.handlers.resource.PathResourceManager {
    private static final Logger LOG = LoggerFactory.getLogger(PathResourceManager.class);
    private static final int MAX_EXTENSION_CACHE_SIZE = 1024;
    private static final String NO_MATCH = "";
    private final IResourceServerConfiguration configuration;
    private final ConcurrentHashMap<String, String> extensionCache = new ConcurrentHashMap<>();
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
        // canonicalize path to prevent traversal via encoded sequences
        String canonicalPath = ResourceUtil.getInstance().canonicalize(path);
        if (canonicalPath.contains("..")) {
            LOG.warn("Blocked path traversal attempt: [" + path + "]");
            return null;
        }

        Resource resource = super.getResource(canonicalPath);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request resource [" + canonicalPath + "]" + ResourceUtil.getInstance().toString(resource));
        }

        // in case no resource found, try with supported file extensions (with caching)
        if (resource == null && canonicalPath.indexOf('.') < 0 && configuration.getSupportedFileExtensions() != null && configuration.getSupportedFileExtensions().length > 0) {
            String cached = extensionCache.get(canonicalPath);
            if (cached != null) {
                if (!NO_MATCH.equals(cached)) {
                    resource = super.getResource(cached);
                    if (LOG.isDebugEnabled() && resource != null) {
                        LOG.debug("Found resource [" + cached + "] in  [" + resource.getUrl() + "] " + resource.getContentLength());
                    }
                }
            } else {
                for (String supportedFileExtension : configuration.getSupportedFileExtensions()) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Check resource [" + canonicalPath + "] with extension [" + supportedFileExtension + "].");
                    }

                    String resolvedPath = canonicalPath + supportedFileExtension;
                    resource = super.getResource(resolvedPath);
                    if (resource != null) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Found resource [" + canonicalPath + supportedFileExtension + "] in  [" + resource.getUrl() + "] " + resource.getContentLength());
                        }
                        if (extensionCache.size() < MAX_EXTENSION_CACHE_SIZE) {
                            extensionCache.put(canonicalPath, resolvedPath);
                        }
                        break;
                    }
                }
                if (resource == null && extensionCache.size() < MAX_EXTENSION_CACHE_SIZE) {
                    extensionCache.put(canonicalPath, NO_MATCH);
                }
            }
        }

        if (configuration.resolveParentResourceIfNotFound()) {
            if (resource != null && resource.isDirectory() && !canonicalPath.endsWith("/")) {
                final String directoryPath = ResourceUtil.getInstance().slashify(canonicalPath);
                
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
