/*
 * Created by JFormDesigner on Sat Jun 12 02:10:19 CST 2021
 */

package com.github.kancyframework.springx.maven.generator.ui;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.utils.DropTargetUtils;
import lombok.Data;
import net.miginfocom.swing.*;

/**
 * @author kancy
 */
@Data
@Component
public class SpringxBootMavenGenerator extends JFrame {
    public SpringxBootMavenGenerator() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        textField = new JTextField();
        button2 = new JButton();
        button1 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[395,fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]"));
        contentPane.add(textField, "cell 0 1,hmin 22");

        //---- button2 ----
        button2.setText("\u9009\u62e9\u8def\u5f84");
        contentPane.add(button2, "cell 1 1");

        //---- button1 ----
        button1.setText("\u751f\u6210");
        contentPane.add(button1, "cell 2 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTextField textField;
    private JButton button2;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
