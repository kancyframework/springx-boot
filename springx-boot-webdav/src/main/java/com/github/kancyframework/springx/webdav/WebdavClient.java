package com.github.kancyframework.springx.webdav;

import com.github.sardine.DavResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * WebdavClient
 *
 * @author huangchengkang
 * @date 2021/10/21 11:26
 */
public interface WebdavClient {

    /**
     * 获取资源
     * @param path 资源路径
     * @return
     * @throws IOException
     */
    InputStream get(String path) throws IOException;

    /**
     * 获取资源
     * @param path 资源路径
     * @param headers 请求头
     * @return
     * @throws IOException
     */
    InputStream get(String path, Map<String, String> headers) throws IOException;

    /**
     * 新增/更新资源
     * @param path 资源路径
     * @param data 数据
     * @throws IOException
     */
    void put(String path, byte[] data) throws IOException;

    /**
     * 新增/更新资源
     * @param path 资源路径
     * @param data 数据
     * @param contentType 数据类型
     * @throws IOException
     */
    void put(String path, byte[] data, String contentType) throws IOException;

    /**
     * 新增/更新资源
     * @param path 资源路径
     * @param dataStream 数据流
     * @throws IOException
     */
    void put(String path, InputStream dataStream) throws IOException;

    /**
     * 新增/更新资源
     * @param path 资源路径
     * @param dataStream 数据流
     * @param contentType 数据类型
     * @throws IOException
     */
    void put(String path, InputStream dataStream, String contentType) throws IOException;

    /**
     * 新增/更新资源
     * @param path 资源路径
     * @param dataStream 数据流
     * @param headers 请求头
     * @throws IOException
     */
    void put(String path, InputStream dataStream, Map<String, String> headers) throws IOException;

    /**
     * 新增/更新资源
     * @param path 资源路径
     * @param localFile 本地文件
     * @throws IOException
     */
    void put(String path, File localFile) throws IOException;

    /**
     * 新增/更新资源
     * @param path 资源路径
     * @param localFile 本地文件
     * @param contentType 数据类型
     * @throws IOException
     */
    void put(String path, File localFile, String contentType) throws IOException;

    /**
     * 删除文件 （文件或文件夹）
     * @param path 资源路径
     * @throws IOException
     */
    void delete(String path) throws IOException;

    /**
     * 列出资源
     * @param path 资源路径
     * @return
     * @throws IOException
     */
    List<DavResource> list(String path) throws IOException;
    /**
     * 搜索资源
     * @param path 资源路径
     * @param query 查询关键字
     * @return
     * @throws IOException
     */
    List<DavResource> search(String path, String query) throws IOException;
    /**
     * 搜索资源
     * @param path 资源路径
     * @param language 查询语法
     * @param query 查询语句
     * @return
     * @throws IOException
     */
    List<DavResource> search(String path, String language, String query) throws IOException;

    /**
     * 文件移到
     * @param sourcePath 源路径
     * @param destinationPath 目标路径
     * @throws IOException
     */
    void move(String sourcePath, String destinationPath) throws IOException;

    /**
     * 文件移到
     * @param sourcePath 源路径
     * @param destinationPath 目标路径
     * @param overwrite 是否重写
     * @throws IOException
     */
    void move(String sourcePath, String destinationPath, boolean overwrite) throws IOException;

    /**
     * 文件复制
     * @param sourcePath 源路径
     * @param destinationPath 目标路径
     * @throws IOException
     */
    void copy(String sourcePath, String destinationPath) throws IOException;

    /**
     * 文件复制
     * @param sourcePath 源路径
     * @param destinationPath 目标路径
     * @param overwrite 是否重写
     * @throws IOException
     */
    void copy(String sourcePath, String destinationPath, boolean overwrite) throws IOException;

    /**
     * 创建文件夹，支持级联创建
     * @param path
     * @throws IOException
     */
    void createDirectory(String path) throws IOException;

    /**
     * 资源是否存在
     * @param path 资源路径
     * @return
     * @throws IOException
     */
    boolean exists(String path) throws IOException;

    /**
     * 锁定资源
     * @param path 资源路径
     * @return 资源Token
     * @throws IOException
     */
    String lock(String path) throws IOException;

    /**
     * 释放锁定资源
     * @param path 资源路径
     * @param token 资源Token
     * @throws IOException
     */
    void unlock(String path, String token) throws IOException;

    /**
     * 关闭资源
     */
    void shutdown();

}
