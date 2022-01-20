package com.kancy.imagebeder.runner;

import com.github.kancyframework.springx.boot.ApplicationRunner;
import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.context.annotation.Async;
import com.github.kancyframework.springx.context.annotation.Component;
import com.kancy.imagebeder.service.Giteer;

import java.io.IOException;

/**
 * InitRunner
 *
 * @author huangchengkang
 * @date 2022/1/21 1:09
 */
@Component
public class InitRunner implements ApplicationRunner {
    /**
     * run
     *
     * @param args
     * @throws Exception
     */
    @Async
    @Override
    public void run(CommandLineArgument args) throws Exception {
        try {
            Giteer.createProject("test", "test");
        } catch (IOException e) {
        }
    }
}
