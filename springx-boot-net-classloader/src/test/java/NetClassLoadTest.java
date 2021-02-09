import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.netclassloader.NetClassLoader;
import com.github.kancyframework.springx.utils.ReflectionUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Collections;

/**
 * https://maven.aliyun.com/mvn/guide
 * @author huangchengkang
 * @date 2018-10-11 15:51
 */
@Ignore
public class NetClassLoadTest {

    @Test
    public void test01()  {
        try {
            //加载网络jar
            NetClassLoader.newInstance()
                    .loadJars(
                            "https://gitee.com/kancy666/public/raw/master/jars/fastjson-1.2.69.jar",
                            "https://gitee.com/kancy666/public/raw/master/jars/fastjson-1.2.69.jar"
                    );
            Class.forName("com.alibaba.fastjson.JSON");
            Object o = ReflectionUtils.invokeStaticMethod("com.alibaba.fastjson.JSON", "toJSONString",
                    new Object[]{Collections.singletonMap("a", 1)}, new Class[]{Object.class});
            Log.info(o.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
