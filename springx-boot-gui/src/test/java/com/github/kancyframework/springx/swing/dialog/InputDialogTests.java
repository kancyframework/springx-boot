package com.github.kancyframework.springx.swing.dialog;

import com.github.kancyframework.springx.swing.Swing;
import org.junit.Test;

/**
 * InputDialogTests
 *
 * @author huangchengkang
 * @date 2021/8/28 18:29
 */
public class InputDialogTests {

    @Test
    public void test01(){
        Object input = Swing.getInput(JSpinnerInputDialog.class, null, "测试");
        System.out.println(input);
    }
}
