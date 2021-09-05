package com.github.kancyframework.springx.utils;

import org.junit.Test;

/**
 * CalendarUtilsTests
 *
 * @author huangchengkang
 * @date 2021/9/5 21:01
 */
public class CalendarUtilsTests {
    @Test
    public void test01() throws Exception {
        System.out.println(CalendarUtils.lunarToSolar("20210729"));
        System.out.println(CalendarUtils.solarToLunar("20210905"));
    }
}
