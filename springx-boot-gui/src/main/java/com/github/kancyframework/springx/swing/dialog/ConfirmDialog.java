/*
 * Created by JFormDesigner on Fri Jun 11 23:42:04 CST 2021
 */

package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * 确认对话框
 * @author kancy
 */
public class ConfirmDialog extends JDialog {

    private JLabel messageLabel;
    private JButton okBtn;
    private JButton rejectBtn;
    private Boolean result;

    public ConfirmDialog() {
        this(null, null);
    }
    public ConfirmDialog(Window owner) {
        this(owner, null);
    }
    public ConfirmDialog(Boolean result) {
        this(null, result);
    }
    public ConfirmDialog(Window owner, Boolean result) {
        super(owner);
        this.result = result;
        initComponents();
        setSize(new Dimension(300,180));
        setModal(true);
        setResizable(false);
    }

    public void confirm(){
        setVisible(true);
    }

    public void confirm(String message){
        messageLabel.setText(message);
        setVisible(true);
    }

    public void confirm(String title, String message){
        setTitle(title);
        messageLabel.setText(message);
        setVisible(true);
    }

    public boolean isOk(){
        return Objects.equals(result, Boolean.TRUE);
    }

    public boolean isReject(){
        return Objects.equals(result, Boolean.FALSE);
    }

    public boolean isCancel(){
        return Objects.isNull(result);
    }

    private void okBtnActionPerformed(ActionEvent e) {
        result = Boolean.TRUE;
        this.dispose();
    }

    private void rejectBtnActionPerformed(ActionEvent e) {
        result = Boolean.FALSE;
        this.dispose();
    }

    private void initComponents() {
        okBtn = new JButton();
        messageLabel = new JLabel();
        rejectBtn = new JButton();

        setTitle("\u786e\u8ba4\u5bf9\u8bdd\u6846");
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- okBtn ----
        okBtn.setText("<html><font color=green>\u221a\u540c\u610f</font></html>");
        okBtn.addActionListener(e -> okBtnActionPerformed(e));
        contentPane.add(okBtn);
        okBtn.setBounds(new Rectangle(new Point(20, 85), okBtn.getPreferredSize()));

        //---- messageLabel ----
        messageLabel.setText("\u8bf7\u786e\u8ba4\uff1f");
        contentPane.add(messageLabel);
        messageLabel.setBounds(20, 20, 240, 55);

        //---- rejectBtn ----
        rejectBtn.setText("<html><font color=red>X\u62d2\u7edd</font></html>");
        rejectBtn.addActionListener(e -> rejectBtnActionPerformed(e));
        contentPane.add(rejectBtn);
        rejectBtn.setBounds(new Rectangle(new Point(100, 85), rejectBtn.getPreferredSize()));

        // compute preferred size
        Dimension preferredSize = new Dimension();
        for(int i = 0; i < contentPane.getComponentCount(); i++) {
            Rectangle bounds = contentPane.getComponent(i).getBounds();
            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
        }
        Insets insets = contentPane.getInsets();
        preferredSize.width += insets.right;
        preferredSize.height += insets.bottom;
        contentPane.setMinimumSize(preferredSize);
        contentPane.setPreferredSize(preferredSize);
        setLocationRelativeTo(getOwner());

    }
}
