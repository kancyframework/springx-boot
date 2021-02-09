package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/**
 * http请求工具类
 *
 * @author kancy
 * @date 2021/1/7 12:16
 */
public class JdkHttpUtils {
    private static final Logger log = LoggerFactory.getLogger(JdkHttpUtils.class);
    private static final String CHARSET_UTF_8 = "utf-8";
    private static final String C_TYPE_FORM_FORMAT = "application/x-www-form-urlencoded;charset=%s";
    private static final String C_TYPE_JSON_FORMAT = "application/json; charset=%s";
    private static final String C_TYPE_STREAM_FORMAT = "application/octet-stream";

    private static final ThreadLocal<Integer> CONNECT_TIMEOUT = ThreadLocal.withInitial(() -> 5000);
    private static final ThreadLocal<Integer> READ_TIMEOUT = ThreadLocal.withInitial(() -> 10000);
    private static final ThreadLocal<String> CHARSET = ThreadLocal.withInitial(() -> CHARSET_UTF_8);

    /**
     * 下载文件
     * @param httpUrl
     * @param filePath
     * @throws IOException
     */
    public static void downloadFile(String httpUrl, String filePath) throws IOException {
        downloadFile(httpUrl, new File(filePath));
    }

    public static void download(String httpUrl, String fileName) throws IOException {
        download(httpUrl, fileName, false);
    }

    public static void download(String httpUrl, String fileName, boolean uniq) throws IOException {
        if (uniq){
            int lastIndexOf = fileName.lastIndexOf(".");
            if (lastIndexOf > 0 && lastIndexOf < fileName.length()){
                fileName = String.format("%s_%s%s", fileName.substring(0,lastIndexOf), DateUtils.getNowTimestampStr() , fileName.substring(lastIndexOf));
            }
        }
        downloadFile(httpUrl, new File(PathUtils.path(System.getProperty("user.home"), "Downloads", fileName)));
    }

    /**
     * 下载文件
     * @param httpUrl
     * @param file
     * @throws IOException
     */
    public static void downloadFile(String httpUrl, File file) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            long startTime = System.currentTimeMillis();
            URL url = new URL(httpUrl);
            HttpURLConnection connection = getHttpURLConnection(url);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", C_TYPE_STREAM_FORMAT);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");

            bis = new BufferedInputStream(connection.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(FileUtils.createNewFile(file.getAbsolutePath())));
            int size = 0;
            int len = 0;
            byte[] buff = new byte[1024 * 512];
            while ((len = bis.read(buff)) != -1) {
                bos.write(buff, 0, len);
                bos.flush();
                size += len;
            }
            log.info("成功下载文件：{} , 总大小：{}k ({}) , 耗时：{} ms",
                        PathUtils.format(file), size/1024 , size, (System.currentTimeMillis() - startTime));
        } finally {
            IoUtils.closeResource(bis);
            IoUtils.closeResource(bos);
        }
    }

    private static HttpURLConnection getHttpURLConnection(URL url) throws IOException {
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
     * 下载文件
     * @param httpUrl
     * @throws IOException
     */
    public static InputStream download(String httpUrl) throws IOException {
        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", C_TYPE_STREAM_FORMAT);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.connect();

        int contentLength = connection.getContentLength();
        if (contentLength > 32) {
            return new BufferedInputStream(connection.getInputStream());
        }
        return connection.getInputStream();
    }

    /**
     *
     * @param url
     * @param jsonContent
     * @param headerMap
     * @return
     * @throws IOException
     */
    public static String putJson(String url, String jsonContent, Map<String, ?> headerMap)
            throws IOException {
        return put(url, jsonContent, CONNECT_TIMEOUT.get(), READ_TIMEOUT.get(),
                String.format(C_TYPE_JSON_FORMAT, CHARSET.get()), headerMap);
    }

    /**
     *
     * @param url
     * @param jsonContent
     * @return
     * @throws IOException
     */
    public static String putJson(String url, String jsonContent) throws IOException {
        return putJson(url, jsonContent, null);
    }

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String putForm(String url) throws IOException {
        return putForm(url, null);
    }

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String putForm(String url, Map<String, Object> params) throws IOException {
        return putForm(url, params, null);
    }

    /**
     *
     * @param url
     * @param params
     * @param header
     * @return
     * @throws IOException
     */
    public static String putForm(String url, Map<String, Object> params, Map<String, ?> header) throws IOException {
        return put(url, buildQuery(params), CONNECT_TIMEOUT.get(), READ_TIMEOUT.get(),
                String.format(C_TYPE_FORM_FORMAT, CHARSET.get()), header);
    }

    /**
     *
     * @param url
     * @param jsonContent
     * @param headerMap
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public static String postJson(String url, String jsonContent, Map<String, String> headerMap)
            throws SocketTimeoutException, IOException {
        return post(url, jsonContent, CONNECT_TIMEOUT.get(), READ_TIMEOUT.get(),
                String.format(C_TYPE_JSON_FORMAT, CHARSET.get()), headerMap);
    }

    /**
     *
     * @param url
     * @param jsonContent
     * @return
     * @throws IOException
     */
    public static String postJson(String url, String jsonContent) throws IOException {
        return postJson(url, jsonContent , null);
    }

    /**
     * POST 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public static String postForm(String url) throws IOException {
        return postForm(url, null);
    }

    /**
     * POST 以application/x-www-form-urlencoded;charset=utf-8方式传输
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public static String postForm(String url, Map<String, Object> params) throws IOException {
        return postForm(url, params, null);
    }

    /**
     * postForm
     * @param url
     * @param params
     * @param header
     * @return
     * @throws IOException
     */
    public static String postForm(String url, Map<String, Object> params, Map<String, ?> header) throws IOException {
        return doRequest("POST", url, buildQuery(params), CONNECT_TIMEOUT.get(), READ_TIMEOUT.get(),
                String.format(C_TYPE_FORM_FORMAT, CHARSET.get()), header);
    }
    /**
     * getForm
     *
     * @param url
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     */
    public static String getForm(String url) throws IOException {
        return getForm(url, null);
    }

    /**
     * getForm
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String getForm(String url, Map<String, Object> params) throws IOException {
        return getForm(url, params, null);
    }

    /**
     * getForm
     * @param url
     * @param params
     * @param header
     * @return
     * @throws IOException
     */
    public static String getForm(String url, Map<String, Object> params, Map<String, ?> header) throws IOException {
        return get(url, buildQuery(params), CONNECT_TIMEOUT.get(), READ_TIMEOUT.get(),
                String.format(C_TYPE_FORM_FORMAT, CHARSET.get()), header);
    }

    public static String get(String url, String requestContent,
                                   int connectTimeout, int readTimeout, String contentType,
                                   Map<String, ?> headerMap) throws IOException {
        return doRequest("GET", url,requestContent, connectTimeout, readTimeout, contentType, headerMap);
    }

    public static String post(String url, String requestContent,
                             int connectTimeout, int readTimeout, String contentType,
                             Map<String, ?> headerMap) throws IOException {
        return doRequest("POST", url,requestContent, connectTimeout, readTimeout, contentType, headerMap);
    }

    public static String put(String url, String requestContent,
                             int connectTimeout, int readTimeout, String contentType,
                             Map<String, ?> headerMap) throws IOException {
        return doRequest("PUT", url,requestContent, connectTimeout, readTimeout, contentType, headerMap);
    }
    public static String delete(String url, String requestContent,
                             int connectTimeout, int readTimeout, String contentType,
                             Map<String, ?> headerMap) throws IOException {
        return doRequest("DELETE", url,requestContent, connectTimeout, readTimeout, contentType, headerMap);
    }

    /**
     *
     * <p>@Description: </p>
     * @Title doRequest
     * @author zhouyy
     * @param method 请求的method post/get
     * @param url 请求url
     * @param requestContent  请求参数
     * @param connectTimeout  请求超时
     * @param readTimeout 响应超时
     * @param contentType 请求格式  xml/json等等
     * @param headerMap 请求header中要封装的参数
     * @return
     * @throws SocketTimeoutException
     * @throws IOException
     * @date: 2019年10月14日 下午3:47:35
     */
    public static String doRequest(String method, String url, String requestContent,
                             int connectTimeout, int readTimeout, String contentType,
                             Map<String, ?> headerMap) throws SocketTimeoutException,
            IOException {
        HttpURLConnection conn = null;
        OutputStream out = null;
        String rsp = null;
        try {
            conn = getConnection(new URL(url), method, contentType, headerMap);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            if(requestContent != null && requestContent.trim().length() >0){
                out = conn.getOutputStream();
                out.write(requestContent.getBytes(CHARSET_UTF_8));
            }
            rsp = getResponseAsString(conn);
        } finally {
            removeThreadLocal();
            if (out != null) {
                out.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rsp;
    }

    public static void setCurrentConnectTimeout(int connectTimeout){
        CONNECT_TIMEOUT.set(connectTimeout);
    }
    public static void setCurrentReadTimeout(int readTimeout){
        READ_TIMEOUT.set(readTimeout);
    }
    public static void setCurrentCharsetName(String charsetName){
        CHARSET.set(charsetName);
    }

    private static void removeThreadLocal() {
        CONNECT_TIMEOUT.remove();
        READ_TIMEOUT.remove();
        CHARSET.remove();
    }


    private static HttpURLConnection getConnection(URL url, String method,
                                            String contentType, Map<String, ?> headerMap) throws IOException {
        HttpURLConnection conn = getHttpURLConnection(url);
        conn.setRequestMethod(method);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "text/xml,text/javascript,text/html,application/json");
        conn.setRequestProperty("Content-Type", contentType);
        if (headerMap != null) {
            for (Map.Entry<String, ?> entry : headerMap.entrySet()) {
                if (Objects.nonNull(entry.getValue())){
                    conn.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        return conn;
    }

    private static String getResponseAsString(HttpURLConnection conn)
            throws IOException {
        InputStream es = conn.getErrorStream();
        if (es == null) {
            return getStreamAsString(conn.getInputStream(), CHARSET_UTF_8, conn);
        } else {
            String msg = getStreamAsString(es, CHARSET_UTF_8, conn);
            if (msg != null && msg.trim().length() >0) {
                throw new IOException(conn.getResponseCode() + ":"
                        + conn.getResponseMessage());
            } else {
                return msg;
            }
        }
    }

    private static String getStreamAsString(InputStream stream, String charset,
                                     HttpURLConnection conn) throws IOException {
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

    private static String buildQuery(Map<String, Object> params) throws IOException {
        if (params == null || params.isEmpty()) {
            return "";
        }
        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        boolean hasParam = false;

        for (Map.Entry<String, ?> entry : entries) {
            String name = entry.getKey();
            if (Objects.nonNull(entry.getValue())){
                String value = String.valueOf(entry.getValue());
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
                query.append(name).append("=").append(URLEncoder.encode(value, CHARSET_UTF_8));
            }
        }
        return query.toString();
    }

    private static class DefaultTrustManager implements X509TrustManager {
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