package com.github.kancyframework.springx.registry;

import org.junit.Test;

public class StringRegistryRedisTests {
    private StringRegistryRedis redis = new StringRegistryRedis("test");

    @Test
    public void test01(){
        redis.set("age", "11");
        System.out.println(redis.get("age"));
        redis.clear();
    }

    @Test
    public void test02(){
        long l = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            redis.set("age::"+i, i + "");
        }
        System.out.println(System.currentTimeMillis() - l);
        System.out.println(redis.get("age::7"));
        redis.delete("age::7");

        redis.set("age::emm::aa", "7");
        System.out.println(redis.get("age::emm::aa"));

    }


    @Test
    public void setTest(){
        redis.set("name", "kancy");
    }

    @Test
    public void getTest(){
        Object name = redis.get("name");
        System.out.println(name);
    }


    @Test
    public void clearTest(){
        redis.clear();
    }

}
