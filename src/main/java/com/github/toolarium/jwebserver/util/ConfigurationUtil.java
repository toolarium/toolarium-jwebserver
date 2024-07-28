/*
 * ConfigurationUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.util;

/**
 * The configuration util
 * 
 * @author patrick
 */
public final class ConfigurationUtil {

    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static class HOLDER {
        static final ConfigurationUtil INSTANCE = new ConfigurationUtil();
    }

    
    /**
     * Constructor
     */
    private ConfigurationUtil() {
        // NOP
    }

    
    /**
     * Get the instance
     *
     * @return the instance
     */
    public static ConfigurationUtil getInstance() {
        return HOLDER.INSTANCE;
    }

    
    /**
     * Parse the string array
     *
     * @param stringArray the string array
     * @return the parsed string
     */
    public String[] parseStringArray(String stringArray) {
        if (stringArray == null || stringArray.isBlank()) {
            return null;
        }
            
        String[] stringArrayList = stringArray.split(",");
        for (int i = 0; i < stringArrayList.length; i++) {
            stringArrayList[i] = stringArrayList[i].trim();
        }
        return stringArrayList;
    }

    
    /**
     * Format an array as string
     *
     * @param stringArray the welcome files
     * @return the formated string
     */
    public String formatArrayAsString(String[] stringArray) {
        if (stringArray == null) {
            return null;
        }
        
        StringBuilder formatString = new StringBuilder();
        for (String welcomeFile : stringArray) {
            if (!formatString.toString().isEmpty()) {
                formatString.append(", ");
            }
            formatString.append(welcomeFile);
        }
        return formatString.toString();
    }
}
