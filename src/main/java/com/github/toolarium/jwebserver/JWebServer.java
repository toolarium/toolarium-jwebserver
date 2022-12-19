/*
 * JWebServer.java
 *
 * Copyright by toolarium, all rights reserved.
 */

package com.github.toolarium.jwebserver;

import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import com.github.toolarium.jwebserver.handler.health.HealthHttpHandler;
import com.github.toolarium.jwebserver.handler.resource.ResourceHandler;
import com.github.toolarium.jwebserver.logger.access.AccessLogHttpHandler;
import com.github.toolarium.jwebserver.logger.access.VerboseLevel;
import com.github.toolarium.jwebserver.logger.ansi.ColoredStackTraceWriter;

import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.Undertow.ListenerInfo;
import io.undertow.server.RoutingHandler;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help;
import picocli.CommandLine.Help.ColorScheme;
import picocli.CommandLine.Option;

 
/**
 * The jwebserver.
 * https://blogs.oracle.com/javamagazine/post/java-18-simple-web-server
 */
@Command(name = "jwebserver", mixinStandardHelpOptions = true, version = "jwebserver v" + Version.VERSION, description = "Small file server.")
public class JWebServer implements Runnable {
    private static final String APP = "jwebserver v" + Version.VERSION;
    private static final String NL = "\n";
    private static final String LINE = "----------------------------------------------------------------------------------------";
    private static final Logger LOG = LoggerFactory.getLogger(JWebServer.class);
    
    @Option(names = { "-b", "--bind" }, paramLabel = "address", description = "The bind address, by default localhost.")
    private String hostname;
    @Option(names = { "-p", "--port" }, paramLabel = "port", description = "The port, by default 8080.")
    private Integer port;
    @Option(names = { "-d", "--directory" }, paramLabel = "directory", description = "The directory, by default working path.")
    private String directory;
    @Option(names = { "-l", "--listing" }, paramLabel = "listing",  description = "Enable directory listing.")
    private Boolean directoryListingEnabled;
    @Option(names = { "--resourcePath" }, paramLabel = "resourcePath", description = "The resource path, by default /.")
    private String resourcePath;
    @Option(names = { "--healthPath" }, paramLabel = "healthPath", defaultValue = "/q/health", description = "The health path, by default /q/health.")
    private String healthPath;    
    @Option(names = { "--basicauth" }, paramLabel = "authentication", description = "The basic authentication: user:password, by default disabled.")
    private String basicAuth;
    @Option(names = { "--verbose" }, paramLabel = "verboseLevel", defaultValue = "INFO", description = "Specify the verbose level: (${COMPLETION-CANDIDATES}), by default INFO.")
    private VerboseLevel verboseLevel;
    @Option(names = { "-v", "--version" }, versionHelp = true, description = "Display version info")
    private boolean versionInfoRequested;
    @Option(names = {"-h", "--help" }, usageHelp = true, description = "Display this help message")
    private boolean usageHelpRequested;

    // define the color schema
    private ColorScheme colorSchema = Help.defaultColorScheme(Help.Ansi.AUTO);
    private WebServerConfiguration configuration;
    private Undertow server;

    
    /**
     * Constructor for JWebServer
     */
    public JWebServer() {
        server = null;
    }


    /**
     * Get the configuration
     *
     * @return the configuration
     */
    public IWebServerConfiguration getConfiguration() {
        if (configuration == null) {
            setConfiguration(new WebServerConfiguration()
                    .readProperties()
                    .setHostname(hostname).setPort(port)
                    .setDirectory(directory).setDirectoryListingEnabled(directoryListingEnabled).setResourcePath(resourcePath)
                    .setBasicAuthentication(basicAuth)
                    .setHealthPath(healthPath)
                    .setVerboseLevel(verboseLevel));
        }
        
        return configuration;
    }

    
    /**
     * Get the configuration
     *
     * @param configuration the configuration
     */
    public void setConfiguration(IWebServerConfiguration configuration) {
        this.configuration = new WebServerConfiguration(configuration);
    }

    
    /**
     * Get the color schema
     * @return the color schema
     */
    private ColorScheme getColorSchmea() {
        return colorSchema;
    }


    /**
     * The main class
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        // try to install jansi
        AnsiConsole.systemInstall();

        // new webserver
        JWebServer jwebServer = new JWebServer();
        
        // parse command line and run
        CommandLine commandLine = new CommandLine(jwebServer).setColorScheme(jwebServer.getColorSchmea());        
        int exitCode = commandLine.execute(args);
        LOG.debug("Ended with code:" + exitCode);
        
        // try to uninstall jansi
        AnsiConsole.systemUninstall();
    }


    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        IWebServerConfiguration configuration = getConfiguration();

        try {
            LOG.info("Start server [" + configuration.getHostname() + "] on port [" + configuration.getPort() + "]...");

            // create routing
            RoutingHandler routingHandler = Handlers.routing();
           // TODO: routingHandler.setFallbackHandler(RoutingHandlers::notFoundHandler);
            
            // add routes
            HealthHttpHandler.addHandler(configuration, routingHandler);
            ResourceHandler.addHandler(configuration, routingHandler);
            
            // create simple server
            server = Undertow.builder()
                    //.setIoThreads(configuration.getIoThreads())
                    //.setWorkerThreads(configuration.getWorkerThreads())
                    .addHttpListener(configuration.getPort(), configuration.getHostname(), AccessLogHttpHandler.addHandler(configuration, routingHandler))
                   .build();
            server.start();
            
            if (!VerboseLevel.NONE.equals(verboseLevel)) {
                System.out.println(getServerMessage(server, configuration)); // CHECKSTYLE IGNORE THIS LINE
            }
        } catch (RuntimeException ex) {
            LOG.warn("Could not start server [" + hostname + "] on port [" + port + "]\n" + ColoredStackTraceWriter.throwableToColorString(ex, colorSchema));
        }
    }
    
    
    /**
     * Stop the server
     */
    public void stop() {
        server.stop();
    }
    
    
    /**
     * Get server message
     *
     * @param server the server
     * @param configuration the configuration
     * @return the server message
     */
    private String getServerMessage(Undertow server, IWebServerConfiguration configuration) {
        String path = configuration.getDirectory();
        if (path.equals(".")) {
            path = "";
        }

        if (!path.isEmpty() && !path.startsWith("/")) { 
            path = "/" + path;
        }
        
        if (!path.endsWith("/")) { 
            path += "/";
        }

        if (configuration.isLocalDirectory()) {
            path = System.getProperty("user.dir").replace('\\', '/') + path;
        }

        String pathType = "{PATH}:";
        if (configuration.readFromClasspath()) {
            pathType = " {CLASSPATH}:";
        }

        path = colorSchema.commandText(configuration.getResourcePath()) + " -> " + pathType + colorSchema.commandText(path);
        
        
        StringBuilder listenerInfoMessage = new StringBuilder(); 
        for (ListenerInfo listenerInfo : server.getListenerInfo()) {
            if (listenerInfo.getSslContext() == null) {
                listenerInfoMessage.append(listenerInfo.getProtcol());
            } else {
                listenerInfoMessage.append(listenerInfo.getSslContext().getProtocol());
            }
            listenerInfoMessage.append(":/");
            listenerInfoMessage.append(listenerInfo.getAddress());
            listenerInfoMessage.append(NL);
        }

        String jwebserverMessage = "  " + colorSchema.parameterText(APP) /*Ansi.AUTO.string("@|bold,blue " + APP + "!|@")*/ + NL
                + prepareHeader("Listener") + colorSchema.commandText(listenerInfoMessage.toString())
                + prepareHeader("Resource") + path + NL
                + prepareHeader("Listing") + configuration.isDirectoryListingEnabled() + NL;

        if (configuration.hasBasicAuthentication()) {
            jwebserverMessage += prepareHeader("Basic Auth") + configuration.hasBasicAuthentication() + NL;
        }

        if (configuration.hasHealthCheck()) {
            jwebserverMessage += prepareHeader("Health") + configuration.getHealthPath() + NL;
        }
        
        final String headerLine = LINE + NL; 
        return NL + headerLine + jwebserverMessage + headerLine;
    }


    /**
     * Prepare header
     *
     * @param tag the tag
     * @return the message
     */
    private String prepareHeader(String tag) {
        StringBuilder msg = new StringBuilder(); 
        msg.append("  > ");
        msg.append(tag);
        for (int i = tag.length(); i < 11; i++) {
            msg.append(' ');
        }
        return msg.toString();
    }
}
