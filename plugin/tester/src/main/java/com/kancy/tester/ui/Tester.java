/*
 * Created by JFormDesigner on Thu Aug 26 17:09:06 CST 2021
 */

package com.kancy.tester.ui;

import com.github.kancyframework.springx.context.InitializingBean;
import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.sun.awt.AWTUtilities;
import lombok.Data;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author kancy
 */
@Data
@Component
public class Tester extends JFrame implements InitializingBean {

    @Autowired
    private IdCardPanel idCardPanel;

    @Autowired
    private BankCardPanel bankCardPanel;

    public Tester() {
        initComponents();
        tabbedPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane = new JTabbedPane();
        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(tabbedPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane tabbedPane;

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied, {@code ApplicationContextAware} etc.
     */
    @Override
    public void afterPropertiesSet() {
        tabbedPane.add("身份证", idCardPanel);
        tabbedPane.add("银行卡", bankCardPanel);
    }
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
