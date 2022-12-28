/*
 * LifecycleLogger.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.logger;

import com.github.toolarium.jwebserver.Version;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.handler.resource.ResourceHandler;
import com.github.toolarium.jwebserver.logger.ansi.ColoredStackTraceWriter;
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
     * @param configuration the configuration
     * @param listenerInfoList the listener information
     */
    public void printServerStartup(IWebServerConfiguration configuration, List<ListenerInfo> listenerInfoList) {
        System.out.println(prepareServerStartup(configuration, listenerInfoList)); // CHECKSTYLE IGNORE THIS LINE
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
     * @param configuration the configuration
     * @param listenerInfoList the listener information
     * @return the server message
     */
    public String prepareServerStartup(IWebServerConfiguration configuration, List<ListenerInfo> listenerInfoList) {
        final String resourcePath = prepareResourcePath(configuration);
        StringBuilder message = new StringBuilder();
        message.append(NL).append(LINE).append(NL);
        
        preapreTitle(message, configuration);
        prepareListener(message, configuration, listenerInfoList, resourcePath);
        prepareResource(message, configuration, resourcePath);

        if (listenerInfoList != null) { 
            if (configuration.hasHealthCheck()) {
                prepareHeader(message, "Health").append(commandText(configuration.getHealthPath())).append(NL);
            }
            
            if (configuration.hasBasicAuthentication()) {
                prepareHeader(message, "Basic Auth").append("enabled").append(NL);
            }
    
            if (configuration.isDirectoryListingEnabled()) {
                prepareHeader(message, "Listing").append("enabled").append(NL);
            }
        }
        
        message.append(LINE).append(NL);
        return message.toString();
    }

    
    /**
     * Prepare the resource path
     * 
     * @param configuration the configuration
     * @return the prepared resource path
     */
    public String prepareResourcePath(IWebServerConfiguration configuration) {
        String resourcePath = configuration.getResourcePath();
        if (!resourcePath.isEmpty() && !resourcePath.startsWith(ResourceHandler.SLASH)) {
            resourcePath = ResourceHandler.SLASH + resourcePath;
        }

        if (!resourcePath.endsWith(ResourceHandler.SLASH)) { 
            resourcePath += ResourceHandler.SLASH;
        }
        return resourcePath;
    }

    
    /**
     * Prepare the path
     * 
     * @param configuration the configuration
     * @param resourcePath the resource path
     * @return the message
     */
    private String preparePath(IWebServerConfiguration configuration, String resourcePath) {
        String path = configuration.getDirectory();
        if (path.equals(".")) {
            path = "";
        }

        if (!path.isEmpty() && !path.startsWith(ResourceHandler.SLASH)) {
            path = ResourceHandler.SLASH + path;
        }

        if (!resourcePath.equals(ResourceHandler.SLASH)) {
            path += resourcePath;
        }

        if (!path.endsWith(ResourceHandler.SLASH)) {
            path += ResourceHandler.SLASH;
        }

        if (configuration.isLocalDirectory()) {
            path = System.getProperty("user.dir").replace('\\', '/') + path;
        }
        return path;
    }
    
    
    /**
     * Prepare the title
     * 
     * @param configuration the configuration
     * @param message the message
     * @return the message
     */
    private StringBuilder preapreTitle(StringBuilder message, IWebServerConfiguration configuration) {
        message.append("  ");
        if (configuration.getWebserverName() != null && !configuration.getWebserverName().isBlank()) {
            //title = "" + parameterText(configuration.getWebserverName()) + " (powered by " + APP + ")";
            message.append(parameterText(configuration.getWebserverName())).append(" (powered by ").append(APP).append(")");

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
     * @param configuration the configuration
     * @param listenerInfoList the listener list
     * @param resourcePath the resource path
     * @return the message
     */
    private StringBuilder prepareListener(StringBuilder message, IWebServerConfiguration configuration, List<ListenerInfo> listenerInfoList, String resourcePath) {
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
                            listenerAddress = ResourceHandler.SLASH + configuration.getHostname() + ":" + configuration.getPort();                      
                        }
                    }
                }
                listenerInfoMessage.append(listenerAddress);
                
                if (!ResourceHandler.SLASH.equals(resourcePath)) {
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
     * Prepare resource message
     * 
     * @param configuration the configuration
     * @param resourcePath the resource path
     * @param message the message builder
     * @return the message
     */
    private StringBuilder prepareResource(StringBuilder message, IWebServerConfiguration configuration, final String resourcePath) {
        final String path = preparePath(configuration, resourcePath);
        String pathType = "{PATH}:";
        if (configuration.readFromClasspath()) {
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
