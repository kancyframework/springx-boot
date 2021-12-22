package handlers;
import com.kancy.jevel.DynamicHandler;

import java.util.Map;

/**
 * HelloDynamicHandler
 *
 * @author huangchengkang
 * @date 2021/12/22 14:10
 */
public class HelloDynamicHandler implements DynamicHandler<String> {

    @Override
    public String handle(Map<String, Object> context) {
        return "hello world!";
    }
}
