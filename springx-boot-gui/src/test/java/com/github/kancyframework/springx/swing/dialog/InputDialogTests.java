package com.github.kancyframework.springx.swing.dialog;

import com.github.kancyframework.springx.swing.Swing;
import org.junit.Test;

import javax.swing.*;

/**
 * InputDialogTests
 *
 * @author huangchengkang
 * @date 2021/8/28 18:29
 */
public class InputDialogTests {

    @Test
    public void test01(){
        JFrame jFrame = new JFrame();
        jFrame.setBounds(100,100,400, 300);
        jFrame.setVisible(true);
        Object input = Swing.getInput(JSpinnerInputDialog.class, jFrame, "测试");
        System.out.println(input);
    }

    @Test
    public void test02(){
        JFrame jFrame = new JFrame();
        jFrame.setBounds(200,200,400, 300);
        jFrame.setVisible(true);

        JSpinnerInputDialog dialog = new JSpinnerInputDialog(jFrame, "测试");
        dialog.setInitValue(10);
        dialog.setMaximum(15);
        dialog.setStepSize(2);
        dialog.show();
        System.out.println(dialog.getInputValue());
    }
}
