package com.github.kancyframework.springx.webdav;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.utils.PathUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * WebdavClient
 *
 * @author huangchengkang
 * @date 2021/10/21 11:03
 */
public class WebdavClientImpl implements WebdavClient{
    private final Sardine sardine;

    private String baseUrl;
    private String username;
    private String password;
    private String contextPath;

    public WebdavClientImpl() {
        this("/");
    }

    public WebdavClientImpl(String contextPath) {
        this("https://dav.jianguoyun.com/dav", "793272861@qq.com", "a7cxqrsftadqkema", contextPath);
    }

    public WebdavClientImpl(String baseUrl, String username, String password) {
        this(baseUrl, username, password, "/");
    }

    public WebdavClientImpl(String baseUrl, String username, String password, String contextPath) {
        this.baseUrl = baseUrl;
        this.username = username;
        this.password = password;
        this.contextPath = contextPath;
        this.sardine = SardineFactory.begin(username, password);
        createContextPathDirectory(contextPath);
        addShutdownHook();
    }

    private void createContextPathDirectory(String contextPath) {
        try {
            createDirectory(contextPath);
        } catch (IOException e) {
            Log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取资源
     *
     * @param path 资源路径
     * @return
     * @throws IOException
     */
    @Override
    public InputStream get(String path) throws IOException {
        return sardine.get(getWebdavUrl(path));
    }

    /**
     * 获取资源
     *
     * @param path    资源路径
     * @param headers 请求头
     * @return
     * @throws IOException
     */
    @Override
    public InputStream get(String path, Map<String, String> headers) throws IOException {
        return sardine.get(getWebdavUrl(path), headers);
    }

    /**
     * 新增/更新资源
     *
     * @param path 资源路径
     * @param data 数据
     * @throws IOException
     */
    @Override
    public void put(String path, byte[] data) throws IOException {
        sardine.put(getWebdavUrl(path), data);
    }

    /**
     * 新增/更新资源
     *
     * @param path        资源路径
     * @param data        数据
     * @param contentType 数据类型
     * @throws IOException
     */
    @Override
    public void put(String path, byte[] data, String contentType) throws IOException {
        sardine.put(getWebdavUrl(path), data, contentType);
    }

    /**
     * 新增/更新资源
     *
     * @param path       资源路径
     * @param dataStream 数据流
     * @throws IOException
     */
    @Override
    public void put(String path, InputStream dataStream) throws IOException {
        sardine.put(getWebdavUrl(path), dataStream);
    }

    /**
     * 新增/更新资源
     *
     * @param path        资源路径
     * @param dataStream  数据流
     * @param contentType 数据类型
     * @throws IOException
     */
    @Override
    public void put(String path, InputStream dataStream, String contentType) throws IOException {
        sardine.put(getWebdavUrl(path), dataStream, contentType);
    }

    /**
     * 新增/更新资源
     *
     * @param path       资源路径
     * @param dataStream 数据流
     * @param headers    请求头
     * @throws IOException
     */
    @Override
    public void put(String path, InputStream dataStream, Map<String, String> headers) throws IOException {
        sardine.put(getWebdavUrl(path), dataStream, headers);
    }

    /**
     * 新增/更新资源
     *
     * @param path      资源路径
     * @param localFile 本地文件
     * @throws IOException
     */
    @Override
    public void put(String path, File localFile) throws IOException {
        sardine.put(getWebdavUrl(path), localFile, null);
    }

    /**
     * 新增/更新资源
     *
     * @param path        资源路径
     * @param localFile   本地文件
     * @param contentType 数据类型
     * @throws IOException
     */
    @Override
    public void put(String path, File localFile, String contentType) throws IOException {
        sardine.put(getWebdavUrl(path), localFile, contentType);
    }

    /**
     * 删除文件 （文件或文件夹）
     *
     * @param path 资源路径
     * @throws IOException
     */
    @Override
    public void delete(String path) throws IOException {
        sardine.delete(getWebdavUrl(path));
    }

    /**
     * 列出资源
     *
     * @param path 资源路径
     * @return
     * @throws IOException
     */
    @Override
    public List<DavResource> list(String path) throws IOException {
        return sardine.list(getWebdavUrl(path));
    }

    /**
     * 搜索资源
     *
     * @param path  资源路径
     * @param query 查询关键字
     * @return
     * @throws IOException
     */
    @Override
    public List<DavResource> search(String path, String query) throws IOException {
        return sardine.search(getWebdavUrl(path), "davbasic", query);
    }

    /**
     * 搜索资源
     *
     * @param path     资源路径
     * @param language 查询语法
     * @param query    查询语句
     * @return
     * @throws IOException
     */
    @Override
    public List<DavResource> search(String path, String language, String query) throws IOException {
        return sardine.search(getWebdavUrl(path), language, query);
    }

    /**
     * 文件移到
     *
     * @param sourcePath      源路径
     * @param destinationPath 目标路径
     * @throws IOException
     */
    @Override
    public void move(String sourcePath, String destinationPath) throws IOException {
        sardine.move(getWebdavUrl(sourcePath), getWebdavUrl(destinationPath));
    }

    /**
     * 文件移到
     *
     * @param sourcePath      源路径
     * @param destinationPath 目标路径
     * @param overwrite       是否重写
     * @throws IOException
     */
    @Override
    public void move(String sourcePath, String destinationPath, boolean overwrite) throws IOException {
        sardine.move(getWebdavUrl(sourcePath), getWebdavUrl(destinationPath), overwrite);
    }

    /**
     * 文件复制
     *
     * @param sourcePath      源路径
     * @param destinationPath 目标路径
     * @throws IOException
     */
    @Override
    public void copy(String sourcePath, String destinationPath) throws IOException {
        sardine.copy(getWebdavUrl(sourcePath), getWebdavUrl(destinationPath));
    }

    /**
     * 文件复制
     *
     * @param sourcePath      源路径
     * @param destinationPath 目标路径
     * @param overwrite       是否重写
     * @throws IOException
     */
    @Override
    public void copy(String sourcePath, String destinationPath, boolean overwrite) throws IOException {
        sardine.copy(getWebdavUrl(sourcePath), getWebdavUrl(destinationPath), overwrite);
    }

    /**
     * 创建文件夹，支持级联创建
     *
     * @param path
     * @throws IOException
     */
    @Override
    public void createDirectory(String path) throws IOException {
        if (path.startsWith("/")) {
            path = path.length() > 1 ? path.substring(1) : "";
        }
        if (StringUtils.isBlank(path)) {
            return;
        }
        String[] items = path.split("/");
        if (items.length == 1) {
            sardine.createDirectory(getWebdavUrl(path));
        } else {
            StringBuilder dirPath = new StringBuilder();
            boolean appearNotExistsPath = false;
            for (String item : items) {
                dirPath.append("/").append(item);
                String webdavUrl = getWebdavUrl(dirPath.toString());
                if (appearNotExistsPath) {
                    sardine.createDirectory(webdavUrl);
                } else {
                    if (!sardine.exists(webdavUrl)) {
                        appearNotExistsPath = true;
                        sardine.createDirectory(webdavUrl);
                    }
                }
            }
        }
    }

    /**
     * 资源是否存在
     *
     * @param path 资源路径
     * @return
     * @throws IOException
     */
    @Override
    public boolean exists(String path) throws IOException {
        return sardine.exists(getWebdavUrl(path));
    }

    /**
     * 锁定资源
     *
     * @param path 资源路径
     * @return 资源Token
     * @throws IOException
     */
    @Override
    public String lock(String path) throws IOException {
        return sardine.lock(getWebdavUrl(path));
    }

    /**
     * 释放锁定资源
     *
     * @param path  资源路径
     * @param token 资源Token
     * @throws IOException
     */
    @Override
    public void unlock(String path, String token) throws IOException {
        sardine.unlock(getWebdavUrl(path), token);
    }

    /**
     * 关闭资源
     */
    @Override
    public void shutdown() {
        if (Objects.nonNull(sardine)){
            try {
                sardine.shutdown();
            } catch (IOException e) {
                Log.error(e.getMessage());
            }
        }
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    /**
     * 拼接真实路径
     * @param path
     * @return
     */
    private String getWebdavUrl(String path) {
        String newPath = PathUtils.append(contextPath, path);
        if (newPath.startsWith("/")){
            return String.format("%s%s", baseUrl, newPath);
        }
        return String.format("%s/%s", baseUrl, newPath);
    }

}
