/*
 * ConfigurationUtil.java
 *
 * Copyright by toolarium, all rights reserved.
 */
package com.github.toolarium.jwebserver.util;

import com.github.toolarium.common.security.ISecuredValue;
import com.github.toolarium.common.security.SecuredValue;
import com.github.toolarium.common.util.PropertyExpander;
import com.github.toolarium.jwebserver.logger.VerboseLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The configuration util
 * 
 * @author patrick
 */
public final class ConfigurationUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationUtil.class);
    private static final String END_VALUE = "].";


    /**
     * Private class, the only instance of the singelton which will be created by accessing the holder class.
     *
     * @author patrick
     */
    private static final class HOLDER {
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
     * Expand a string and replace environment variables or system properties
     *
     * @param value the value
     * @return the expanded value
     */
    public String expand(String value) {
        if (value == null) {
            return value;
        }
        
        final String result = PropertyExpander.getInstance().expand(value);
        if (!value.equals(result)) {
            LOG.debug("Resolved value [" + value + "] to [" + result + "].  ");
        }
        
        return result;
    }


    /**
     * Expand a string and replace environment variables or system properties
     *
     * @param valueList the value list
     * @return the expanded value list
     */
    public String[] expand(String[] valueList) {
        if (valueList == null || valueList.length == 0) {
            return valueList;
        }
        
        String[] expandedList = new String[valueList.length];
        for (int i = 0; i < expandedList.length; i++) {
            expandedList[i] = expand(valueList[i]);
        }

        return expandedList;
    }
    

    /**
     * Convert integer
     *
     * @param name the attribute name
     * @param value the value
     * @param defaultValue the default value
     * @return the value
     */
    public Integer convert(String name, String value, Integer defaultValue) {
        try {
            return Integer.valueOf(expand(expand(value)));
        } catch (Exception e) {
            String nameMsg = ""; 
            if (name != null) {
                nameMsg = "for attribute [" + name + "]";
            }
            LOG.warn("Invalid value [" + value + "] " + nameMsg + ", keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
    }

    
    /**
     * Convert boolean
     *
     * @param name the attribute name
     * @param value the value
     * @param defaultValue the default value
     * @return the value
     */
    public Boolean convert(String name, String value, Boolean defaultValue) {
        try {
            return Boolean.valueOf(expand(value));
        } catch (Exception e) {
            String nameMsg = ""; 
            if (name != null) {
                nameMsg = "for attribute [" + name + "]";
            }
            LOG.warn("Invalid value [" + value + "] " + nameMsg + ", keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
    }


    /**
     * Convert verbose level
     *
     * @param name the attribute name
     * @param value the value
     * @param defaultValue the default value
     * @return the value
     */
    public VerboseLevel convert(String name, String value, VerboseLevel defaultValue) {
        try {
            return VerboseLevel.valueOf(expand(value));
        } catch (Exception e) {
            String nameMsg = ""; 
            if (name != null) {
                nameMsg = "for attribute [" + name + "]";
            }
            LOG.warn("Invalid value [" + value + "] " + nameMsg + ", keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
    }

    
    /**
     * Convert secured value
     *
     * @param name the attribute name
     * @param value the value
     * @param defaultValue the default value
     * @return the value
     */
    public ISecuredValue<String> convert(String name, String value, ISecuredValue<String> defaultValue) {
        try {
            return new SecuredValue<String>(expand(value));
        } catch (Exception e) {
            String nameMsg = ""; 
            if (name != null) {
                nameMsg = "for attribute [" + name + "]";
            }
            LOG.warn("Invalid value [" + value + "] " + nameMsg + ", keep default value [" + defaultValue + END_VALUE);
            return defaultValue;
        }
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
