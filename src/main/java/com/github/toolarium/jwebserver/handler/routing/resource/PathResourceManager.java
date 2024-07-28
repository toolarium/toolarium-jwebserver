/*
 * PathResourceManager.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.routing.resource;

import com.github.toolarium.jwebserver.config.IResourceServerConfiguration;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import io.undertow.server.handlers.resource.Resource;
import java.nio.file.Path;
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
    }


    /**
     * @see io.undertow.server.handlers.resource.PathResourceManager#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String path)  {
        Resource resource = super.getResource(path);

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
                
        return resource;
    }
}
