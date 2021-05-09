package com.kancy.spring.minidb;

import com.github.kancyframework.springx.log.Log;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

public class MapDbTests {

    @Before
    public void before(){
        System.setProperty("spring.application.name", "junit-test");
    }

    @Test
    public void test01(){
        MapDb.putData("count", MapDb.getData("count", 0) + 1);
        Integer count = MapDb.getData("count");
        Log.info("MapDbTests count : {}", count);
    }

    @Test
    public void test02(){
        MapDb.beginTransaction();
        MapDb.putData("txCount", MapDb.getData("txCount", 0) + 1);
        MapDb.commitTransaction();
    }

    @Test
    public void test03(){
        Serializable txCount = MapDb.getData("txCount");
        Log.info("MapDbTests txCount : {}", txCount);

        Serializable txDate = MapDb.getData("txDate");
        Log.info("MapDbTests txDate : {}", txDate);
    }
}
