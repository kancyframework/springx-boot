package com.github.kancyframework.springx.demo.hook;

import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.boot.ShutdownHook;
import com.github.kancyframework.springx.context.annotation.Component;

@Component
public class MyShutdownHook implements ShutdownHook {
    @Override
    public void run(CommandLineArgument args) {

    }
}
