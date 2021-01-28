package com.github.kancyframework.springx.log;

/**
 * Logger
 *
 * @author kancy
 * @date 2020/2/18 6:23
 */
public interface Logger {
    /**
     * debug
     * @param msgFormat
     * @param args
     */
    void debug(String msgFormat, Object... args);

    /**
     * info
     * @param msgFormat
     * @param args
     */
    void info(String msgFormat, Object... args);

    /**
     * warn
     * 不提供外部使用
     * @param msgFormat
     * @param args
     */
    void warn(String msgFormat, Object... args);


    /**
     * error
     * @param msgFormat
     * @param args
     */
    void error(String msgFormat, Object... args);

    /**
     * error
     * @param msg
     * @param e
     */
    void error(String msg, Exception e);

}
