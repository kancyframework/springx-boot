package com.github.kancyframework.springx.netclassloader;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * jdk 9 以上的jdk需要增加jvm参数 --add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED
 * java --add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED -jar xx.jar
 *
 * @author huangchengkang
 * @date 2018-10-11 15:02
 */
public class NetClassLoader {

    private static Logger log = LoggerFactory.getLogger(NetClassLoader.class);

    private NetClassLoader() {}

    private static NetClassLoader netClassLoader;

    private static ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    private static List<String> classUrlList = new ArrayList<>();

    /**
     * double check 创建示例
     */
    public static NetClassLoader newInstance() {
        if(netClassLoader == null){
            synchronized (NetClassLoader.class){
                if(netClassLoader == null){
                    netClassLoader = new NetClassLoader();
                }
            }
        }
        return netClassLoader;
    }

    /**
     * 加载
     * @param urls
     * @return
     */
    public static NetClassLoader load(String ... urls) {
        return NetClassLoader.newInstance().loadJars(urls);
    }


    /**
     * 加载
     * @param urlPaths
     */
    public NetClassLoader loadJars(String ... urlPaths) {
        Assert.isNotBlank(urlPaths, "param urls is empty");
        if (urlPaths.length == 0){
            return this;
        }
        List<URL> list = null;
        try {
            if (urlPaths.length == 1){
                loadJars(new URL(urlPaths[0]));
                return this;
            }
            list = new ArrayList<>(urlPaths.length);
            Set<String> set = CollectionUtils.toSet(urlPaths);
            for (String urlPath : set) {
                if(urlPath != null && urlPath.trim().length() > 0){
                    list.add(new URL(urlPath));
                }
            }
        } catch (MalformedURLException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        loadJars(list.toArray(new URL[]{}));
        return this;
    }

    /**
     * 加载
     * @param urls
     */
    public NetClassLoader loadJars(URL ... urls){
        Assert.isNotEmpty(urls, "param urls is empty");

        Arrays.stream(urls).parallel().forEach(url -> {
            if (classUrlList.contains(url.toString())){
                log.info("已成功加载网络类库资源文件：{}", url);
                return;
            }
            try {
                // 下载文件到本地
                File newFile = FileUtils.createNewFile(PathUtils.path(System.getProperty("user.home"), "jars", Md5Utils.md5(url.toString())  + ".jar"));
                URL newUrl = newFile.toURL();
                if (newFile.length() < 1){
                    try {
                        JdkHttpUtils.downloadFile(url.toString(), newFile);
                    } catch (IOException e) {
                        log.warn("无法下载文件", e);
                        newUrl = url;
                    }
                }

                if (classLoader instanceof URLClassLoader){
                    ReflectionUtils.invokeMethod(classLoader, "addURL", newUrl);
                }else {
                    Field field = classLoader.getClass().getDeclaredField("ucp");
                    field.setAccessible(true);
                    Object ucp = field.get(classLoader);
                    Method method = ucp.getClass().getDeclaredMethod("addURL", URL.class);
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(ucp, newUrl);
                }
                classUrlList.add(url.toString());
                log.info("成功加载网络类库资源文件：{}", url);
            } catch (Exception e) {
                if (Objects.equals("java.lang.reflect.InaccessibleObjectException", e.getClass().getName())){
                    log.error("JDK9及以上需要增加JVM启动参数：--add-opens java.base/jdk.internal.loader=ALL-UNNAMED --add-opens jdk.zipfs/jdk.nio.zipfs=ALL-UNNAMED");
                } else {
                    log.info("失败加载网络类库资源文件：{} , {}", url, e);
                }
            }
        });
        return this;
    }

}
