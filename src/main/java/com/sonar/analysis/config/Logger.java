package com.sonar.analysis.config;


import org.apache.logging.log4j.LogManager;

public class Logger {
    private Logger() {
    }

    public static org.apache.logging.log4j.Logger getLogger(){
        return LogManager.getLogger(Logger.class);
    }
}
