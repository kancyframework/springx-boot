package com.kancy.jevel;

import com.github.kancyframework.springx.boot.ApplicationRunner;
import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.context.annotation.Component;

/**
 * EvelRunner
 *
 * @author huangchengkang
 * @date 2021/9/18 0:23
 */
@Component
public class EvelRunner implements ApplicationRunner {
    /**
     * run
     *
     * @param args
     */
    @Override
    public void run(CommandLineArgument args) {
        System.out.println(args);
    }
}
