package com.kancy.tester.server.handler;

import com.github.kancyframework.springx.utils.StringUtils;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * BaseHttpHandler
 *
 * @author huangchengkang
 * @date 2021/8/28 23:03
 */
public abstract class BaseHttpHandler implements HttpContextHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        String resp = null;
        if (Objects.equals(requestMethod, "GET")){
            resp = doGet(httpExchange);
        } else if (Objects.equals(requestMethod, "POST")){
            resp = doPost(httpExchange);
        } else {
            resp = doUnsupportMethod(httpExchange);
        }
        // 响应内容
        byte[] respContents = resp.getBytes(getResponseCharset());
        // 设置响应头
        httpExchange.getResponseHeaders().add("Content-Type", getContext());
        // 设置响应code和内容长度
        httpExchange.sendResponseHeaders(200, respContents.length);
        // 设置响应内容
        httpExchange.getResponseBody().write(respContents);
        // 关闭处理器, 同时将关闭请求和响应的输入输出流（如果还没关闭）
        httpExchange.close();
    }


    protected Map<String, String> queryParamsToMap(HttpExchange exchange){
        String query = exchange.getRequestURI().getQuery();
        if (StringUtils.isBlank(query)){
            return new HashMap<>();
        }
        Map<String, String> params = new HashMap<>();
        List<String> list = StringUtils.toList(query, "&");
        list.stream().forEach(item->{
            String[] kv = StringUtils.toArray(item, "=");
            params.put(kv[0].trim(), kv[1]);
        });
        return params;
    }

    protected String doUnsupportMethod(HttpExchange httpExchange) {
        return "不支持的请求方式:" + httpExchange.getRequestMethod();
    }

    protected String getResponseCharset() {
        return "UTF-8";
    }

    protected String getContext() {
        return "application/json; charset=" + getResponseCharset();
    }

    protected String doPost(HttpExchange httpExchange){
        return "";
    }

    protected String doGet(HttpExchange httpExchange) {
        return "";
    }
}
