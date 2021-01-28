package com.github.kancyframework.springx.log;

/**
 * LogLevel
 *
 * @author kancy
 * @date 2020/2/16 2:19
 */
public enum LogLevel {
    DEBUG(0),INFO(1),WARN(2),ERROR(3);

    /**
     * log level value
     */
    private int level;

    /**
     * constructor
     * @param level
     */
    private LogLevel(int level) {
        this.level = level;
    }

    /**
     * get level value
     * @return
     */
    public int getLevel(){
        return this.level;
    }
}
