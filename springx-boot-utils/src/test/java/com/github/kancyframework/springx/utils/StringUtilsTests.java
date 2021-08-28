package com.github.kancyframework.springx.utils;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * StringUtilsTests
 *
 * @author huangchengkang
 * @date 2021/8/28 22:25
 */
public class StringUtilsTests {
    @Test
    public void test02(){
        System.out.println(SimpleJsonUtils.parseMap("{\"a\":1,\"b\":2.3,\"c\":false,\"d\":\"ddd\"}"));
        System.out.println(SimpleJsonUtils.parseObject("{\"a\":1,\"b\":2.3,\"c\":false,\"d\":\"ddd\"}", Map.class));
    }

    @Test
    public void test01(){
        Map<Object, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2.3d);
        map.put("c",false);
        map.put("d", "ddd");
        map.put("e", null);
        System.out.println(StringUtils.toJSONString(map));
    }
}
