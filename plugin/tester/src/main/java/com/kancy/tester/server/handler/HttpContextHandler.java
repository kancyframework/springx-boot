package com.kancy.tester.server.handler;

import com.sun.net.httpserver.HttpHandler;

/**
 * HttpServerHandler
 *
 * @author huangchengkang
 * @date 2021/8/28 23:14
 */
public interface HttpContextHandler extends HttpHandler {
    String getContextPath();
}
