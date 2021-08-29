/*
 * Created by JFormDesigner on Sat Aug 28 14:50:30 CST 2021
 */

package com.kancy.tester.ui;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.swing.Swing;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.domain.City;
import com.kancy.tester.domain.District;
import com.kancy.tester.domain.Province;
import com.kancy.tester.domain.Town;
import com.kancy.tester.utils.IDCardUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Objects;

/**
 * @author kancy
 */
public class DistrictDialog extends JDialog {
    private static Province DEFAULT_PROVINCE = new Province("请选择省份");
    private static City DEFAULT_CITY = new City("请选择城市");
    private static Town DEFAULT_TOWN = new Town("请选择县区");

    public DistrictDialog(Window owner) {
        super(owner);
        initComponents();
        initData();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(500, 150));
    }

    private void initData() {
        provinceComboBox.addItem(DEFAULT_PROVINCE);
        cityComboBox.addItem(DEFAULT_CITY);
        townComboBox.addItem(DEFAULT_TOWN);

        for (Province value : District.getInstance().getProvinces().values()) {
            provinceComboBox.addItem(value);
        }

        // 默认勾选
        provinceComboBox.setSelectedItem(District.getInstance().getProvinces().getOrDefault(MapDb.getData("defaultDistrictProvinceCode"), DEFAULT_PROVINCE));
        cityComboBox.setSelectedItem(District.getInstance().getCitys().getOrDefault(MapDb.getData("defaultDistrictCityCode"), DEFAULT_CITY));
        townComboBox.setSelectedItem(District.getInstance().getTowns().getOrDefault(MapDb.getData("defaultDistrictTownCode"), DEFAULT_TOWN));
    }

    private void provinceComboBoxItemStateChanged(ItemEvent e) {
        // 调整城市
        cityComboBox.removeAllItems();
        townComboBox.removeAllItems();

        if (Objects.equals(e.getStateChange(), 2)){
            return;
        }

        if (cityComboBox.getItemCount() == 0){
            cityComboBox.addItem(DEFAULT_CITY);
        }
        if (townComboBox.getItemCount() == 0){
            townComboBox.addItem(DEFAULT_TOWN);
        }

        Province province = (Province) e.getItem();
        if (Objects.equals(province, DEFAULT_PROVINCE)){
            return;
        }

        for (City value : province.getCitys().values()) {
            cityComboBox.addItem(value);
        }
    }

    private void cityComboBoxItemStateChanged(ItemEvent e) {
        townComboBox.removeAllItems();
        if (Objects.equals(e.getStateChange(), 2)){
            return;
        }

        if (townComboBox.getItemCount() == 0){
            townComboBox.addItem(DEFAULT_TOWN);
        }

        City city = (City) e.getItem();
        if (Objects.equals(city, DEFAULT_CITY)){
            return;
        }

        for (Town value : city.getTowns().values()) {
            townComboBox.addItem(value);
        }
    }

    private void townComboBoxItemStateChanged(ItemEvent e) {

    }

    private void okButtonActionPerformed(ActionEvent e) {
        Province province = (Province) provinceComboBox.getSelectedItem();
        City city = (City) cityComboBox.getSelectedItem();
        Town town = (Town) townComboBox.getSelectedItem();
        Log.info("行政区划选择：{}-{}-{}", province, city, town);

        IDCardUtils.setAddressEnabled(
                Objects.equals(province, DEFAULT_PROVINCE) ? null : province,
                Objects.equals(city, DEFAULT_CITY) ? null : city,
                Objects.equals(town, DEFAULT_TOWN) ? null : town
        );

        MapDb.putData("defaultDistrictProvinceCode", province.getProvinceCode());
        MapDb.putData("defaultDistrictCityCode", city.getCityCode());
        MapDb.putData("defaultDistrictTownCode", town.getTownCode());

        dispose();

        Swing.msg(this, "设置成功！");

    }

    private void provinceComboBoxActionPerformed(ActionEvent e) {

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        provinceComboBox = new JComboBox();
        cityComboBox = new JComboBox();
        townComboBox = new JComboBox();
        okButton = new JButton();

        //======== this ========
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

        //---- provinceComboBox ----
        provinceComboBox.addItemListener(e -> provinceComboBoxItemStateChanged(e));
        contentPane.add(provinceComboBox, "cell 1 1,alignx left,growx 0,width 100:0:100");

        //---- cityComboBox ----
        cityComboBox.addItemListener(e -> cityComboBoxItemStateChanged(e));
        contentPane.add(cityComboBox, "cell 2 1,alignx left,growx 0,width 120:0:120");

        //---- townComboBox ----
        townComboBox.addItemListener(e -> townComboBoxItemStateChanged(e));
        contentPane.add(townComboBox, "cell 3 1,alignx left,growx 0,width 150:0:150");

        //---- okButton ----
        okButton.setText("\u786e\u5b9a");
        okButton.addActionListener(e -> okButtonActionPerformed(e));
        contentPane.add(okButton, "cell 4 1,alignx right,growx 0");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComboBox provinceComboBox;
    private JComboBox cityComboBox;
    private JComboBox townComboBox;
    private JButton okButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
