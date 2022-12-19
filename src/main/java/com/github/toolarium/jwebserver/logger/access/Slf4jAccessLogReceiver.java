/*
 * Slf4jAccessLogReceiver.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.logger.access;

import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import org.slf4j.Logger;


/**
 * The slf4j access log receiver
 * 
 * @author patrick
 */
public class Slf4jAccessLogReceiver implements AccessLogReceiver {
    private final Logger logger;

    /**
     * Constructor for Slf4jAccessLogReceiver
     *
     * @param logger the logger instance
     */
    public Slf4jAccessLogReceiver(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void logMessage(String message) {
        logger.info("{}", message);
    }
}