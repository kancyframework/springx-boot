package com.kancy.dingtalk;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JdkHttpClient
 *
 * @author huangchengkang
 * @date 2021/11/16 12:57
 */
public class HttpClient {

    private static final int READ_TIMEOUT = 5000;
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final String CHARSET_UTF_8 = "utf-8";
    private static final String C_TYPE_JSON_FORMAT = "application/json; charset=%s";

    /**
     * PostJSON
     *
     * @param url         网址
     * @param jsonContent json内容
     * @return {@link String}
     * @throws IOException IO异常
     */
    public String postJson(String url, String jsonContent) throws IOException {
        return postJson(url, jsonContent, null);
    }

    /**
     * PostJSON
     *
     * @param url         网址
     * @param jsonContent json内容
     * @param headerMap   标题映射
     * @return {@link String}
     * @throws IOException IO异常
     */
    public String postJson(String url, String jsonContent, Map<String, String> headerMap)
            throws IOException {
        if (Objects.isNull(headerMap)) {
            headerMap = new HashMap<>();
        }
        if (!headerMap.containsKey("Content-Type")) {
            headerMap.put("Content-Type", String.format(C_TYPE_JSON_FORMAT, CHARSET_UTF_8));
        }
        return post(url, jsonContent, headerMap, READ_TIMEOUT, CONNECTION_TIMEOUT);
    }

    /**
     * 邮递
     *
     * @param url            网址
     * @param requestContent 请求内容
     * @param headerMap      标题映射
     * @param connectTimeout 连接超时
     * @param readTimeout    读超时
     * @return {@link String}
     * @throws IOException IO异常
     */
    public String post(String url, String requestContent, Map<String, ?> headerMap,
                       int connectTimeout, int readTimeout) throws IOException {
        return doRequest("POST", url, requestContent, headerMap, connectTimeout, readTimeout);
    }

    /**
     * 收到
     *
     * @param url 网址
     * @return {@link String}
     * @throws IOException IO异常
     */
    public String get(String url) throws IOException {
        return doRequest("GET", url, null, null, READ_TIMEOUT, CONNECTION_TIMEOUT);
    }

    /**
     * 收到
     *
     * @param url            网址
     * @param connectTimeout 连接超时
     * @param readTimeout    读超时
     * @return {@link String}
     * @throws IOException IO异常
     */
    public String get(String url, int connectTimeout, int readTimeout) throws IOException {
        return doRequest("GET", url, null, null, connectTimeout, readTimeout);
    }

    /**
     * 收到
     *
     * @param url            网址
     * @param headerMap      标题映射
     * @param connectTimeout 连接超时
     * @param readTimeout    读超时
     * @return {@link String}
     * @throws IOException IO异常
     */
    public String get(String url, Map<String, ?> headerMap, int connectTimeout, int readTimeout) throws IOException {
        return doRequest("GET", url, null, headerMap, connectTimeout, readTimeout);
    }

    /**
     * 请求
     *
     * @param method         方法
     * @param url            网址
     * @param requestContent 请求内容
     * @param headerMap      标题映射
     * @param connectTimeout 连接超时
     * @param readTimeout    读超时
     * @return {@link String}
     * @throws IOException IO异常
     */
    protected String doRequest(String method, String url, String requestContent, Map<String, ?> headerMap,
                               int connectTimeout, int readTimeout) throws IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            conn = getConnection(new URL(url), method, headerMap);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            if (requestContent != null && requestContent.trim().length() > 0) {
                out = conn.getOutputStream();
                out.write(requestContent.getBytes(CHARSET_UTF_8));
            }
            rsp = getResponseAsString(conn);
        } finally {
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }


    /**
     * 取得联系
     *
     * @param url       网址
     * @param method    方法
     * @param headerMap 标题映射
     * @return {@link HttpURLConnection}
     * @throws IOException IO异常
     */
    private HttpURLConnection getConnection(URL url, String method, Map<String, ?> headerMap) throws IOException {
        HttpURLConnection conn = getHttpURLConnection(url);
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,application/json");
        if (headerMap != null) {
            for (Map.Entry<String, ?> entry : headerMap.entrySet()) {
                if (Objects.nonNull(entry.getValue())) {
                    conn.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        return conn;
    }


    /**
     * 获取http URL连接
     *
     * @param url 网址
     * @return {@link HttpURLConnection}
     * @throws IOException IO异常
     */
    private HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection connection;
        if ("https".equals(url.getProtocol())) {
            SSLContext ctx;
            try {
                ctx = SSLContext.getInstance("TLS");
                ctx.init(new KeyManager[0],
                        new TrustManager[]{new DefaultTrustManager()},
                        new SecureRandom());
            } catch (Exception e) {
                throw new IOException(e);
            }
            HttpsURLConnection connHttps = (HttpsURLConnection) url
                    .openConnection();
            connHttps.setSSLSocketFactory(ctx.getSocketFactory());
            connHttps.setHostnameVerifier((hostname, session) -> true);
            connection = connHttps;
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        return connection;
    }

    /**
     * 以字符串形式获取响应
     *
     * @param conn 康涅狄格州？
     * @return {@link String}
     * @throws IOException IO异常
     */
    private String getResponseAsString(HttpURLConnection conn)
            throws IOException {
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), CHARSET_UTF_8);
        } else {
            String msg = getStreamAsString(es, CHARSET_UTF_8);
            if (msg != null && msg.trim().length() > 0) {
                throw new IOException(conn.getResponseCode() + ":"
                        + conn.getResponseMessage());
            } else {
                return msg;
            }
        }
    }

    /**
     * 以字符串形式获取流
     *
     * @param stream  流动
     * @param charset 字符集
     * @return {@link String}
     * @throws IOException IO异常
     */
    private String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();
            final char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }
            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    /**
     * 默认信任管理器
     *
     * @author kancy
     * @date 2021/11/16
     */
    private class DefaultTrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }
    }
}
