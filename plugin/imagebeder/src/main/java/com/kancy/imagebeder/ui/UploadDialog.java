/*
 * Created by JFormDesigner on Wed Jan 19 00:10:07 CST 2022
 */

package com.kancy.imagebeder.ui;

import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.kancy.imagebeder.service.UploadResult;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * @author kancy
 */
public class UploadDialog extends JDialog implements ActionListener {
    private final UploadResult uploadResult;
    public UploadDialog(Window owner, UploadResult uploadResult) {
        super(owner);
        this.uploadResult = uploadResult;
        initComponents();
        initData();
    }

    private void initData() {
        textField_img_name.setText(uploadResult.getFileName());
        textField_img_url.setText(uploadResult.getDownloadUrl());
        textField_html_text.setText(uploadResult.getHtmlText());
        textField_markdown_text.setText(uploadResult.getMarkdownText());

        textField_html_text.setCaretPosition(0);
        textField_markdown_text.setCaretPosition(0);
        textField_img_url.setCaretPosition(0);
        textField_img_name.setCaretPosition(0);

        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        if (Objects.equals(actionCommand, "copy_img_name")){
            SystemUtils.setClipboardText(uploadResult.getFileName());
        }else if (Objects.equals(actionCommand, "copy_img_url")){
            SystemUtils.setClipboardText(uploadResult.getDownloadUrl());
        }else if (Objects.equals(actionCommand, "copy_html_text")){
            SystemUtils.setClipboardText(uploadResult.getHtmlText());
        }else if (Objects.equals(actionCommand, "copy_markdown_text")){
            SystemUtils.setClipboardText(uploadResult.getMarkdownText());
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label4 = new JLabel();
        textField_img_name = new JTextField();
        button1 = new JButton();
        label1 = new JLabel();
        textField_img_url = new JTextField();
        button2 = new JButton();
        label3 = new JLabel();
        textField_html_text = new JTextField();
        button3 = new JButton();
        label2 = new JLabel();
        textField_markdown_text = new JTextField();
        button4 = new JButton();

        //======== this ========
        setTitle("\u4e0a\u4f20\u6210\u529f");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[396,fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label4 ----
        label4.setText("\u56fe\u7247\u540d\u79f0\uff1a");
        contentPane.add(label4, "cell 1 1,alignx right,growx 0");

        //---- textField_img_name ----
        textField_img_name.setPreferredSize(new Dimension(50, 35));
        textField_img_name.setEditable(false);
        contentPane.add(textField_img_name, "cell 2 1");

        //---- button1 ----
        button1.setText("\u590d\u5236");
        button1.setPreferredSize(new Dimension(78, 35));
        button1.setActionCommand("copy_img_name");
        contentPane.add(button1, "cell 3 1");

        //---- label1 ----
        label1.setText("\u56fe\u7247\u94fe\u63a5\uff1a");
        contentPane.add(label1, "cell 1 2,alignx right,growx 0");

        //---- textField_img_url ----
        textField_img_url.setPreferredSize(new Dimension(50, 35));
        textField_img_url.setEditable(false);
        contentPane.add(textField_img_url, "cell 2 2");

        //---- button2 ----
        button2.setText("\u590d\u5236");
        button2.setPreferredSize(new Dimension(78, 35));
        button2.setActionCommand("copy_img_url");
        contentPane.add(button2, "cell 3 2");

        //---- label3 ----
        label3.setText("\u7f51\u9875\u4ee3\u7801\uff1a");
        contentPane.add(label3, "cell 1 3,alignx right,growx 0");

        //---- textField_html_text ----
        textField_html_text.setPreferredSize(new Dimension(50, 35));
        textField_html_text.setEditable(false);
        contentPane.add(textField_html_text, "cell 2 3");

        //---- button3 ----
        button3.setText("\u590d\u5236");
        button3.setPreferredSize(new Dimension(78, 35));
        button3.setActionCommand("copy_html_text");
        contentPane.add(button3, "cell 3 3");

        //---- label2 ----
        label2.setText("Markdown\u4ee3\u7801\uff1a");
        contentPane.add(label2, "cell 1 4,alignx right,growx 0");

        //---- textField_markdown_text ----
        textField_markdown_text.setPreferredSize(new Dimension(50, 35));
        textField_markdown_text.setEditable(false);
        contentPane.add(textField_markdown_text, "cell 2 4");

        //---- button4 ----
        button4.setText("\u590d\u5236");
        button4.setPreferredSize(new Dimension(78, 35));
        button4.setActionCommand("copy_markdown_text");
        contentPane.add(button4, "cell 3 4");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label4;
    private JTextField textField_img_name;
    private JButton button1;
    private JLabel label1;
    private JTextField textField_img_url;
    private JButton button2;
    private JLabel label3;
    private JTextField textField_html_text;
    private JButton button3;
    private JLabel label2;
    private JTextField textField_markdown_text;
    private JButton button4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
