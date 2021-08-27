/*
 * Created by JFormDesigner on Fri Aug 27 15:30:40 CST 2021
 */

package com.kancy.tester.ui;

import com.github.kancyframework.springx.context.InitializingBean;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.utils.PopupMenuUtils;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.spring.minidb.MapDb;
import com.kancy.tester.utils.IDCardUtils;
import com.kancy.tester.utils.NameUtils;
import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Objects;

/**
 * @author kancy
 */
@Data
@Component
public class IdCardPanel extends JPanel implements InitializingBean {

    public IdCardPanel() {
        initComponents();
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied, {@code ApplicationContextAware} etc.
     */
    @Override
    public void afterPropertiesSet() {
        idCardImagePanel.add(idCardFrontImagePanel);
        idCardImagePanel.add(idCardBackImagePanel);

        randomCheckBoxActionPerformed(null);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.equals(e.getSource(), frontImage)) {
                    CardLayout layout = (CardLayout) idCardImagePanel.getLayout();
                    layout.next(idCardImagePanel);
                }
                if (Objects.equals(e.getSource(), backImage)) {
                    CardLayout layout = (CardLayout) idCardImagePanel.getLayout();
                    layout.first(idCardImagePanel);
                }
            }
        };
        frontImage.addMouseListener(mouseAdapter);
        backImage.addMouseListener(mouseAdapter);

        PopupMenuUtils.addPopupMenu(frontImage, idCardPopupMenu);
        PopupMenuUtils.addPopupMenu(backImage, idCardPopupMenu);

        setDeFaultValue();
    }

    private void setDeFaultValue() {
        imageNationLabel.setText(MapDb.getData("defaultNation", "汉"));
        String[] strings = StringUtils.toArray(MapDb.getData("idCardAgeRange", "18-50"), "-");
        IDCardUtils.setAgeRange(Integer.parseInt(strings[0]), Integer.parseInt(strings[1]));

    }

    private void randomCheckBoxActionPerformed(ActionEvent e) {
        if (randomCheckBox.isSelected()){
            nameTextField.setEditable(false);
            idCardNoTextField.setEditable(false);
            if (StringUtils.isBlank(nameTextField.getText())){
                nameTextField.setText(NameUtils.fullName());
            }
            if (StringUtils.isBlank(idCardNoTextField.getText())){
                idCardNoTextField.setText(IDCardUtils.create());
            }
        } else {
            nameTextField.setEditable(true);
            idCardNoTextField.setEditable(true);
        }
    }

    private void genButtonActionPerformed(ActionEvent e) {
        if (randomCheckBox.isSelected()){
            nameTextField.setEditable(false);
            idCardNoTextField.setEditable(false);
            nameTextField.setText(NameUtils.fullName());
            idCardNoTextField.setText(IDCardUtils.create());
        } else {
            nameTextField.setEditable(true);
            idCardNoTextField.setEditable(true);
        }

        // 渲染图片
        imageNameLabel.setText(nameTextField.getText());
        String idCard = idCardNoTextField.getText().trim();
        imageIdCardLabel.setText(addSpace(idCard));

        imageYearLabel.setText(idCard.substring(6, 10));

        String monthStr = idCard.substring(10, 12);
        imageMonthLabel.setText(monthStr.startsWith("0")?monthStr.substring(1):monthStr);

        String dayStr = idCard.substring(12, 14);
        imageDayLabel.setText(dayStr.startsWith("0")?dayStr.substring(1):dayStr);

        String sexStr = idCard.substring(16, 17);
        // 调整身份证头像和性别
        if (Integer.parseInt(sexStr) % 2 == 0){
            imageSexLabel.setText("女");
            String filePath = MapDb.getData("defaultGirlHeadPhoto");
            if (!randomCheckBox.isSelected() && StringUtils.isNotBlank(filePath)){
                try {
                    String classPathPrefix = "classpath:";
                    if (filePath.startsWith(classPathPrefix)){
                        String classPath = filePath.replace(classPathPrefix, "");
                        if (!classPath.startsWith("/") ){
                            classPath = "/" + classPath;
                        }
                        getImageHeadPhotoLabel().setIcon(new ImageIcon(getClass().getResource(classPath)));
                    } else {
                        getImageHeadPhotoLabel().setIcon(new ImageIcon(new File(filePath).toURL()));
                    }
                } catch (Exception x) {
                    imageHeadPhotoLabel.setIcon(new ImageIcon(getClass().getResource(
                            String.format("/images/id_card_front_photo_200x200_girl_%s.png",
                                    RandomUtils.nextInt(1, 5)))));
                }
            }else{
                imageHeadPhotoLabel.setIcon(new ImageIcon(getClass().getResource(
                        String.format("/images/id_card_front_photo_200x200_girl_%s.png",
                                RandomUtils.nextInt(1, 5)))));
            }
        }else{
            imageSexLabel.setText("男");

            String filePath = MapDb.getData("defaultBoyHeadPhoto");
            if (!randomCheckBox.isSelected() && StringUtils.isNotBlank(filePath)){
                try {
                    String classPathPrefix = "classpath:";
                    if (filePath.startsWith(classPathPrefix)){
                        String classPath = filePath.replace(classPathPrefix, "");
                        if (!classPath.startsWith("/") ){
                            classPath = "/" + classPath;
                        }
                        getImageHeadPhotoLabel().setIcon(new ImageIcon(getClass().getResource(classPath)));
                    } else {
                        getImageHeadPhotoLabel().setIcon(new ImageIcon(new File(filePath).toURL()));
                    }
                } catch (Exception x) {
                    imageHeadPhotoLabel.setIcon(new ImageIcon(getClass().getResource(
                            String.format("/images/id_card_front_photo_200x200_boy_%s.png",
                                    RandomUtils.nextInt(1, 4)))));
                }
            }else{
                imageHeadPhotoLabel.setIcon(new ImageIcon(getClass().getResource(
                        String.format("/images/id_card_front_photo_200x200_boy_%s.png",
                                RandomUtils.nextInt(1, 4)))));
            }
        }

        imageAddressLabel.setText(IDCardUtils.getAddress(idCard));

        // 反面
        imageQfjgLabel.setText(String.format("%s公安局", IDCardUtils.getCityAndTown(idCard)));
        if (randomCheckBox.isSelected()){
            imageCardValidDateLabel.setText(IDCardUtils.getCardValidDate(idCard));
        }else{
            String cardValidDate = MapDb.getData("defaultIdCardValidDate");
            if(StringUtils.isBlank(cardValidDate)){
                cardValidDate = IDCardUtils.getCardValidDate(idCard);
            }
            imageCardValidDateLabel.setText(cardValidDate);
        }
    }

    private String addSpace(String str){
        return addSpace(str, 1);
    }
    private String addSpace(String str, int len){
        if (Objects.isNull(str)){
            return str;
        }
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char aChar : chars) {
            sb.append(aChar);
            for (int i = 0; i < len; i++) {
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        idCardConfigPanel = new JPanel();
        JLabel label1 = new JLabel();
        nameTextField = new JTextField();
        JLabel label2 = new JLabel();
        idCardNoTextField = new JTextField();
        randomCheckBox = new JCheckBox();
        genButton = new JButton();
        idCardImagePanel = new JPanel();
        idCardFrontImagePanel = new JPanel();
        imageNameLabel = new JLabel();
        imageSexLabel = new JLabel();
        imageNationLabel = new JLabel();
        imageYearLabel = new JLabel();
        imageMonthLabel = new JLabel();
        imageDayLabel = new JLabel();
        imageAddressLabel = new JLabel();
        imageIdCardLabel = new JLabel();
        imageHeadPhotoLabel = new JLabel();
        frontImage = new JLabel();
        idCardBackImagePanel = new JPanel();
        imageQfjgLabel = new JLabel();
        imageCardValidDateLabel = new JLabel();
        backImage = new JLabel();
        idCardPopupMenu = new JPopupMenu();
        menu1 = new JMenu();
        menuItem1 = new JMenuItem();
        menuItem4 = new JMenuItem();
        menuItem6 = new JMenuItem();
        menu2 = new JMenu();
        sexBoyCheckBoxMenuItem = new JCheckBoxMenuItem();
        sexGirlCheckBoxMenuItem = new JCheckBoxMenuItem();
        menuItem7 = new JMenuItem();
        menuItem8 = new JMenuItem();
        menuItem9 = new JMenuItem();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem5 = new JMenuItem();

        //======== this ========
        setLayout(new BorderLayout());

        //======== idCardConfigPanel ========
        {
            idCardConfigPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[46,fill]" +
                "[85,fill]" +
                "[94,fill]" +
                "[175,fill]" +
                "[97,fill]" +
                "[fill]",
                // rows
                "[]" +
                "[]"));

            //---- label1 ----
            label1.setText("\u59d3\u540d");
            idCardConfigPanel.add(label1, "cell 0 1,alignx right,growx 0");

            //---- nameTextField ----
            nameTextField.setText("\u5f20\u4e09\u4e30");
            nameTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            nameTextField.setForeground(Color.black);
            idCardConfigPanel.add(nameTextField, "cell 1 1,hmin 21");

            //---- label2 ----
            label2.setText("\u8eab\u4efd\u8bc1\u53f7\u7801");
            idCardConfigPanel.add(label2, "cell 2 1,alignx right,growx 0");

            //---- idCardNoTextField ----
            idCardNoTextField.setText("350103198207160353");
            idCardNoTextField.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 12));
            idCardNoTextField.setForeground(Color.black);
            idCardConfigPanel.add(idCardNoTextField, "cell 3 1,hmin 21");

            //---- randomCheckBox ----
            randomCheckBox.setText("\u968f\u673a");
            randomCheckBox.setSelected(true);
            randomCheckBox.addActionListener(e -> randomCheckBoxActionPerformed(e));
            idCardConfigPanel.add(randomCheckBox, "cell 4 1,alignx right,growx 0");

            //---- genButton ----
            genButton.setText("\u751f\u6210");
            genButton.addActionListener(e -> genButtonActionPerformed(e));
            idCardConfigPanel.add(genButton, "cell 5 1,dock center");
        }
        add(idCardConfigPanel, BorderLayout.NORTH);

        //======== idCardImagePanel ========
        {
            idCardImagePanel.setLayout(new CardLayout());
        }
        add(idCardImagePanel, BorderLayout.CENTER);

        //======== idCardFrontImagePanel ========
        {
            idCardFrontImagePanel.setLayout(null);

            //---- imageNameLabel ----
            imageNameLabel.setText("\u5f20\u4e09\u4e30");
            imageNameLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageNameLabel.setForeground(Color.black);
            idCardFrontImagePanel.add(imageNameLabel);
            imageNameLabel.setBounds(115, 50, 70, 30);

            //---- imageSexLabel ----
            imageSexLabel.setText("\u7537");
            imageSexLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageSexLabel.setForeground(Color.black);
            idCardFrontImagePanel.add(imageSexLabel);
            imageSexLabel.setBounds(115, 95, 45, 30);

            //---- imageNationLabel ----
            imageNationLabel.setText("\u6c49");
            imageNationLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageNationLabel.setForeground(Color.black);
            idCardFrontImagePanel.add(imageNationLabel);
            imageNationLabel.setBounds(245, 95, 45, 30);

            //---- imageYearLabel ----
            imageYearLabel.setText("1982");
            imageYearLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageYearLabel.setForeground(Color.black);
            idCardFrontImagePanel.add(imageYearLabel);
            imageYearLabel.setBounds(115, 140, 60, 30);

            //---- imageMonthLabel ----
            imageMonthLabel.setText("7");
            imageMonthLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageMonthLabel.setForeground(Color.black);
            idCardFrontImagePanel.add(imageMonthLabel);
            imageMonthLabel.setBounds(215, 140, 35, 30);

            //---- imageDayLabel ----
            imageDayLabel.setText("16");
            imageDayLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageDayLabel.setForeground(Color.black);
            idCardFrontImagePanel.add(imageDayLabel);
            imageDayLabel.setBounds(265, 140, 35, 30);

            //---- imageAddressLabel ----
            imageAddressLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageAddressLabel.setForeground(Color.black);
            imageAddressLabel.setVerticalAlignment(SwingConstants.TOP);
            imageAddressLabel.setText("<html>\u798f\u5efa\u7701\u798f\u5dde\u5e02\u53f0\u6c5f\u533a\u53d7\u6761\u8def\u70ed\u8863\u5c0f\u533a<br/>17\u5355\u51432154\u5ba4</html>");
            idCardFrontImagePanel.add(imageAddressLabel);
            imageAddressLabel.setBounds(115, 190, 280, 95);

            //---- imageIdCardLabel ----
            imageIdCardLabel.setText("3 5 0 1 0 3 1 9 8 2 0 7 1 6 0 3 5 3");
            imageIdCardLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 18));
            imageIdCardLabel.setForeground(Color.black);
            idCardFrontImagePanel.add(imageIdCardLabel);
            imageIdCardLabel.setBounds(200, 305, 300, 35);

            //---- imageHeadPhotoLabel ----
            imageHeadPhotoLabel.setIcon(new ImageIcon(getClass().getResource("/images/id_card_front_photo_200x200_boy_3.png")));
            idCardFrontImagePanel.add(imageHeadPhotoLabel);
            imageHeadPhotoLabel.setBounds(375, 55, 200, 200);

            //---- frontImage ----
            frontImage.setIcon(new ImageIcon(getClass().getResource("/images/id_card_front_600x400.png")));
            frontImage.setToolTipText("\u5355\u51fb\u5207\u6362\u5230\u8eab\u4efd\u8bc1\u53cd\u9762\u54e6\uff01");
            idCardFrontImagePanel.add(frontImage);
            frontImage.setBounds(0, 0, 600, 380);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < idCardFrontImagePanel.getComponentCount(); i++) {
                    Rectangle bounds = idCardFrontImagePanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = idCardFrontImagePanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                idCardFrontImagePanel.setMinimumSize(preferredSize);
                idCardFrontImagePanel.setPreferredSize(preferredSize);
            }
        }

        //======== idCardBackImagePanel ========
        {
            idCardBackImagePanel.setLayout(null);

            //---- imageQfjgLabel ----
            imageQfjgLabel.setText("\u798f\u5dde\u5e02\u53f0\u6c5f\u533a\u516c\u5b89\u5c40");
            imageQfjgLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageQfjgLabel.setForeground(Color.black);
            idCardBackImagePanel.add(imageQfjgLabel);
            imageQfjgLabel.setBounds(260, 265, 320, 30);

            //---- imageCardValidDateLabel ----
            imageCardValidDateLabel.setText("2021.06.05-2041.06.05");
            imageCardValidDateLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 16));
            imageCardValidDateLabel.setForeground(Color.black);
            idCardBackImagePanel.add(imageCardValidDateLabel);
            imageCardValidDateLabel.setBounds(260, 310, 230, 35);

            //---- backImage ----
            backImage.setIcon(new ImageIcon(getClass().getResource("/images/id_card_back_600x400.png")));
            backImage.setToolTipText("\u5355\u51fb\u5207\u6362\u5230\u8eab\u4efd\u8bc1\u6b63\u9762\u54e6\uff01");
            idCardBackImagePanel.add(backImage);
            backImage.setBounds(0, 0, 600, 380);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < idCardBackImagePanel.getComponentCount(); i++) {
                    Rectangle bounds = idCardBackImagePanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = idCardBackImagePanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                idCardBackImagePanel.setMinimumSize(preferredSize);
                idCardBackImagePanel.setPreferredSize(preferredSize);
            }
        }

        //======== idCardPopupMenu ========
        {

            //======== menu1 ========
            {
                menu1.setText("\u8bbe\u7f6e");

                //---- menuItem1 ----
                menuItem1.setText("\u8bbe\u7f6e\u9ed8\u8ba4\u6c11\u65cf");
                menu1.add(menuItem1);
                menu1.addSeparator();

                //---- menuItem4 ----
                menuItem4.setText("\u8bbe\u7f6e\u5e74\u9f84\u533a\u95f4");
                menu1.add(menuItem4);

                //---- menuItem6 ----
                menuItem6.setText("\u8bbe\u7f6e\u8eab\u4efd\u8bc1\u6709\u6548\u671f");
                menu1.add(menuItem6);
                menu1.addSeparator();

                //======== menu2 ========
                {
                    menu2.setText("\u8bbe\u7f6e\u751f\u6210\u6027\u522b");

                    //---- sexBoyCheckBoxMenuItem ----
                    sexBoyCheckBoxMenuItem.setText("\u7537\u6027\u8eab\u4efd\u8bc1");
                    sexBoyCheckBoxMenuItem.setActionCommand("\u8bbe\u7f6e\u751f\u6210\u6027\u522b");
                    sexBoyCheckBoxMenuItem.setSelected(true);
                    menu2.add(sexBoyCheckBoxMenuItem);

                    //---- sexGirlCheckBoxMenuItem ----
                    sexGirlCheckBoxMenuItem.setText("\u5973\u6027\u8eab\u4efd\u8bc1");
                    sexGirlCheckBoxMenuItem.setActionCommand("\u8bbe\u7f6e\u751f\u6210\u6027\u522b");
                    sexGirlCheckBoxMenuItem.setSelected(true);
                    menu2.add(sexGirlCheckBoxMenuItem);
                }
                menu1.add(menu2);
                menu1.addSeparator();

                //---- menuItem7 ----
                menuItem7.setText("\u8bbe\u7f6e\u9ed8\u8ba4\u7537\u5934\u50cf");
                menu1.add(menuItem7);

                //---- menuItem8 ----
                menuItem8.setText("\u8bbe\u7f6e\u9ed8\u8ba4\u5973\u5934\u50cf");
                menu1.add(menuItem8);
            }
            idCardPopupMenu.add(menu1);

            //---- menuItem9 ----
            menuItem9.setText("\u8bbe\u7f6e\u91cd\u7f6e");
            idCardPopupMenu.add(menuItem9);
            idCardPopupMenu.addSeparator();

            //---- menuItem2 ----
            menuItem2.setText("\u590d\u5236\u8eab\u4efd\u8bc1\u53f7\u7801");
            idCardPopupMenu.add(menuItem2);

            //---- menuItem3 ----
            menuItem3.setText("\u590d\u5236\u8eab\u4efd\u8bc1\u4fe1\u606f");
            idCardPopupMenu.add(menuItem3);
            idCardPopupMenu.addSeparator();

            //---- menuItem5 ----
            menuItem5.setText("\u8eab\u4efd\u8bc1\u53e6\u5b58\u4e3a");
            idCardPopupMenu.add(menuItem5);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel idCardConfigPanel;
    private JTextField nameTextField;
    private JTextField idCardNoTextField;
    private JCheckBox randomCheckBox;
    private JButton genButton;
    private JPanel idCardImagePanel;
    private JPanel idCardFrontImagePanel;
    private JLabel imageNameLabel;
    private JLabel imageSexLabel;
    private JLabel imageNationLabel;
    private JLabel imageYearLabel;
    private JLabel imageMonthLabel;
    private JLabel imageDayLabel;
    private JLabel imageAddressLabel;
    private JLabel imageIdCardLabel;
    private JLabel imageHeadPhotoLabel;
    private JLabel frontImage;
    private JPanel idCardBackImagePanel;
    private JLabel imageQfjgLabel;
    private JLabel imageCardValidDateLabel;
    private JLabel backImage;
    private JPopupMenu idCardPopupMenu;
    private JMenu menu1;
    private JMenuItem menuItem1;
    private JMenuItem menuItem4;
    private JMenuItem menuItem6;
    private JMenu menu2;
    private JCheckBoxMenuItem sexBoyCheckBoxMenuItem;
    private JCheckBoxMenuItem sexGirlCheckBoxMenuItem;
    private JMenuItem menuItem7;
    private JMenuItem menuItem8;
    private JMenuItem menuItem9;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenuItem menuItem5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
