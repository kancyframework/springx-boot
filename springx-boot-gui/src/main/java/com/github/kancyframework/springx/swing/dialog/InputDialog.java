package com.github.kancyframework.springx.swing.dialog;

import com.github.kancyframework.springx.utils.ObjectUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * InputDialog
 *
 * @author kancy
 * @date 2020/2/16 11:28
 */
public abstract class InputDialog extends OptionDialog {
    private String inputPrompt;
    private JComponent inputComponent;
    private Object inputValue;
    private Object defaultValue;

    public InputDialog() {
        super();
    }

    public InputDialog(String inputPrompt) {
        this();
        this.inputPrompt = inputPrompt;
    }

    public InputDialog(Component parentComponent, String inputPrompt) {
        super(parentComponent);
        this.inputPrompt = inputPrompt;
    }

    public InputDialog(Component parentComponent, String title, String inputPrompt) {
        super(parentComponent, title);
        this.inputPrompt = inputPrompt;
    }

    @Override
    public void show(){
        super.show();
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setInputValue();
                dialog.dispose();
            }
        });
    }

    public void show(Consumer consumer){
        show(consumer, false);
    }

    public void show(Consumer consumer, boolean canBlank){
        this.show();
        Object inputValue = getInputValue();
        if (!canBlank && ObjectUtils.isBlank(inputValue)){
            return;
        }
        if (Objects.equals(inputValue, defaultValue)){
            return;
        }
        consumer.accept(inputValue);
    }

    /**
     * 自定义组件视图
     */
    @Override
    protected Object customizeDialogComponentView() {
        ArrayList list = new ArrayList<>();
        list.add(this.inputPrompt);
        // 初始化
        JComponent inputComponent = getInputComponent();
        if (Objects.nonNull(inputComponent)){
            list.add(inputComponent);
            this.inputComponent = inputComponent;
            doSetDefaultValue();
        }
        list.add(super.closeButton);
        return list.toArray();
    }

    /**
     * 自定义输入组件
     * @return
     */
    protected abstract JComponent getInputComponent();

    /**
     * 关闭时触发
     * @param e
     */
    @Override
    protected void onCloseDialog(ActionEvent e) {
        setInputValue();
        super.onCloseDialog(e);
    }

    /**
     * 设置输入提示
     * @param inputPrompt
     */
    public void setInputPrompt(String inputPrompt) {
        this.inputPrompt = inputPrompt;
    }

    /**
     * getInputValue
     * @return
     */
    public Object getInputValue() {
        return this.inputValue;
    }

    /**
     * setInputValue
     */
    private void setInputValue(){
        this.inputValue = getValue();
    }

    /**
     * 获取输入的值
     * @return
     */
    private Object getValue() {
        if (this.inputComponent instanceof JScrollPane){
            Component component = ((JViewport) inputComponent.getComponent(0)).getView();
            if (component instanceof JTextComponent){
                return ((JTextComponent)component).getText();
            }
        }
        if (this.inputComponent instanceof JTextComponent){
            return ((JTextComponent)this.inputComponent).getText();
        }
        if (this.inputComponent instanceof JComboBox){
            return ((JComboBox)this.inputComponent).getSelectedItem();
        }
        if (this.inputComponent instanceof JSpinner){
            return ((JSpinner)this.inputComponent).getValue();
        }
        if (this.inputComponent instanceof JList){
            return ((JList)this.inputComponent).getSelectedValue();
        }
        if (this.inputComponent instanceof JTable){
            return ((JTable)this.inputComponent).getSelectedRow();
        }
        return null;
    }

    /**
     * 设置默认值
     */
    public void setDefaultValue(Object defaultValue){
        this.defaultValue = defaultValue;
    }

    private void doSetDefaultValue(){
        if (Objects.nonNull(defaultValue)){
            if (Objects.nonNull(inputComponent)){
                if (this.inputComponent instanceof JScrollPane){
                    Component component = ((JViewport) inputComponent.getComponent(0)).getView();
                    if (component instanceof JTextComponent){
                        ((JTextComponent)component).setText(defaultValue.toString());
                    }
                }else  if (this.inputComponent instanceof JTextComponent){
                    ((JTextComponent)this.inputComponent).setText(defaultValue.toString());
                } else if (this.inputComponent instanceof JComboBox){
                    ((JComboBox)this.inputComponent).setSelectedItem(defaultValue);
                } else if (this.inputComponent instanceof JSpinner){
                    ((JSpinner)this.inputComponent).setValue(defaultValue);
                } else if (this.inputComponent instanceof JList){
                    ((JList)this.inputComponent).setSelectedValue(defaultValue, true);
                }
            }
        }
    }

}
