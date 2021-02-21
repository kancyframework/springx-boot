package com.github.kancyframework.springx.boot;


/**
 * ApplicationInitializer
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public interface ApplicationInitializer {
    /**
     * run
     * @param args
     * @throws Exception
     */
    void run(CommandLineArgument args) throws Exception;
}
