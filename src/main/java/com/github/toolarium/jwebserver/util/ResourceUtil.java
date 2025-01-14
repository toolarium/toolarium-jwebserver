/*
 * ResourceUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.util;


/**
 * Resource utility.
 * 
 * @author patrick
 */
public final class ResourceUtil {
    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     */
    private static final class HOLDER {
        static final ResourceUtil INSTANCE = new ResourceUtil();
    }

    
    /**
     * Constructor
     */
    private ResourceUtil() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ResourceUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Slashify
     *
     * @param base the path
     * @return the slashified string
     */
    public String slashify(String base) {
        if (base == null) {
            return base;
        }
        
        if (base.endsWith("/")) {
            return base;
        }
        
        return base + "/";
    }

    
    /**
     * Canonicalize
     *
     * @param s the string to canonicalize
     * @return the canonicalized string
     */
    public String canonicalize(String s) {
        return io.undertow.util.CanonicalPathUtils.canonicalize(s);
    }

    
    /**
     * Prepare string
     *
     * @param input the input
     * @param size the size of the string
     * @return the prepared string
     */
    public String prepareString(String[] input, int size) {
        if (input == null || input.length == 0 || size == 0) {
            return "/";
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; (i < input.length && i <= size); i++) {
            builder.append(input[i]);
            builder.append("/");
        }
        return builder.toString();
    }
    
    
    /**
     * Convert a resource into a string
     *
     * @param resource the resource
     * @return the string
     */
    public String toString(io.undertow.server.handlers.resource.Resource resource) {
        StringBuilder builder = new StringBuilder();
        
        if (resource != null) {
            builder.append(" -> ");
            builder.append(resource.getName());
            //builder.append(resource.getPath());
            //builder.append(" | ");
            //builder.append(resource.getFilePath());
            
            builder.append(" | ");
            if (resource.isDirectory()) {
                builder.append("*");
            } else {
                builder.append(resource.getContentLength());
            }
            builder.append(" | ");
            builder.append(resource.getResourceManagerRootPath());
            builder.append(" | ");
            builder.append(resource.getLastModifiedString());
            //builder.append(resource.getUrl());
        }
    
        return builder.toString();
    }
}
