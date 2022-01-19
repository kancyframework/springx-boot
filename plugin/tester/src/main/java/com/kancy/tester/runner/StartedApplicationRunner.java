package com.kancy.tester.runner;

import com.github.kancyframework.dingtalk.DingTalkClient;
import com.github.kancyframework.springx.boot.ApplicationRunner;
import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.context.annotation.Async;
import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.utils.SystemUtils;

/**
 * StartedApplicationRunner
 *
 * @author huangchengkang
 * @date 2021/12/12 13:53
 */
@Component
public class StartedApplicationRunner implements ApplicationRunner {

    @Autowired
    private DingTalkClient dingTalkClient;
    /**
     * run
     *
     * @param args
     * @throws Exception
     */
    @Async
    @Override
    public void run(CommandLineArgument args) throws Exception {
        dingTalkClient.sendText("我启动了: " + SystemUtils.getUserName());
    }
}
