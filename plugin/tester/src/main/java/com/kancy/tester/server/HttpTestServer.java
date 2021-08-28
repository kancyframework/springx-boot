package com.kancy.tester.server;

import com.github.kancyframework.springx.boot.ApplicationRunner;
import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.utils.SpringUtils;
import com.kancy.tester.server.handler.HttpContextHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * HttpTestServer
 *
 * @author huangchengkang
 * @date 2021/8/28 22:54
 */
@Component
public class HttpTestServer extends Thread implements ApplicationRunner {

    /**
     * run
     *
     * @param args
     */
    @Override
    public void run(CommandLineArgument args){
        start();
    }

    @Override
    public void run() {
        try {
            int port = 5656;
            startServer(port);
            Log.info("HttpServer 启动成功，监听端口：{}", port);
        } catch (IOException e) {
            Log.error("HttpServer 启动失败！");
        }
    }

    public void startServer(int port) throws IOException {
        // 创建 http 服务器, 绑定本地 8080 端口
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        // 创建上下文监听, "/" 表示匹配所有 URI 请求
        SpringUtils.getBeansOfType(HttpContextHandler.class).values().stream().forEach(httpHandler -> {
            httpServer.createContext(httpHandler.getContextPath(), httpHandler);
        });
        // 启动服务
        httpServer.start();
    }

}
