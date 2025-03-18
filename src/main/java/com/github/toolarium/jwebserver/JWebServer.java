/*
 * JWebServer.java
 *
 * Copyright by toolarium, all rights reserved.
 */

package com.github.toolarium.jwebserver;
import com.github.toolarium.jwebserver.config.IWebServerConfiguration;
import com.github.toolarium.jwebserver.config.WebServerConfiguration;
import com.github.toolarium.jwebserver.handler.health.HealthHttpHandler;
import com.github.toolarium.jwebserver.handler.routing.RoutingHandler;
import com.github.toolarium.jwebserver.logger.LifecycleLogger;
import com.github.toolarium.jwebserver.logger.VerboseLevel;
import com.github.toolarium.jwebserver.logger.access.AccessLogHttpHandler;
import com.github.toolarium.jwebserver.logger.logback.LogbackUtil;
import com.github.toolarium.jwebserver.util.ConfigurationUtil;
import io.undertow.Handlers;
import io.undertow.Undertow;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import org.fusesource.jansi.AnsiConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Help.ColorScheme;
import picocli.CommandLine.Option;

 
/**
 * The jwebserver.
 * https://blogs.oracle.com/javamagazine/post/java-18-simple-web-server
 */
@Command(name = "jwebserver", mixinStandardHelpOptions = true, version = "jwebserver v" + Version.VERSION, description = "Small file server.")
public class JWebServer implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(JWebServer.class);
    
    @Option(names = { "-b", "--bind" }, paramLabel = "address", description = "The bind address, by default 0.0.0.0.")
    private String hostname;
    @Option(names = { "-p", "--port" }, paramLabel = "port", description = "The port, by default 8080.")
    private Integer port;
    @Option(names = { "-s", "--securePort" }, paramLabel = "securePort", description = "The secure port.")
    private Integer securePort;
    @Option(names = { "--resourcePath" }, paramLabel = "resourcePath", description = "The resource path, by default /.")
    private String resourcePath;
    @Option(names = { "--healthPath" }, paramLabel = "healthPath", defaultValue = "/q/health", description = "The health path, by default /q/health.")
    private String healthPath;    
    @Option(names = { "--basicauth" }, paramLabel = "authentication", description = "The basic authentication: user:password, by default disabled.")
    private String basicAuth;
    @Option(names = { "--ioThreads" }, paramLabel = "ioThreads", description = "The number of I/O threads.")
    private Integer ioThreads;
    @Option(names = { "--workerThreads" }, paramLabel = "workerThreads", description = "The number of worker threads.")
    private Integer workerThreads;
    @Option(names = { "--name" }, paramLabel = "webserverName", defaultValue = "", description = "The webserver name.")
    private String webserverName;    
    @Option(names = { "--accessLogFormat" }, paramLabel = "accessLogFormat", description = "Defines the access log format, default: combined.")
    private String accessLogFormatString;
    @Option(names = { "--accessLogFilePattern" }, paramLabel = "accessLogFilePattern", description = "Defines the access log file pattern, default: logs/access-%%d{yyyy-MM-dd}.log.gz.")
    private String accessLogFilePattern;

    @Option(names = { "-d", "--directory" }, paramLabel = "directory", description = "The directory, by default working path.")
    private String directory;
    @Option(names = { "-l", "--listing" }, paramLabel = "listing",  description = "Enable directory listing.")
    private Boolean directoryListingEnabled;
    @Option(names = { "--welcomeFiles" }, paramLabel = "welcomeFiles", description = "The welcome files, by default index.html, index.htm.")
    private String welcomeFiles;
    @Option(names = { "--disableResolveParentResourceIfNotFound" }, paramLabel = "disableResolveParentResourceIfNotFound",  description = "Disable the resolution of parent resources if the requested resource can' be found.")    
    private Boolean disableResolveParentResourceIfNotFound;
    
    @Option(names = { "--trustAll" }, paramLabel = "trustAnyCertificate", description = "Define to trust any certificate, default false")
    private Boolean trustAnyCertificate;
    
    // proxy
    @Option(names = { "--proxy" }, paramLabel = "proxyHost", description = "Defines the comma-separated url list in which this instance acts as a proxy.")
    private String proxyHostNameList;
    //@Option(names = { "--rewriteHostHeader" }, paramLabel = "rewriteHostHeader", description = "Defines if the host header should be re written, default true")
    //private Boolean rewriteHostHeader;
    //@Option(names = { "--reuseXForwarded" }, paramLabel = "reuseXForwarded", description = "Defines if the X-Forwarded headers should be re written, default true")
    //private Boolean reuseXForwarded;
    @Option(names = { "--maxRequestTime" }, paramLabel = "maxRequestTime", description = "Defines the max request time, default 30000")
    private Integer maxRequestTime;
    @Option(names = { "--connectionsPerThread" }, paramLabel = "connectionsPerThread", description = "Defines the connections per thread, default 20.")
    private Integer connectionsPerThread;
    
    @Option(names = { "--verbose" }, paramLabel = "verboseLevel", defaultValue = "INFO", description = "Specify the verbose level: (${COMPLETION-CANDIDATES}), by default INFO.")
    private VerboseLevel verboseLevel;
    @Option(names = { "-v", "--version" }, versionHelp = true, description = "Display version info")
    private boolean versionInfoRequested;
    @Option(names = {"-h", "--help" }, usageHelp = true, description = "Display this help message")
    private boolean usageHelpRequested;

    private WebServerConfiguration configuration;
    private LifecycleLogger lifecycleLogger;
    private transient Undertow server;
    private boolean hasError;
    

    /**
     * Constructor for JWebServer
     */
    public JWebServer() {
        configuration = null;
        lifecycleLogger = new LifecycleLogger();
        server = null;
        hasError = false;
    }


    /**
     * Get the configuration
     *
     * @return the configuration
     */
    public IWebServerConfiguration getConfiguration() {
        if (configuration == null) {
            if (port == null && securePort == null) {
                port = 8080;
            }
            
            WebServerConfiguration webServerConfiguration = new WebServerConfiguration()
                    .readProperties()
                    .setWebserverName(webserverName)
                    .setHostname(hostname).setPort(port).setSecurePort(securePort)
                    .setResourcePath(resourcePath)
                    .setBasicAuthentication(basicAuth)
                    .setHealthPath(healthPath)
                    .setIoThreads(ioThreads).setWorkerThreads(workerThreads)
                    .setVerboseLevel(verboseLevel).setAccessLogFilePattern(accessLogFilePattern).setAccessLogFormatString(accessLogFormatString);

            // SSL configuration
            webServerConfiguration.getSSLServerConfiguration().setTrustAnyCertificate(trustAnyCertificate);
            
            Boolean resolveParentResourceIfNotFound = null;
            if (disableResolveParentResourceIfNotFound != null && disableResolveParentResourceIfNotFound.booleanValue()) {
                resolveParentResourceIfNotFound = Boolean.FALSE;
            }
            
            // resource configuration
            webServerConfiguration.getResourceServerConfiguration()
                    .setDirectory(directory)
                    .setDirectoryListingEnabled(directoryListingEnabled)
                    .setResolveParentResourceIfNotFound(resolveParentResourceIfNotFound)
                    .setWelcomeFiles(welcomeFiles);

            // proxy configuration
            webServerConfiguration.getProxyServerConfiguration()
                    //.setRewriteHostHeader(rewriteHostHeader)
                    //.setReuseXForwarded(reuseXForwarded)
                    .setMaxRequestTime(maxRequestTime)
                    .setConnectionsPerThread(connectionsPerThread)
                    .setProxyHostNames(proxyHostNameList);

            setConfiguration(webServerConfiguration);
        }
        
        return configuration;
    }

    
    /**
     * Get the configuration
     *
     * @param webServerConfiguration the web server configuration
     */
    public void setConfiguration(IWebServerConfiguration webServerConfiguration) {
        this.configuration = new WebServerConfiguration(webServerConfiguration);
    }

    
    /**
     * Get the color schema
     * 
     * @return the color schema
     */
    private ColorScheme getColorSchmea() {
        return lifecycleLogger.getColorScheme();
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
        CommandLine commandLine = new CommandLine(jwebServer).setColorScheme(jwebServer.getColorSchmea())
            .registerConverter(String.class, s -> ConfigurationUtil.getInstance().expand(s))
            .registerConverter(Integer.class, s -> ConfigurationUtil.getInstance().convert(null, s, (Integer)null))
            .registerConverter(Boolean.class, s -> ConfigurationUtil.getInstance().convert(null, s, (Boolean)null))
            .registerConverter(VerboseLevel.class, s -> ConfigurationUtil.getInstance().convert(null, s, (VerboseLevel)null));
        
        int exitCode = commandLine.execute(args);
        if (jwebServer.hasError()) {
            LOG.debug("Executed Ended with code:" + exitCode);
        } else {
            LOG.debug("Successful started.");
        }
        
        // try to uninstall jansi
        AnsiConsole.systemUninstall();
    }

    
    /**
     * Stop the server
     */
    public synchronized void start() {
        if (!isRunning()) {
            run();
        } else {
            LOG.warn("Server is already running!");
        }
    }

    
    /**
     * Stop the server
     */
    public synchronized void stop() {
        if (isRunning()) {
            server.stop();
            server = null;
        } else {
            LOG.warn("Server is already stopped.");
        }
    }


    /**
     * Check if the server is running
     *
     * @return true if it is running
     */
    public boolean isRunning() {
        return (server != null);
    }

    
    /**
     * Check if there are any errors
     *
     * @return true if there are any errors
     */
    public boolean hasError() {
        return hasError;
    }

    
    /**
     * @see java.lang.Runnable#run()
     */
    @Override
    public synchronized void run() {
        
        if (verboseLevel != null && VerboseLevel.VERBOSE.equals(verboseLevel)) {
            LogbackUtil.getInstance().enableVerbose();
        }
        
        IWebServerConfiguration webServerConfiguration = getConfiguration();
        
        try {
            LOG.info("Start server [" + webServerConfiguration.getHostname() + "] on port [" + webServerConfiguration.getPort() + "]...");

            // create routing
            io.undertow.server.RoutingHandler routingHandler = Handlers.routing();
            // TODO: routingHandler.setFallbackHandler(RoutingHandlers::notFoundHandler);
            
            // add routes
            HealthHttpHandler.addHandler(webServerConfiguration, routingHandler);
            RoutingHandler.addHandler(webServerConfiguration, routingHandler);
            
            // create ssl context with added self-signed certificate in trust store for a SSL client
            
            // create simple server
            Undertow.Builder builder = Undertow.builder()
                    .setIoThreads(webServerConfiguration.getIoThreads()).setWorkerThreads(webServerConfiguration.getWorkerThreads());
            
            // set port
            if (webServerConfiguration.getPort() != null) {
                builder.addHttpListener(webServerConfiguration.getPort(), webServerConfiguration.getHostname(), AccessLogHttpHandler.addHandler(webServerConfiguration, routingHandler));
            }
            
            // set ssl port
            if (webServerConfiguration.getSecurePort() != null) {
                try {
                    SSLContext sslContext = webServerConfiguration.getSSLServerConfiguration().getSSLContext();
                    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                    builder.addHttpsListener(webServerConfiguration.getSecurePort(), webServerConfiguration.getHostname(), sslContext, AccessLogHttpHandler.addHandler(webServerConfiguration, routingHandler));
                } catch (Exception e) {
                    if (!VerboseLevel.NONE.equals(verboseLevel)) {
                        lifecycleLogger.printServerStartup(webServerConfiguration, null);
                    }
                    LOG.warn("Could not get SSL context [" + webServerConfiguration.getHostname() + "] on port [" + webServerConfiguration.getSecurePort() + "]\n" + lifecycleLogger.preapreThrowable(e));
                }
            }
            
            server = builder.build();
            server.start();
            
            if (!VerboseLevel.NONE.equals(verboseLevel)) {
                lifecycleLogger.printServerStartup(webServerConfiguration, server.getListenerInfo());
            }
        } catch (RuntimeException ex) {
            hasError = true;
            if (!VerboseLevel.NONE.equals(verboseLevel)) {
                lifecycleLogger.printServerStartup(webServerConfiguration, null);
            }
            int port;
            if (webServerConfiguration.getPort() != null) {
                port = webServerConfiguration.getPort();
            } else {
                port = webServerConfiguration.getSecurePort();
            }
            LOG.warn("Could not start server [" + webServerConfiguration.getHostname() + "] on port [" + port + "]\n" + lifecycleLogger.preapreThrowable(ex));
        }
    }
}
