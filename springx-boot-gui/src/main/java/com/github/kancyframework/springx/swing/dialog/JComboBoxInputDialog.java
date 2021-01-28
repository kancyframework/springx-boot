package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * JComboBoxInputDialog
 *
 * @author kancy
 * @date 2020/2/16 14:59
 */
public class JComboBoxInputDialog extends InputDialog {

    private List dataModel = new ArrayList<>();
    private Integer selectedIndex;
    private Object selectedItem;

    public JComboBoxInputDialog() {
    }

    public JComboBoxInputDialog(String inputPrompt) {
        super(inputPrompt);
    }

    public JComboBoxInputDialog(Component parentComponent, String inputPrompt) {
        super(parentComponent, inputPrompt);
    }

    @Override
    protected JComponent getInputComponent() {
        JComboBox<Object> jComboBox = new JComboBox<>();
        for (Object item : dataModel) {
            jComboBox.addItem(item);
        }
        if (Objects.nonNull(selectedIndex)){
            jComboBox.setSelectedIndex(selectedIndex);
        }
        if (Objects.nonNull(selectedItem)){
            jComboBox.setSelectedItem(selectedItem);
        }
        return jComboBox;
    }

    public void setDataModel(Class<? extends Enum> enumClass){
        try {
            Method valuesMethod = enumClass.getMethod("values");
            Enum[] enums = (Enum[]) valuesMethod.invoke(enumClass);
            if (Objects.nonNull(enums)){
                dataModel = new ArrayList<>();
                for (Enum anEnum : enums) {
                    dataModel.add(anEnum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDataModel(Object ... objects){
        if (Objects.nonNull(objects)){
            dataModel = new ArrayList<>();
            for (Object item : objects) {
                dataModel.add(item);
            }
        }
    }

    public void setDataModel(List dataModel) {
        if (Objects.nonNull(dataModel)){
            this.dataModel = new ArrayList<>();
            this.dataModel.addAll(dataModel);
        }
    }

    public Integer getSelectedIndex() {
        return selectedIndex;
    }

    public Object getSelectedItem() {
        return selectedItem;
    }
}
