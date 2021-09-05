/*
 * Created by JFormDesigner on Sun Sep 05 22:10:48 CST 2021
 */

package com.kancy.tester.ui;

import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.utils.DateUtils;
import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author kancy
 */
@Data
public class BirthdayDialog extends JDialog {

    private Consumer<Date> dateConsumer;

    private Date defaultDate;

    private String yearRange = "1950-2050";

    public BirthdayDialog(Window owner) {
        this(owner, null);
    }
    public BirthdayDialog(Window owner, Date defaultDate) {
        this(owner, defaultDate, null);
    }

    public BirthdayDialog(Window owner, Date defaultDate, Consumer<Date> dateConsumer) {
        super(owner);
        this.dateConsumer = dateConsumer;
        this.defaultDate = defaultDate;
        initComponents();

        initData();

        setSize(new Dimension(415, 110));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setModal(true);
    }

    private void initData() {
        setYearRange(yearRange);

        monthComboBox.removeAllItems();
        monthComboBox.addItem("请选择月份");
        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(i);
        }

        dayComboBox.removeAllItems();
        dayComboBox.addItem("请选择日期");
        for (int i = 1; i <= 31; i++) {
            dayComboBox.addItem(i);
        }

        setYearRange(yearRange);
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
        setYearRange(yearRange);
    }

    public void setYearRange(String yearRange) {
        this.yearRange = yearRange;
        yearComboBox.removeAllItems();
        yearComboBox.addItem("请选择年份");
        String[] yearRangeArray = yearRange.split("-", 2);
        int minYear = Integer.parseInt(yearRangeArray[0]);
        int maxYear = Integer.parseInt(yearRangeArray[1]);
        for (int i = minYear; i <= maxYear; i++) {
            yearComboBox.addItem(String.valueOf(i));
        }

        if (Objects.nonNull(defaultDate)){
            int year = DateUtils.getYear(defaultDate);
            if (minYear <= year && year <= maxYear){
                String date = DateUtils.getDateStr(defaultDate, "yyyyMMdd");
                yearComboBox.setSelectedItem(date.substring(0, 4));
                monthComboBox.setSelectedItem(Integer.parseInt(date.substring(4, 6)));
                dayComboBox.setSelectedItem(Integer.parseInt(date.substring(6, 8)));
            } else {
                yearComboBox.setSelectedItem("请选择年份");
                monthComboBox.setSelectedItem("请选择月份");
                dayComboBox.setSelectedItem("请选择日期");
            }
        }
    }

    public void showDialog(){
        if (isVisible()){
            return;
        }
        setVisible(true);
    }
    public void showDialog(Consumer<Date> dateConsumer){
        if (isVisible()){
            return;
        }
        this.dateConsumer = dateConsumer;
        setVisible(true);
    }

    private void yearComboBoxActionPerformed(ActionEvent e) {
        try {
            Integer.parseInt((String) yearComboBox.getSelectedItem());
        } catch (Exception exception) {
            yearComboBox.setSelectedItem("请选择年份");
            monthComboBox.setSelectedItem("请选择月份");
            dayComboBox.setSelectedItem("请选择日期");
            return;
        }
    }

    private void monthComboBoxActionPerformed(ActionEvent e) {
        try {
            int year = Integer.parseInt((String) yearComboBox.getSelectedItem());
            int month = (int) monthComboBox.getSelectedItem();
            switch (month){
                case 2:
                    if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                        dayComboBox.removeItem(30);
                        dayComboBox.removeItem(31);
                    }else {
                        dayComboBox.removeItem(29);
                        dayComboBox.removeItem(30);
                        dayComboBox.removeItem(31);
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    dayComboBox.removeItem(29);
                    dayComboBox.removeItem(30);
                    dayComboBox.removeItem(31);
                    dayComboBox.addItem(29);
                    dayComboBox.addItem(30);
                    break;
                default:
                    dayComboBox.removeItem(29);
                    dayComboBox.removeItem(30);
                    dayComboBox.removeItem(31);
                    dayComboBox.addItem(29);
                    dayComboBox.addItem(30);
                    dayComboBox.addItem(31);

            }
        } catch (Exception exception) {
            return;
        }
    }

    private void okButtonActionPerformed(ActionEvent e) {
        int year = 0;
        int month = 0;
        int day = 0;

        try {
            year = Integer.parseInt((String) yearComboBox.getSelectedItem());
        } catch (Exception exception) {
        }
        try {
            month = (int) monthComboBox.getSelectedItem();
        } catch (Exception exception) {
        }
        try {
            day = (int) dayComboBox.getSelectedItem();
        } catch (Exception exception) {
        }

        if (year == 0 && month == 0 && day == 0){
            callback(year, month, day);
            return;
        }
        if (year == 0){
            Swing.msg(this, "请选择年份！");
            return;
        }
        if (month == 0){
            Swing.msg(this, "请选择月份！");
            return;
        }
        if (day == 0){
            Swing.msg(this, "请选择日期！");
            return;
        }

        callback(year, month, day);

        this.dispose();
    }
    private void callback(int year, int month, int day) {
        if (Objects.nonNull(dateConsumer)){
            if (year == 0){
                dateConsumer.accept(null);
                return;
            }
            dateConsumer.accept(DateUtils.toDate(String.format("%02d%02d%02d", year, month, day)));
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        yearComboBox = new JComboBox();
        monthComboBox = new JComboBox();
        dayComboBox = new JComboBox();
        JButton okButton = new JButton();

        //======== this ========
        setTitle("\u65e5\u671f\u9009\u62e9\u5668");
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]"));

        //---- yearComboBox ----
        yearComboBox.addActionListener(e -> yearComboBoxActionPerformed(e));
        contentPane.add(yearComboBox, "cell 1 1");

        //---- monthComboBox ----
        monthComboBox.addActionListener(e -> monthComboBoxActionPerformed(e));
        contentPane.add(monthComboBox, "cell 2 1");
        contentPane.add(dayComboBox, "cell 3 1");

        //---- okButton ----
        okButton.setText("\u786e\u5b9a");
        okButton.addActionListener(e -> okButtonActionPerformed(e));
        contentPane.add(okButton, "cell 4 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComboBox yearComboBox;
    private JComboBox monthComboBox;
    private JComboBox dayComboBox;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
