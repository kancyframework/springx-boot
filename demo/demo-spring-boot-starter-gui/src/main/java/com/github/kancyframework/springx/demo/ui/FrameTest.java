/*
 * Created by JFormDesigner on Mon Feb 22 10:17:37 CST 2021
 */

package com.github.kancyframework.springx.demo.ui;

import com.github.kancyframework.springx.context.annotation.Component;

import java.awt.*;
import javax.swing.*;

/**
 * @author kancy
 */
@Component
public class FrameTest extends JFrame {
    public FrameTest() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        button1 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u83dc\u5355");

                //---- menuItem1 ----
                menuItem1.setText("test");
                menu1.add(menuItem1);
            }
            menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        //---- button1 ----
        button1.setText("test");
        contentPane.add(button1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
