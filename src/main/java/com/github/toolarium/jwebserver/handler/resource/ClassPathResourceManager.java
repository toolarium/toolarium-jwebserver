/*
 * ClassPathResourceManager.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.handler.resource;

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
    

    /**
     * Constructor for ClassPathResourceManager
     *
     * @param classLoader the class loader
     * @param prefix the prefix
     */
    public ClassPathResourceManager(final ClassLoader classLoader, final String prefix) {
        super(classLoader, prefix);
    }
    
    
    /**
     * @see io.undertow.server.handlers.resource.ClassPathResourceManager#getResource(java.lang.String)
     */
    @Override
    public Resource getResource(String path) throws IOException {
        Resource resource = super.getResource(path);
        
        if (LOG.isDebugEnabled()) {
            if (resource != null) {
                if (resource.isDirectory()) {
                    LOG.debug("Found directory [" + resource.getPath() + "] in  [" + resource.getUrl() + "].");
                } else {
                    LOG.debug("Found resource [" + resource.getPath() + "] in  [" + resource.getUrl() + "] " + resource.getContentLength());
                }
            } else {
                LOG.debug("Resource not found [" + path + "].");
            }
        }
        
        
        return resource;
    }
}
