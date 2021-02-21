/*
 * Created by JFormDesigner on Sun Feb 21 16:00:43 CST 2021
 */

package com.github.kancyframework.springx.swing.console;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author kancy
 */
public class ConsoleDialog extends JFrame {

    private JTextArea textArea;

    private static ConsoleDialog console;

    static {
        install();
    }

    public static synchronized void install() {
        if (Objects.isNull(console)){
            console = new ConsoleDialog();
            console.setIconImage(new ImageIcon(ConsoleDialog.class.getResource("/images/log_32x32.png")).getImage());
            console.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            console.setSize(1280, 800);
            console.setLocationRelativeTo(null);
        }
    }

    public static void open(){
        if (!console.isVisible()){
            console.setVisible(true);
        }
    }

    public ConsoleDialog() {
        initComponents();
        initConsole();
    }

    private void initConsole() {
        ConsolePrintStream consoleStream = new ConsolePrintStream(System.out, textArea);
        System.setOut(consoleStream);
        System.setErr(consoleStream);
    }

    private void initComponents() {
        JScrollPane scrollPane = new JScrollPane();
        textArea = new JTextArea();
        setTitle("\u63a7\u5236\u53f0");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        pack();
    }

}
