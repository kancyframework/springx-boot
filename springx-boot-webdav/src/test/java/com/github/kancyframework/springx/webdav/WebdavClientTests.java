package com.github.kancyframework.springx.webdav;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.log.LogLevel;
import com.github.kancyframework.springx.utils.DebugUtils;
import com.github.kancyframework.springx.utils.FileUtils;
import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * WebdavClientTests
 *
 * @author huangchengkang
 * @date 2021/10/21 10:53
 */

public class WebdavClientTests {
    private static final String davhost = "https://dav.jianguoyun.com/dav/";
    private static final String davformat = davhost.concat("%s");
    private static final Sardine sardine = SardineFactory.begin("793272861@qq.com", "a7cxqrsftadqkema");
    @Test
    public void test01() throws Exception {
        WebdavClient webdavClient = new WebdavClientImpl("http://localhost/webdav",
                "admin","admin","/");
        String filePath = "E:\\Work\\Idea\\workspace\\springx-boot\\springx-boot-webdav\\src\\test\\java\\com\\github\\kancyframework\\springx\\webdav\\WebdavClientTests.java";
//        webdavClient.put("/WebdavClientTests3.java", new File(filePath));
        List<DavResource> list = webdavClient.list("/PublicFileRepository");
        System.out.println(list);
    }
    @Test
    public void createDirectory() throws Exception {
        // http://192.168.114.168/webdav
        WebdavClient webdavClient = new WebdavClientImpl("clouddata");
//        webdavClient.createDirectory("/a/b/c");
//        webdavClient.delete("/a");
//        List<DavResource> list = webdavClient.list("/");
//        for (DavResource davResource : list) {
//            System.out.println(davResource);
//        }

        List<DavResource> webdavClientTest = webdavClient.search("/", "WebdavClientTest");
        System.out.println(webdavClientTest);
    }

    @Test
    public void upload() throws Exception {
        String filePath = "E:\\Work\\Idea\\workspace\\springx-boot\\springx-boot-webdav\\src\\test\\java\\com\\github\\kancyframework\\springx\\webdav\\WebdavClientTests.java";

        Log.setLogLevel(LogLevel.DEBUG);
        DebugUtils.startPoint();
        for (int i = 0; i <3 ; i++) {
            String url = getRemoteUrl("/clouddata/WebdavClientTests3.java");
//            sardine.put(url,FileUtils.readFileToByteArray(new File(filePath)));
            System.out.println(sardine.exists(url));
            DebugUtils.printPoint("["+i+"]");
        }
        DebugUtils.endPoint();
        sardine.shutdown();
        sardine.shutdown();

        sardine.createDirectory(getRemoteUrl("/clouddata/dira/dirb/"));

//        sardine.delete(getRemoteUrl("/clouddata/dira/"));
    }

    private String getRemoteUrl(String path) {
        if (path.startsWith("/")){
            path = path.substring(1);
        }
        String url = String.format(davformat, path);
        System.out.println(url);
        return url;
    }
}
