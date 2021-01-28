package com.github.kancyframework.springx.log;

/**
 * Log
 *
 * @author kancy
 * @date 2020/2/16 1:58
 */
public class Log {
    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    /**
     * debug
     * @param msgFormat
     * @param args
     */
    public static void debug(String msgFormat, Object... args){
        logger.debug(msgFormat, args);
    }

    /**
     * info
     * @param msgFormat
     * @param args
     */
    public static void info(String msgFormat, Object... args){
        logger.info(msgFormat, args);
    }

    /**
     * warn
     * 不提供外部使用
     * @param msgFormat
     * @param args
     */
    public static void warn(String msgFormat, Object... args){
        logger.warn(msgFormat, args);
    }


    /**
     * error
     * @param msgFormat
     * @param args
     */
    public static void error(String msgFormat, Object... args){
        logger.error(msgFormat, args);
    }

    /**
     * error
     * @param msg
     * @param e
     */
    public static void error(String msg, Exception e){
        logger.error(msg, e);
    }

    /**
     * 设置日志级别
     * @param logLevel
     */
    public static void setLogLevel(LogLevel logLevel) {
        SimpleLogger.setLogLevel(logLevel);
    }
}
