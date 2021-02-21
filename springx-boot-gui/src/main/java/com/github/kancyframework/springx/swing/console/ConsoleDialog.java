package com.github.kancyframework.springx.swing.console;

import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.swing.utils.PopupMenuUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author kancy
 */
public class ConsoleDialog extends JFrame implements ActionListener {

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
        initPopupMenu();
        pack();
    }

    private void initPopupMenu() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenu logLevelMenu = new JMenu("\u65e5\u5fd7\u7ea7\u522b");
        JRadioButtonMenuItem debug = new JRadioButtonMenuItem("debug");
        JRadioButtonMenuItem info = new JRadioButtonMenuItem("info");
        JRadioButtonMenuItem warn = new JRadioButtonMenuItem("warn");
        JRadioButtonMenuItem error = new JRadioButtonMenuItem("error");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(debug);
        buttonGroup.add(info);
        buttonGroup.add(warn);
        buttonGroup.add(error);

        logLevelMenu.add(debug);
        logLevelMenu.add(info);
        logLevelMenu.add(warn);
        logLevelMenu.add(error);
        jPopupMenu.add(logLevelMenu);
        PopupMenuUtils.addPopupMenu(textArea, jPopupMenu);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "debug":
            case "info":
            case "warn":
            case "error":
                LoggerFactory.setLogLevel(e.getActionCommand());
                break;
        }
    }
}
