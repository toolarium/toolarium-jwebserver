/*
 * StdoutAccessLogReceiver.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.logger.access;

import io.undertow.server.handlers.accesslog.AccessLogReceiver;

/**
 * Implements a standard out access log receiver
 *  
 * @author patrick
 */
public class StdoutAccessLogReceiver implements AccessLogReceiver {

    /**
     * @see io.undertow.server.handlers.accesslog.AccessLogReceiver#logMessage(java.lang.String)
     */
    @Override
    public void logMessage(String message) {
        System.out.println(message); // CHECKSTYLE IGNORE THIS LINE
    }
}
