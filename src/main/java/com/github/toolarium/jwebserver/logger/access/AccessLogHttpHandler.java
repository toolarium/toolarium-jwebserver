/*
 * AccessLogHandlerUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.logger.access;

import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.logger.VerboseLevel;
import com.github.toolarium.jwebserver.logger.logback.LogbackUtil;
//import com.github.toolarium.jwebserver.logger.LogbackUtil;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import org.slf4j.Logger;



/**
 * The access log handler utility
 *  
 * @author patrick
 */
public final class AccessLogHttpHandler {
    private static final String ACCESSLOG_APPENDER_NAME = AccessLogHttpHandler.class.getCanonicalName();
    
    /**
     * Constructor for AccessLogHttpHandler
     */
    private AccessLogHttpHandler() {
        // NOP
    }

    
    /**
     * Add basic authentication
     *
     * @param configuration the configuration
     * @param handlerToWrap the handler to wrap
     * @return the handler
     */
    public static HttpHandler addHandler(final IWebServerConfiguration configuration, final HttpHandler handlerToWrap) {
        
        if (VerboseLevel.VERBOSE.equals(configuration.getVerboseLevel()) || VerboseLevel.ACCESS.equals(configuration.getVerboseLevel())) {
            final Logger log = LogbackUtil.getInstance().createAccessLogAppender(ACCESSLOG_APPENDER_NAME, configuration.getAccessLogFilePattern());
            final AccessLogReceiver accessLogReceiver = new Slf4jAccessLogReceiver(log);
            return new AccessLogHandler(handlerToWrap, accessLogReceiver, configuration.getAccessLogFormatString(), AccessLogHttpHandler.class.getClassLoader());
        } else if (VerboseLevel.ACCESS_CONSOLE.equals(configuration.getVerboseLevel())) {
            //LogbackUtil.getInstance().detachAppender(ACCESSLOG_APPENDER_NAME);
            final AccessLogReceiver accessLogReceiver = new StdoutAccessLogReceiver();
            return new AccessLogHandler(handlerToWrap, accessLogReceiver, configuration.getAccessLogFormatString(), AccessLogHttpHandler.class.getClassLoader());
        } else {
            //LogbackUtil.getInstance().detachAppender(ACCESSLOG_APPENDER_NAME);
        }
        
        return handlerToWrap;
    }
}
