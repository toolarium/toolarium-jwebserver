/*
 * ColoredStackTraceWriter.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.logger.ansi;

import java.io.StringWriter;
import java.util.List;
import picocli.CommandLine.Help;
import picocli.CommandLine.Help.Ansi.IStyle;


/**
 * Implemends a colored stack trace writer
 * 
 * @author patrick
 */
public class ColoredStackTraceWriter extends StringWriter {
    private Help.ColorScheme colorScheme;

    /**
     * Constructor for ColoredStackTraceWriter
     *
     * @param colorScheme the color schema
     */
    public ColoredStackTraceWriter(Help.ColorScheme colorScheme) {
        this.colorScheme = colorScheme;
    }

    
    /**
     * @see java.io.StringWriter#write(java.lang.String, int, int)
     */
    @Override
    public void write(String str, int off, int len) {
        
        final List<IStyle> styles;
        if (str.startsWith("\t")) {
            styles = colorScheme.stackTraceStyles();
        } else {
            styles = colorScheme.errorStyles();
        }
        
        super.write(colorScheme.apply(str.substring(off, len), styles).toString());
    }
}

