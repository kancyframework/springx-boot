package com.github.kancyframework.springx.swing.console;

import javax.swing.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;

/**
 * Log
 *
 * @author kancy
 * @date 2020/2/16 1:58
 */
public class ConsolePrintStream extends PrintStream {
    private JTextArea text;

    /**
     * 使用方法：
     * ConsolePrintStream consoleStream = new ConsolePrintStream(System.out, text);
     * System.setOut(consoleStream);
     * System.setErr(consoleStream);
     *
     * @param out
     * @param text
     */
    public ConsolePrintStream(OutputStream out, JTextArea text) {
        super(out);
        this.text = text;
    }

    /**
     * 重截write方法,所有的打印方法都要调用的方法
     */
    @Override
    public void write(byte[] buf, int off, int len) {
        final String message = new String(buf, off, len);
        if (Objects.nonNull(this.text)) {
            // SWT非界面线程访问组件的方式
            String newMsg = message.replaceAll("\\u001B\\[\\d+m", "")
                    .replace("\u001B[0m", "");
            SwingUtilities.invokeLater(new Thread(() -> text.append(newMsg)));
        } else {
            super.write(buf, off, len);
        }
    }
}

