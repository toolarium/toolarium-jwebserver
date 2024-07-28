/*
 * IWebServerConfiguration.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.config;

import com.github.toolarium.jwebserver.logger.VerboseLevel;


/**
 * Defines the webserver configuration interface
 * 
 * @author patrick
 */
public interface IWebServerConfiguration {
    
    /**
     * Get the webserver name
     *
     * @return the webserver name
     */
    String getWebserverName();

    
    /**
     * Get the hostname
     *
     * @return the hostname
     */
    String getHostname();

    
    /**
     * Get the port
     *
     * @return the port
     */
    Integer getPort();

    
    /**
     * Get the secure port
     *
     * @return the secure port
     */
    Integer getSecurePort();

    
    /**
     * Get the verbose level
     *
     * @return the verbose level
     */
    VerboseLevel getVerboseLevel();

    
    /**
     * Get access log format string:
     * <p>
     * <ul>
     * <li><b>%a</b> - Remote IP address
     * <li><b>%A</b> - Local IP address
     * <li><b>%b</b> - Bytes sent, excluding HTTP headers, or '-' if no bytes were sent
     * <li><b>%B</b> - Bytes sent, excluding HTTP headers
     * <li><b>%h</b> - Remote host name
     * <li><b>%H</b> - Request protocol
     * <li><b>%l</b> - Remote logical username from identd (always returns '-')
     * <li><b>%m</b> - Request method
     * <li><b>%o</b> - Obfuscated remote IP address (IPv4: last byte removed, IPv6: cut off after second colon, ie. '1.2.3.' or 'fe08:44:')
     * <li><b>%p</b> - Local port
     * <li><b>%q</b> - Query string (excluding the '?' character)
     * <li><b>%r</b> - First line of the request
     * <li><b>%s</b> - HTTP status code of the response
     * <li><b>%t</b> - Date and time, in Common Log Format format
     * <li><b>%u</b> - Remote user that was authenticated
     * <li><b>%U</b> - Requested URL path
     * <li><b>%v</b> - Local server name
     * <li><b>%D</b> - Time taken to process the request, in millis
     * <li><b>%T</b> - Time taken to process the request, in seconds
     * <li><b>%I</b> - current Request thread name (can compare later with stacktraces)
     * </ul>
     * </p>
     * <p>
     * In addition, the caller can specify one of the following aliases for commonly utilized patterns:
     * </p>
     * <ul>
     * <li><b>common</b> - <code>%h %l %u %t "%r" %s %b</code>
     * <li><b>combined</b> - <code>%h %l %u %t "%r" %s %b "%{i,Referer}" "%{i,User-Agent}"</code>
     * <li><b>commonobf</b> - <code>%o %l %u %t "%r" %s %b</code>
     * <li><b>combinedobf</b> - <code>%o %l %u %t "%r" %s %b "%{i,Referer}" "%{i,User-Agent}"</code>
     * </ul>
     * </p>
     * <p>
     * There is also support to write information from the cookie, incoming header, or the session<br>
     * It is modeled after the apache syntax:
     * <ul>
     * <li><code>%{i,xxx}</code> for incoming headers
     * <li><code>%{o,xxx}</code> for outgoing response headers
     * <li><code>%{c,xxx}</code> for a specific cookie
     * <li><code>%{r,xxx}</code> xxx is an attribute in the ServletRequest
     * <li><code>%{s,xxx}</code> xxx is an attribute in the HttpSession
     * </ul>
     * </p>
     * 
     * @return the accesslog format string
     */
    String getAccessLogFormatString();

    
    /**
     * The access log file pattern, e.g. "access-%d{yyyy-MM-dd}.log.gz"
     * <p>
     * For more information, please refer to the online manual at
     * http://logback.qos.ch/manual/appenders.html#TimeBasedRollingPolicy</p>
     *
     * @return the access log file pattern
     */
    String getAccessLogFilePattern();

    
    /**
     * Define if the resource has basic authentication  
     *
     * @return true if it is enabled
     */
    boolean hasBasicAuthentication();

    
    /**
     * Get the basic authentication: user:password
     *
     * @return the basic authentication
     */
    String getBasicAuthentication();

    
    /**
     * Define if the server support health  
     *
     * @return true if it is enabled
     */
    boolean hasHealthCheck();
    

    /**
     * Get the health path  
     *
     * @return the health path
     */
    String getHealthPath();
    

    /**
     * Get the number of I/O threads
     *
     * @return the number of I/O threads
     */
    int getIoThreads();
    
    
    /**
     * Get the number of working threads 
     *
     * @return the number of working threads
     */
    int getWorkerThreads();
    
    
    /**
     * Get the resource path  
     *
     * @return the resource path
     */
    String getResourcePath();

    
    /**
     * Get the ssl server configuration
     *
     * @return the ssl server configuration
     */
    SSLServerConfiguration getSSLServerConfiguration();
    
    
    /**
     * Get the resource server configuration
     *
     * @return the resource server configuration
     */
    ResourceServerConfiguration getResourceServerConfiguration();
    
    
    /**
     * Get the proxy server configuration
     *
     * @return the proxy server configuration
     */
    ProxyServerConfiguration getProxyServerConfiguration();
    
    
    /**
     * True if it is a proxy server
     *
     * @return true if it is a proxy server
     */
    boolean isProxyServer();
}
