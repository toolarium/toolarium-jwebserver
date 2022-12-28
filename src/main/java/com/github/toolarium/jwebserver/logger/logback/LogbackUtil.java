/*
 * LogbackUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.logger.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
//import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Logback utitlity
 * 
 * @author patrick
 */
public final class LogbackUtil {
    private static final Logger LOG = LoggerFactory.getLogger(LogbackUtil.class);
    private boolean enableVerbose;
    
    
    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final LogbackUtil INSTANCE = new LogbackUtil();
    }

    /**
     * Constructor
     */
    private LogbackUtil() {
        enableVerbose = false;
    }

    /**
     * Get the instance
     *
     * @return the instance
     */
    public static LogbackUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Enable verbose mode
     */
    public void enableVerbose() { 
        if (!enableVerbose) {
            String filePattern = "logs/jwebserver-%d{yyyy-MM-dd}.log.gz";
            
            ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) 
                LogbackUtil.getInstance().createLogAppender("com.github.toolarium.jwebserver", "%-30(%d{ISO8601} - %1.-1level - %-6.6t{5}) - %-100.100(%logger#%M:%L){99} | %msg%n", filePattern, null, true, (FileSize) null, (FileSize) null);
            log.setAdditive(true);
            log.setLevel(Level.DEBUG);
            enableVerbose = true;
        }
    }
    
    
    /**
     * Detach appender
     *
     * @param appenderName the appender name
     */
    public void detachAppender(String appenderName) {
        Logger log = LoggerFactory.getLogger(appenderName);
        if (log instanceof ch.qos.logback.classic.Logger) {
            Appender<ILoggingEvent> appender = ((ch.qos.logback.classic.Logger)log).getAppender(appenderName);
            if (appender != null) {
                if (((ch.qos.logback.classic.Logger)log).detachAppender(appenderName)) {
                    LOG.info("Successful detached appender [" + appenderName  + "].");
                }
                
                if (appender instanceof RollingFileAppender) {
                    ((RollingFileAppender<ILoggingEvent>)appender).getEncoder().stop();
                    ((RollingFileAppender<ILoggingEvent>)appender).getRollingPolicy().stop();
                }
                
                appender.stop();
            }
        } else {
            log.info("Could not detach appender [" + appenderName + "].");
        }
    }

    
    /**
     * Create logger with an own appender
     *
     * @param appenderName the name of the appender
     * @param filePattern the file pattern, e.g. "logs/access-%d{yyyy-MM-dd}.log.gz"
     * @return the logger
     */
    public Logger createAccessLogAppender(String appenderName, String filePattern) {
        return createAccessLogAppender(appenderName, filePattern, null, null, null);
    }

    
    /**
     * Create logger with an own appender
     *
     * @param appenderName the name of the appender
     * @param filePattern the file pattern, e.g. "logs/access-%d{yyyy-MM-dd}.log.gz"
     * @param maxHistory the max history
     * @param inputMaxFileSize the max file size, e.g. 100MB
     * @param inputTotalSize the total size, e.g. 100GB
     * @return the logger
     */
    public Logger createAccessLogAppender(String appenderName, String filePattern, Integer maxHistory, String inputMaxFileSize, String inputTotalSize) {
        FileSize maxFileSize = null;
        if (inputMaxFileSize != null) {
            maxFileSize = FileSize.valueOf(inputMaxFileSize);
        }
        FileSize totalSize = null;
        if (inputTotalSize != null) {
            totalSize = FileSize.valueOf(inputTotalSize);
        }
        return createLogAppender(appenderName, "%msg%n", filePattern, maxHistory, true, maxFileSize, totalSize);
    }

    
    /**
     * Create logger with an own appender
     * 
     * @param appenderName the name of the appender
     * @param linePattern the log line pattern, e.g. %-30(%d{ISO8601} - %1.-1level - %-6.6t{5}) - %-100.100(%logger#%M:%L){99} | %msg%n
     * @param filePattern the file pattern, e.g. "%d{yyyy-MM-dd}.%i.log"
     * @param maxHistory the max history
     * @param appendFile true to append file
     * @param inputMaxFileSize the max file size, e.g. 100MB
     * @param inputTotalSize the total size, e.g. 100GB
     * @return the logger
     */
    public Logger createLogAppender(String appenderName, String linePattern, String filePattern, Integer maxHistory, boolean appendFile, String inputMaxFileSize, String inputTotalSize) {
        FileSize maxFileSize = null;
        if (inputMaxFileSize != null) {
            maxFileSize = FileSize.valueOf(inputMaxFileSize);
        }
        FileSize totalSize = null;
        if (inputTotalSize != null) {
            totalSize = FileSize.valueOf(inputTotalSize);
        }
        return createLogAppender(appenderName, linePattern, filePattern, maxHistory, appendFile, maxFileSize, totalSize);
    }
    
    
    /**
     * Create logger with an own appender
     *
     * @param appenderName the name of the appender
     * @param linePattern the log line pattern, e.g. %-30(%d{ISO8601} - %1.-1level - %-6.6t{5}) - %-100.100(%logger#%M:%L){99} | %msg%n
     * @param filePattern the file pattern, e.g. "%d{yyyy-MM-dd}.%i.log"
     * @param maxHistory the max history
     * @param appendFile true to append file
     * @param maxFileSize the max file size
     * @param totalSize the total size
     * @return the logger
     */
    public Logger createLogAppender(String appenderName, String linePattern, String filePattern, Integer maxHistory, boolean appendFile, FileSize maxFileSize, FileSize totalSize) {
        LoggerContext logCtx = (LoggerContext)LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(logCtx);
        logEncoder.setPattern(linePattern);
        logEncoder.start();

        /*
        ConsoleAppender<ILoggingEvent>logConsoleAppender = new ConsoleAppender();
        logConsoleAppender.setContext(logCtx);
        logConsoleAppender.setName("console");
        logConsoleAppender.setEncoder(logEncoder);
        logConsoleAppender.start();
        */


        logEncoder = new PatternLayoutEncoder();
        logEncoder.setContext(logCtx);
        logEncoder.setPattern(linePattern);
        logEncoder.setCharset(StandardCharsets.UTF_8);
        logEncoder.start();

        RollingFileAppender<ILoggingEvent> logFileAppender = new RollingFileAppender<>();
        logFileAppender.setContext(logCtx);
        logFileAppender.setName(appenderName);
        logFileAppender.setEncoder(logEncoder);
        logFileAppender.setAppend(appendFile);
        //logFileAppender.setFile(path + filename + filenameExtension);

        RollingPolicy logFilePolicy;
        if (maxFileSize != null) {
            SizeAndTimeBasedRollingPolicy<ILoggingEvent> sizeAndTimeBasedRollingPolicy = new SizeAndTimeBasedRollingPolicy<>();
            sizeAndTimeBasedRollingPolicy.setContext(logCtx);
            sizeAndTimeBasedRollingPolicy.setParent(logFileAppender);
            sizeAndTimeBasedRollingPolicy.setFileNamePattern(filePattern);
            
            if (maxHistory != null) {
                sizeAndTimeBasedRollingPolicy.setMaxHistory(maxHistory);    // e.g. 30 days to keep
            }
                
            if (maxFileSize != null) {
                // 500 MB max size, in case its bigger, it will be split by an additional index
                sizeAndTimeBasedRollingPolicy.setMaxFileSize(maxFileSize); 
            }
            
            if (totalSize != null) {
                sizeAndTimeBasedRollingPolicy.setTotalSizeCap(totalSize);   // total size of all archive files, if total size > 100GB, it will delete old archived file
            }
            
            sizeAndTimeBasedRollingPolicy.start();
            logFilePolicy = sizeAndTimeBasedRollingPolicy;
        } else {
            TimeBasedRollingPolicy<ILoggingEvent> timeBasedRollingPolicy = new TimeBasedRollingPolicy<>();
            timeBasedRollingPolicy.setContext(logCtx);
            timeBasedRollingPolicy.setParent(logFileAppender);
            timeBasedRollingPolicy.setFileNamePattern(filePattern);
            
            if (maxHistory != null) {
                timeBasedRollingPolicy.setMaxHistory(maxHistory);
            }
            
            if (totalSize != null) {
                timeBasedRollingPolicy.setTotalSizeCap(totalSize);   // total size of all archive files, if total size > 100GB, it will delete old archived file
            }

            timeBasedRollingPolicy.start();
            logFilePolicy = timeBasedRollingPolicy;
        }

        logFileAppender.setRollingPolicy(logFilePolicy);
        logFileAppender.start();

        ch.qos.logback.classic.Logger log = logCtx.getLogger(appenderName);
        log.setAdditive(false);
        log.setLevel(Level.INFO);
        //log.addAppender(logConsoleAppender);
        log.addAppender(logFileAppender);
        
        LOG.info("Successful added appender [" + appenderName  + "].");
        return log;
    }
 
    
    /**
     * Get logback logger instance
     * 
     * @param name the logger
     * @return the logger instance
     */
    public ch.qos.logback.classic.Logger getLogger(String name) {
        LoggerContext logCtx = (LoggerContext)LoggerFactory.getILoggerFactory();
        return logCtx.getLogger(name);
    }
}