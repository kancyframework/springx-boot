/*
 * Created by JFormDesigner on Sat Jan 09 17:05:07 CST 2021
 */

package com.github.kancyframework.springx.swing.form;

import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.github.kancyframework.springx.utils.ClassUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.Objects;

/**
 * @author kancy
 */
public abstract class TextForm<T extends JTextComponent> extends JComponent implements Value<String> {

    private final String labelName;
    private JTextComponent textComponent;

    private int labelWidth = 50;
    private int inputWidth = 300;
    private int rowHeight = 0;
    private boolean scroll = false;

    public TextForm(String labelName) {
        this(labelName, false);
    }

    public TextForm(String labelName, boolean scroll) {
        this(labelName, scroll, scroll ? 50 : 0);
    }

    public TextForm(String labelName, int labelWidth) {
        this.labelName = labelName;
        this.labelWidth = labelWidth;
        initComponents();
    }

    public TextForm(String labelName, int labelWidth, int inputWidth) {
        this.labelName = labelName;
        this.labelWidth = labelWidth;
        this.inputWidth = inputWidth;
        initComponents();
    }

    public TextForm(String labelName, boolean scroll, int rowHeight) {
        this.labelName = labelName;
        this.scroll = scroll;
        this.rowHeight = rowHeight;
        initComponents();
    }

    public TextForm(String labelName, boolean scroll, int rowHeight, int labelWidth, int inputWidth) {
        this.labelName = labelName;
        this.scroll = scroll;
        this.rowHeight = rowHeight;
        this.labelWidth = labelWidth;
        this.inputWidth = inputWidth;
        initComponents();
    }

    private void initComponents() {
        textComponent = getTextComponent();

        //======== this ========
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {labelWidth, inputWidth, 20};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {rowHeight};

        //---- label ----
        add(new JLabel(labelName), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 5), 0, 0));
        add(scroll ? new JScrollPane(textComponent) : textComponent, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * 设置提示
     * @param message
     */
    public void setTips(String message) {
        JLabel tip= new JLabel(ImageUtils.getQuestionMarkIcon());
        tip.setToolTipText(String.format("<html>%s</html>", message));
        add(tip, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                scroll ? GridBagConstraints.NORTHWEST : GridBagConstraints.WEST, GridBagConstraints.CENTER,
                new Insets(0, 0, 0, 5), 0, 0));
    }

    /**
     * 提供TextComponent
     * @return
     */
    protected T getTextComponent(){
        try {
            Class<T> genericType = ClassUtils.getGenericType(getClass());
            return genericType.newInstance();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Override
    public String getValue() {
        return textComponent.getText();
    }

    @Override
    public void setDefaultValue(String defaultValue) {
        if (Objects.nonNull(defaultValue)){
            textComponent.setText(defaultValue);
        }
    }
}
