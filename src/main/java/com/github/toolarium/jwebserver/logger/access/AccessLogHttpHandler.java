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
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.accesslog.AccessLogReceiver;
import io.undertow.util.Headers;
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
     * @param webServerConfiguration the web server configuration
     * @param handlerToWrap the handler to wrap
     * @return the handler
     */
    public static HttpHandler addHandler(final IWebServerConfiguration webServerConfiguration, final HttpHandler handlerToWrap) {
        VerboseLevel level = webServerConfiguration.getVerboseLevel();
        if (VerboseLevel.VERBOSE.equals(level) || VerboseLevel.ACCESS.equals(level)) {
            final Logger log = LogbackUtil.getInstance().createAccessLogAppender(ACCESSLOG_APPENDER_NAME, webServerConfiguration.getAccessLogFilePattern());
            final AccessLogReceiver accessLogReceiver = new Slf4jAccessLogReceiver(log);
            return new AccessLogHandler(sanitizeAuthorizationHeader(handlerToWrap), accessLogReceiver, webServerConfiguration.getAccessLogFormatString(), AccessLogHttpHandler.class.getClassLoader());
        } else if (VerboseLevel.ACCESS_CONSOLE.equals(level)) {
            //LogbackUtil.getInstance().detachAppender(ACCESSLOG_APPENDER_NAME);
            final AccessLogReceiver accessLogReceiver = new StdoutAccessLogReceiver();
            return new AccessLogHandler(sanitizeAuthorizationHeader(handlerToWrap), accessLogReceiver, webServerConfiguration.getAccessLogFormatString(), AccessLogHttpHandler.class.getClassLoader());
        } else {
            //LogbackUtil.getInstance().detachAppender(ACCESSLOG_APPENDER_NAME);
        }

        return handlerToWrap;
    }


    /**
     * Wrap handler to redact the Authorization header after auth processing but before access log captures it
     *
     * @param next the next handler
     * @return the sanitizing handler
     */
    private static HttpHandler sanitizeAuthorizationHeader(final HttpHandler next) {
        return new HttpHandler() {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws Exception {
                // add completion listener to redact Authorization header before access log captures it
                exchange.addExchangeCompleteListener((ex, nextListener) -> {
                    if (ex.getRequestHeaders().contains(Headers.AUTHORIZATION)) {
                        ex.getRequestHeaders().put(Headers.AUTHORIZATION, "***");
                    }
                    nextListener.proceed();
                });
                next.handleRequest(exchange);
            }
        };
    }
}
