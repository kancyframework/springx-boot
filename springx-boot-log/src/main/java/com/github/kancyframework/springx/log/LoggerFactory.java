package com.github.kancyframework.springx.log;

/**
 * LogFactory
 *
 * @author kancy
 * @date 2020/2/18 3:38
 */
public class LoggerFactory {
    private static final Logger log = new SimpleLogger(Log.class.getName());

    public static Logger getLogger(){
        return log;
    }

    public static Logger getLogger(Class clazz){
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String className){
        return new SimpleLogger(className);
    }

    public static void setLogLevel(LogLevel logLevel){
        SimpleLogger.setLogLevel(logLevel);
    }

    public static void setLogLevel(String logLevelName){
        SimpleLogger.setLogLevel(LogLevel.valueOf(logLevelName.toUpperCase()));
    }

    public static LogLevel getLogLevel(){
        return SimpleLogger.getLogLevel();
    }
}
