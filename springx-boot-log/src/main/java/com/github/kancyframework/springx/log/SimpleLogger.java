package com.github.kancyframework.springx.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * Logger
 *
 * @author kancy
 * @date 2020/2/18 5:07
 */
class SimpleLogger implements Logger {
    private static LogLevel logLevel = LogLevel.INFO;

    static {
        loadLogLevel();
    }

    private LogFormatter logFormatter;

    SimpleLogger(String logName) {
        this.logFormatter = new LogFormatter(logName);
    }

    /**
     * debug
     * @param msgFormat
     * @param args
     */
    @Override
    public void debug(String msgFormat, Object... args){
        log(msgFormat, args, LogLevel.DEBUG);
    }

    /**
     * info
     * @param msgFormat
     * @param args
     */
    @Override
    public void info(String msgFormat, Object... args){
        log(msgFormat, args, LogLevel.INFO);
    }

    /**
     * warn
     * 不提供外部使用
     * @param msgFormat
     * @param args
     */
    @Override
    public void warn(String msgFormat, Object... args){
        log(msgFormat, args, LogLevel.WARN);
    }


    /**
     * error
     * @param msgFormat
     * @param args
     */
    @Override
    public void error(String msgFormat, Object... args){
        log(msgFormat, args, LogLevel.ERROR);
    }

    /**
     * error
     * @param msg
     * @param e
     */
    @Override
    public void error(String msg, Exception e){
        String msgFormat = String.format("%s : {}", msg);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        log(msgFormat, new Object[]{sw.toString()}, LogLevel.ERROR);
    }

    /**
     * 打印日志
     * @param msgFormat
     * @param args
     * @param logLevel
     */
    private void log(String msgFormat, Object[] args, LogLevel logLevel) {
        if (Objects.nonNull(msgFormat) && canLog(logLevel)) {
            msgFormat = msgFormat.replace("{}", "%s");
            String msg = String.format(msgFormat, args);
            if (Objects.equals(LogLevel.ERROR , logLevel)){
                System.err.println(logFormatter.format(msg, logLevel));
            } else {
                System.out.println(logFormatter.format(msg, logLevel));
            }
        }
    }

    /**
     * 是否可以打印日志
     * @param logLevel
     * @return
     */
    private static boolean canLog(LogLevel logLevel) {
        if (Objects.nonNull(SimpleLogger.logLevel)){
            return logLevel.getLevel() >= SimpleLogger.logLevel.getLevel();
        }
        return false;
    }

    /**
     * 加载日志级别
     */
    private static void loadLogLevel() {
        String propertyName = "log.level";
        String logLevelName = System.getProperty(propertyName, System.getenv(propertyName));
        if (Objects.isNull(logLevelName) || logLevelName.isEmpty()){
            logLevelName = "INFO";
        }
        setLogLevel(LogLevel.valueOf(logLevelName.toUpperCase()));
    }

    /**
     * 设置日志级别
     * @param logLevel
     */
    public static void setLogLevel(LogLevel logLevel) {
        SimpleLogger.logLevel = logLevel;
    }

    public static LogLevel getLogLevel(){
        return logLevel;
    }

}
