/*
 * LifecycleLogger.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.logger;

import com.github.toolarium.jwebserver.Version;
import com.github.toolarium.jwebserver.config.IResourceServerConfiguration;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.handler.routing.RoutingHandler;
import com.github.toolarium.jwebserver.logger.ansi.ColoredStackTraceWriter;
import com.github.toolarium.jwebserver.util.ConfigurationUtil;
import io.undertow.Undertow.ListenerInfo;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import picocli.CommandLine.Help;
import picocli.CommandLine.Help.ColorScheme;


/**
 * The startup logger
 *  
 * @author patrick
 */
public class LifecycleLogger {
    private static final String NL = "\n";
    private static final String LINE = "----------------------------------------------------------------------------------------";
    private static final String APP = "jwebserver v" + Version.VERSION;
    private ColorScheme colorSchema = Help.defaultColorScheme(Help.Ansi.AUTO);


    /**
     * Constructor
     */
    public  LifecycleLogger() {
    }

    
    /**
     * Constructor
     * 
     * @param colorSchema the color schema
     */
    public  LifecycleLogger(ColorScheme colorSchema) {
        this.colorSchema = colorSchema;
    }

    
    /**
     * Get the color schema
     *
     * @return Get the color schema
     */
    public ColorScheme getColorScheme() {
        return colorSchema;
    }
    
    
    /**
     * Print server startup
     *
     * @param webServerConfiguration the web server configuration
     * @param listenerInfoList the listener information
     */
    public void printServerStartup(IWebServerConfiguration webServerConfiguration, List<ListenerInfo> listenerInfoList) {
        System.out.println(prepareServerStartup(webServerConfiguration, listenerInfoList)); // CHECKSTYLE IGNORE THIS LINE
    }
    

    /**
     * Convert a {@code Throwable} to a {@code String} , with message and stack traces extracted and colored according
     * to {@code ColorScheme}.
     * 
     * @param t the {@code Throwable} to be converted
     * @return converted and colored {@code String}
     */
    public String preapreThrowable(Throwable t) {
        Help.ColorScheme colorScheme = new Help.ColorScheme.Builder(getColorScheme()).applySystemProperties().build();
        StringWriter stringWriter = new ColoredStackTraceWriter(colorScheme);
        t.printStackTrace(new PrintWriter(stringWriter)); // CHECKSTYLE IGNORE THIS LINE
        return stringWriter.toString();
    }

    
    /**
     * Get server message
     *
     * @param webServerConfiguration the web server configuration
     * @param listenerInfoList the listener information
     * @return the server message
     */
    public String prepareServerStartup(IWebServerConfiguration webServerConfiguration, List<ListenerInfo> listenerInfoList) {
        final String resourcePath = prepareResourcePath(webServerConfiguration);
        StringBuilder message = new StringBuilder();
        message.append(NL).append(LINE).append(NL);
        
        preapreTitle(message, webServerConfiguration);
        prepareListener(message, webServerConfiguration, listenerInfoList, resourcePath);

        if (webServerConfiguration.isProxyServer()) {
            prepareProxy(message, webServerConfiguration, resourcePath);
        } else {
            prepareResource(message, webServerConfiguration, resourcePath);
        }

        if (listenerInfoList != null) { 
            if (webServerConfiguration.hasHealthCheck()) {
                prepareHeader(message, "Health").append(commandText(webServerConfiguration.getHealthPath())).append(NL);
            }
            
            if (webServerConfiguration.hasBasicAuthentication()) {
                prepareHeader(message, "Basic Auth").append("enabled").append(NL);
            }
    
            if (webServerConfiguration.isProxyServer()) {
                // NOP
            } else {
                if (webServerConfiguration.getResourceServerConfiguration().isDirectoryListingEnabled()) {
                    prepareHeader(message, "Listing").append("enabled").append(NL);
                }
            }
        }
        
        message.append(LINE).append(NL);
        return message.toString();
    }

    
    /**
     * Prepare the resource path
     * 
     * @param webServerConfiguration the web server configuration
     * @return the prepared resource path
     */
    public String prepareResourcePath(IWebServerConfiguration webServerConfiguration) {
        String resourcePath = webServerConfiguration.getResourcePath();
        if (!resourcePath.isEmpty() && !resourcePath.startsWith(RoutingHandler.SLASH)) {
            resourcePath = RoutingHandler.SLASH + resourcePath;
        }

        if (!resourcePath.endsWith(RoutingHandler.SLASH)) { 
            resourcePath += RoutingHandler.SLASH;
        }
        return resourcePath;
    }

    
    /**
     * Prepare the path
     * 
     * @param resourceConfiguration the resource configuration
     * @param resourcePath the resource path
     * @return the message
     */
    private String preparePath(IResourceServerConfiguration resourceConfiguration, String resourcePath) {
        String path = resourceConfiguration.getDirectory();
        if (path.equals(".")) {
            path = "";
        }

        if (!path.isEmpty() && !path.startsWith(RoutingHandler.SLASH)) {
            path = RoutingHandler.SLASH + path;
        }

        if (!resourcePath.equals(RoutingHandler.SLASH)) {
            path += resourcePath;
        }

        if (!path.endsWith(RoutingHandler.SLASH)) {
            path += RoutingHandler.SLASH;
        }

        if (resourceConfiguration.isLocalDirectory()) {
            path = System.getProperty("user.dir").replace('\\', '/') + path;
        }
        return path;
    }
    
    
    /**
     * Prepare the title
     * 
     * @param webServerConfiguration the web server configuration
     * @param message the message
     * @return the message
     */
    private StringBuilder preapreTitle(StringBuilder message, IWebServerConfiguration webServerConfiguration) {
        message.append("  ");
        if (webServerConfiguration.getWebserverName() != null && !webServerConfiguration.getWebserverName().isBlank()) {
            //title = "" + parameterText(configuration.getWebserverName()) + " (powered by " + APP + ")";
            message.append(parameterText(webServerConfiguration.getWebserverName())).append(" (powered by ").append(APP).append(")");

        } else {
            message.append(parameterText(APP)); /*Ansi.AUTO.string("@|bold,blue " + APP + "!|@")*/
        }
        message.append(NL);
        return message;
    }


    /**
     * Prepare the listener
     * 
     * @param message the message
     * @param webServerConfiguration the web server configuration
     * @param listenerInfoList the listener list
     * @param resourcePath the resource path
     * @return the message
     */
    private StringBuilder prepareListener(StringBuilder message, IWebServerConfiguration webServerConfiguration, List<ListenerInfo> listenerInfoList, String resourcePath) {
        if (listenerInfoList != null && !listenerInfoList.isEmpty()) {
            for (ListenerInfo listenerInfo : listenerInfoList) {
                StringBuilder listenerInfoMessage = new StringBuilder();
                if (listenerInfo.getSslContext() == null) {
                    listenerInfoMessage.append(listenerInfo.getProtcol());
                } else {
                    listenerInfoMessage.append(listenerInfo.getSslContext().getProtocol());
                }
                listenerInfoMessage.append(":/");
    
                String listenerAddress = "" + listenerInfo.getAddress();
                if (listenerInfo.getAddress() instanceof InetSocketAddress) {
                    InetAddress address = ((InetSocketAddress)listenerInfo.getAddress()).getAddress();
                    if (address instanceof Inet6Address) {
                        if (listenerInfoList.size() == 1) {
                            if (webServerConfiguration.getPort() != null) {
                                listenerAddress = RoutingHandler.SLASH + webServerConfiguration.getHostname() + ":" + webServerConfiguration.getPort();                      
                            }
                            
                            if (webServerConfiguration.getSecurePort() != null) {
                                listenerAddress = RoutingHandler.SLASH + webServerConfiguration.getHostname() + ":" + webServerConfiguration.getSecurePort();                      
                            }
                        }
                    }
                }
                listenerInfoMessage.append(listenerAddress);
                
                if (!RoutingHandler.SLASH.equals(resourcePath)) {
                    listenerInfoMessage.append(resourcePath);
                }
                listenerInfoMessage.append(NL);
                
                prepareHeader(message, "Listener");
                message.append(commandText(listenerInfoMessage.toString()));
            }
        }
        
        return message;
    }

    
    /**
     * Prepare proxy message
     * 
     * @param webServerConfiguration the web server configuration
     * @param resourcePath the resource path
     * @param message the message builder
     * @return the message
     */
    private StringBuilder prepareProxy(StringBuilder message, IWebServerConfiguration webServerConfiguration, final String resourcePath) {
        return prepareHeader(message, "Proxy").append(commandText(resourcePath)).append(" -> ")
                .append(commandText(ConfigurationUtil.getInstance().formatArrayAsString(webServerConfiguration.getProxyServerConfiguration().getProxyHostNames()))).append(NL);
    }


    /**
     * Prepare resource message
     * 
     * @param webServerConfiguration the web server configuration
     * @param resourcePath the resource path
     * @param message the message builder
     * @return the message
     */
    private StringBuilder prepareResource(StringBuilder message, IWebServerConfiguration webServerConfiguration, final String resourcePath) {
        final String path = preparePath(webServerConfiguration.getResourceServerConfiguration(), resourcePath);
        String pathType = "{PATH}:";
        if (webServerConfiguration.getResourceServerConfiguration().readFromClasspath()) {
            pathType = " {CLASSPATH}:";
        }
        return prepareHeader(message, "Resource").append(commandText(resourcePath)).append(" -> ").append(pathType).append(commandText(path)).append(NL);
    }


    /**
     * Command text
     *
     * @param message the message
     * @return the formated message
     */
    private String commandText(String message) {
        if (getColorScheme() != null) {
            return "" + getColorScheme().commandText(message);
        }
        
        return message;
    }
    

    /**
     * The parameter text
     *
     * @param message the message
     * @return the formated message
     */
    private String parameterText(String message) {
        if (getColorScheme() != null) {
            return "" + getColorScheme().parameterText(message);
        }
        
        return message;
    }

    
    /**
     * Prepare header
     *
     * @param message the message
     * @param tag the tag
     * @return the message
     */
    private StringBuilder prepareHeader(StringBuilder message, String tag) {
        message.append("  > ");
        message.append(tag);
        for (int i = tag.length(); i < 11; i++) {
            message.append(' ');
        }
        return message;
    }
}
