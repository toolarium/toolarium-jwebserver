/*
 * LogbackTest.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver;

import com.github.toolarium.jwebserver.logger.logback.LogbackUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;


/**
 * Test Logback
 *  
 * @author patrick
 */
public class LogbackTest {
    
    /**
     * Simple lofg test
     *
     * @throws InterruptedException In case of interrupt
     */
    @Test
    public void testAccessLog() throws InterruptedException {
        Logger log = LogbackUtil.getInstance().createAccessLogAppender("accesslog", "build/logs/logfile-%d{yyyy-MM-dd_HHmmss}.log.gz");
        log.info("TEST1");
        Thread.sleep(1000);
        log.info("TEST2");
        Thread.sleep(1000);
        log.info("TEST3");
        LogbackUtil.getInstance().detachAppender("accesslog");
        Thread.sleep(1000);
        log.info("TEST4");
    }
}
