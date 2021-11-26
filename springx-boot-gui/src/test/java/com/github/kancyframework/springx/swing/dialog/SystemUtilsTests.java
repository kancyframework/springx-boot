package com.github.kancyframework.springx.swing.dialog;

import com.github.kancyframework.springx.swing.utils.SystemUtils;
import org.junit.Test;


/**
 * SystemUtilsTests
 *
 * @author huangchengkang
 * @date 2021/11/26 21:08
 */
public class SystemUtilsTests {

    @Test
    public void test01(){
        System.out.println(SystemUtils.getClipboardText());
    }
}
