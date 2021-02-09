import com.github.kancyframework.springx.utils.FileUtils;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.junit.Test;

import java.io.File;

public class WebdavClientTests {
    private static final String davhost = "https://dav.jianguoyun.com/dav/";
    private static final String davformat = davhost.concat("%s");
    private static final Sardine sardine = SardineFactory.begin("793272861@qq.com", "a7cxqrsftadqkema");

    @Test
    public void upload() throws Exception {
        String filePath = "E:\\develop\\idea\\shuhe\\springx-boot\\demo\\demo-spring-boot-starter-gui\\src\\test\\java\\WebdavClientTests.java";
        sardine.put(getRemoteUrl("/clouddata/WebdavClientTests2.java"), FileUtils.readFileToByteArray(new File(filePath)));
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