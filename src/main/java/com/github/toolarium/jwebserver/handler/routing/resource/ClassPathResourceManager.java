/*
 * ClassPathResourceManager.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.routing.resource;

import com.github.toolarium.jwebserver.config.IResourceServerConfiguration;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import io.undertow.server.handlers.resource.Resource;
import java.io.IOException;
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
    }
    
    
    /**
     * @see io.undertow.server.handlers.resource.ClassPathResourceManager#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String path) throws IOException {
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

        if (resource != null) {
            if (resource.isDirectory()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Found directory [" + resource.getPath() + "] in  [" + resource.getUrl() + "].");
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
                            LOG.debug("Found directory [" + resource.getPath() + "/] in  [" + resource.getUrl() + "].");
                        }
                        return subResource;
                    }
                }
                
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Found resource [" + resource.getPath() + "] in  [" + resource.getUrl() + "] " + resource.getContentLength());
                }
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Resource not found [" + path + "].");
            }
        }
                
        return resource;
    }
}
