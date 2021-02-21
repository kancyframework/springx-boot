/*
 * Created by JFormDesigner on Sun Jan 31 12:22:11 CST 2021
 */

package com.github.kancyframework.springx.demo.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.rsyntax.*;
import lombok.Data;
import net.miginfocom.swing.*;

/**
 * @author kancy
 */
@Data
@Component
public class DemoFrame extends JFrame {
    public DemoFrame() {
        initComponents();
        init();
    }

    private void init() {
        SearchToolBar toolBar = new SearchToolBar(syntaxTextComponent.getTextArea());
        getContentPane().add(toolBar, BorderLayout.NORTH);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menu2 = new JMenu();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menu3 = new JMenu();
        menuItem4 = new JMenuItem();
        menu4 = new JMenu();
        radioButtonMenuItem1 = new JRadioButtonMenuItem();
        radioButtonMenuItem2 = new JRadioButtonMenuItem();
        menu5 = new JMenu();
        menuItem5 = new JMenuItem();
        menuItem6 = new JMenuItem();
        menuItem7 = new JMenuItem();
        panel = new JPanel();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();
        button5 = new JButton();
        button6 = new JButton();
        button7 = new JButton();
        syntaxTextComponent = new RSyntaxTextComponent();

        //======== this ========
        setIconImage(new ImageIcon(getClass().getResource("/house_96px.png")).getImage());
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u83dc\u5355");

                //---- menuItem1 ----
                menuItem1.setText("\u5173\u4e8e");
                menu1.add(menuItem1);
                menu1.addSeparator();

                //======== menu2 ========
                {
                    menu2.setText("\u5b50\u83dc\u5355");

                    //---- menuItem2 ----
                    menuItem2.setText("\u52a8\u4f5c1");
                    menu2.add(menuItem2);

                    //---- menuItem3 ----
                    menuItem3.setText("\u52a8\u4f5c2");
                    menu2.add(menuItem3);
                }
                menu1.add(menu2);
            }
            menuBar1.add(menu1);

            //======== menu3 ========
            {
                menu3.setText("\u8bbe\u7f6e");

                //---- menuItem4 ----
                menuItem4.setText("\u8bbe\u7f6e\u6837\u5f0f");
                menu3.add(menuItem4);

                //======== menu4 ========
                {
                    menu4.setText("\u529f\u80fd\u5f00\u5173");

                    //---- radioButtonMenuItem1 ----
                    radioButtonMenuItem1.setText("\u5f00");
                    menu4.add(radioButtonMenuItem1);

                    //---- radioButtonMenuItem2 ----
                    radioButtonMenuItem2.setText("\u5173");
                    menu4.add(radioButtonMenuItem2);
                }
                menu3.add(menu4);
            }
            menuBar1.add(menu3);

            //======== menu5 ========
            {
                menu5.setText("\u6d4b\u8bd5");

                //---- menuItem5 ----
                menuItem5.setText("\u5f02\u5e38\u6d4b\u8bd5");
                menu5.add(menuItem5);
                menu5.addSeparator();

                //---- menuItem6 ----
                menuItem6.setText("\u7cfb\u7edf\u5c5e\u6027");
                menu5.add(menuItem6);

                //---- menuItem7 ----
                menuItem7.setText("\u63a7\u5236\u53f0");
                menu5.add(menuItem7);
            }
            menuBar1.add(menu5);
        }
        setJMenuBar(menuBar1);

        //======== panel ========
        {
            panel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]"));

            //---- button1 ----
            button1.setText("\u843d");
            panel.add(button1, "cell 0 0,dock center");

            //---- button2 ----
            button2.setText("\u82b1");
            panel.add(button2, "cell 1 0,dock center");

            //---- button3 ----
            button3.setText("\u4e0d");
            panel.add(button3, "cell 2 0,dock center");

            //---- button4 ----
            button4.setText("\u662f");
            panel.add(button4, "cell 3 0,dock center");

            //---- button5 ----
            button5.setText("\u65e0");
            panel.add(button5, "cell 4 0,dock center");

            //---- button6 ----
            button6.setText("\u60c5");
            panel.add(button6, "cell 5 0,dock center");

            //---- button7 ----
            button7.setText("\u7269");
            panel.add(button7, "cell 6 0,dock center");
        }
        contentPane.add(panel, BorderLayout.SOUTH);
        contentPane.add(syntaxTextComponent, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButtonMenuItem1);
        buttonGroup1.add(radioButtonMenuItem2);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenu menu2;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenu menu3;
    private JMenuItem menuItem4;
    private JMenu menu4;
    private JRadioButtonMenuItem radioButtonMenuItem1;
    private JRadioButtonMenuItem radioButtonMenuItem2;
    private JMenu menu5;
    private JMenuItem menuItem5;
    private JMenuItem menuItem6;
    private JMenuItem menuItem7;
    private JPanel panel;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private RSyntaxTextComponent syntaxTextComponent;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
