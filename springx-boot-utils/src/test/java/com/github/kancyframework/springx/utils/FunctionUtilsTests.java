package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.utils.dto.User;
import org.junit.Test;

/**
 * FunctionUtilsTests
 *
 * @author huangchengkang
 * @date 2021/12/7 11:32
 */
public class FunctionUtilsTests {
    @Test
    public void test01() throws Exception {
        System.out.println(FunctionUtils.getPropertyName(User::isDeleted));
        System.out.println(FunctionUtils.getPropertyName(User::getUserName));
        System.out.println(FunctionUtils.getColumnName(User::isDeleted));
        System.out.println(FunctionUtils.getColumnName(User::getUserName));
    }
}
