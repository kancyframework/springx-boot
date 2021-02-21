package com.github.kancyframework.springx.boot;

/**
 * Jvm ShutdownHook
 */
public interface ShutdownHook {

    void run(CommandLineArgument args);
}
