package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * OptionDialog
 *
 * @author kancy
 * @date 2020/2/16 15:30
 */
public abstract class OptionDialog {
    private static final Icon icon = new ImageIcon(InputDialog.class.getClassLoader().getResource("images/alert.png"));
    private String title;
    private Component parentComponent;

    protected JOptionPane jOptionPane;
    protected JDialog dialog;
    protected JButton closeButton;

    public OptionDialog() {
        initCloseButton();
        this.title = "\u7cfb\u7edf\u63d0\u793a";
    }

    public OptionDialog(Component parentComponent) {
        this();
        this.parentComponent = parentComponent;
    }

    public OptionDialog(Component parentComponent, String title) {
        this(parentComponent);
        this.title = title;
    }

    public void show(){
        Object object = customizeDialogComponentView();
        jOptionPane = new JOptionPane(object, JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon, new Object[]{});
        jOptionPane.setComponentOrientation(((parentComponent == null) ? JOptionPane.getRootFrame() : parentComponent).getComponentOrientation());
        dialog = jOptionPane.createDialog(this.parentComponent, title);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    /**
     * 自定义组件视图
     * @return
     */
    protected abstract Object customizeDialogComponentView();

    /**
     * 初始化closeButton
     */
    protected void initCloseButton() {
        if (Objects.isNull(closeButton)){
            closeButton = new JButton();
            closeButton.setText("\u5b8c\u6210");
            closeButton.setActionCommand("close");
            closeButton.addActionListener(e -> onCloseDialog(e));
        }
    }

    /**
     * 关闭时触发
     * @param e
     */
    protected void onCloseDialog(ActionEvent e){
        dialog.dispose();
    }

    public String getTitle() {
        return title;
    }

    public Component getParentComponent() {
        return parentComponent;
    }
}
