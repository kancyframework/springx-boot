/*
 * Created by JFormDesigner on Thu Aug 26 15:35:25 CST 2021
 */

package com.kancy.tester.ui;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.utils.PopupMenuUtils;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.kancy.tester.domain.BankCard;
import com.kancy.tester.domain.CardBin;
import com.kancy.tester.service.BankCardService;
import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * @author kancy
 */
@Data
@Component
public class BankCardPanel extends JPanel {

    private BankCardService bankCardService = new BankCardService();

    public BankCardPanel() {
        initComponents();
        genBanckCardButtonActionPerformed(null);

        PopupMenuUtils.addPopupMenu(bankCardImageLabel, bankCardPopupMenu);
    }

    private void genBanckCardButtonActionPerformed(ActionEvent e) {

        Object selectedItem = getBankCardTypeComboBox().getSelectedItem();
        Object searchCardType = Objects.equals(selectedItem, "储蓄卡") ? "DEBIT" :
                Objects.equals(selectedItem, "信用卡") ? "CREDIT" :
                        (RandomUtils.nextInt(10000) % 2 == 0 ? "DEBIT" : "CREDIT");
        Object searchBankName = getBankNameComboBox().getSelectedItem();

        Object indexKey = null;
        if (Objects.equals(searchBankName, "所有")){
            indexKey = searchCardType;
        }else {
            indexKey = String.format("%s@%s", searchBankName, searchCardType);
        }
        BankCard bankCard = bankCardService.generateCard(String.valueOf(indexKey));

        CardBin cardBin = bankCard.getCardBin();
        bankCardNoLabel.setText(bankCardService.formatCardNo(bankCard));
        cardBinLabel.setText(cardBin.getId());
        bankNameLabel.setText(addSpace(cardBin.getBankName()));
        bankCardNameLabel.setText(String.format("%s  %s CARD",
                bankCard.getCardBin().getCardName(),
                bankCard.getCardBin().getCardType()));

    }
    private String addSpace(String str){
        return addSpace(str, 1);
    }
    private String addSpace(String str, int len){
        if (Objects.isNull(str) || str.length() > 5){
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
        bankCardGenConfigPanel = new JPanel();
        JLabel bankCardTypeLabel = new JLabel();
        bankCardTypeComboBox = new JComboBox<>();
        JLabel bankNameConfigLabel = new JLabel();
        bankNameComboBox = new JComboBox<>();
        JButton genBanckCardButton = new JButton();
        bankCardImagePanel = new JPanel();
        bankCardNameLabel = new JLabel();
        bankNameLabel = new JLabel();
        bankCardNoLabel = new JLabel();
        cardBinLabel = new JLabel();
        bankCardImageLabel = new JLabel();
        bankCardPopupMenu = new JPopupMenu();
        menuItem2 = new JMenuItem();
        menuItem3 = new JMenuItem();
        menuItem1 = new JMenuItem();
        menuItem4 = new JMenuItem();

        //======== this ========
        setLayout(new BorderLayout());

        //======== bankCardGenConfigPanel ========
        {
            bankCardGenConfigPanel.setBorder(new EtchedBorder());
            bankCardGenConfigPanel.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[54,fill]" +
                "[131,fill]" +
                "[94,fill]" +
                "[195,fill]" +
                "[fill]" +
                "[fill]",
                // rows
                "[]"));

            //---- bankCardTypeLabel ----
            bankCardTypeLabel.setText("\u5361\u7c7b\u578b");
            bankCardGenConfigPanel.add(bankCardTypeLabel, "cell 0 0,alignx right,growx 0");

            //---- bankCardTypeComboBox ----
            bankCardTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "\u6240\u6709",
                "\u50a8\u84c4\u5361",
                "\u4fe1\u7528\u5361"
            }));
            bankCardTypeComboBox.setActionCommand("empty");
            bankCardGenConfigPanel.add(bankCardTypeComboBox, "cell 1 0");

            //---- bankNameConfigLabel ----
            bankNameConfigLabel.setText("\u94f6\u884c\u540d\u79f0");
            bankCardGenConfigPanel.add(bankNameConfigLabel, "cell 2 0,alignx right,growx 0");

            //---- bankNameComboBox ----
            bankNameComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
                "\u6240\u6709",
                "\u4e2d\u56fd\u94f6\u884c",
                "\u90ae\u50a8\u94f6\u884c",
                "\u62db\u5546\u94f6\u884c",
                "\u5de5\u5546\u94f6\u884c",
                "\u4e2d\u4fe1\u94f6\u884c",
                "\u5efa\u8bbe\u94f6\u884c",
                "\u519c\u4e1a\u94f6\u884c",
                "\u4ea4\u901a\u94f6\u884c",
                "\u5e73\u5b89\u94f6\u884c",
                "\u6c11\u751f\u94f6\u884c",
                "\u5e7f\u53d1\u94f6\u884c",
                "\u5174\u4e1a\u94f6\u884c",
                "\u5317\u4eac\u94f6\u884c",
                "\u4e0a\u6d77\u94f6\u884c",
                "\u534e\u590f\u94f6\u884c",
                "\u6d66\u53d1\u94f6\u884c",
                "\u5149\u5927\u94f6\u884c",
                "\u5176\u4ed6\u94f6\u884c"
            }));
            bankNameComboBox.setActionCommand("empty");
            bankCardGenConfigPanel.add(bankNameComboBox, "cell 3 0");

            //---- genBanckCardButton ----
            genBanckCardButton.setText("\u751f\u6210");
            genBanckCardButton.addActionListener(e -> genBanckCardButtonActionPerformed(e));
            bankCardGenConfigPanel.add(genBanckCardButton, "cell 5 0,dock center");
        }
        add(bankCardGenConfigPanel, BorderLayout.NORTH);

        //======== bankCardImagePanel ========
        {
            bankCardImagePanel.setLayout(null);

            //---- bankCardNameLabel ----
            bankCardNameLabel.setText("\u592a\u5e73\u6d0b\u5546\u4f1a\u5361  DEBIT CARD");
            bankCardNameLabel.setForeground(Color.black);
            bankCardNameLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
            bankCardImagePanel.add(bankCardNameLabel);
            bankCardNameLabel.setBounds(95, 75, 465, 30);

            //---- bankNameLabel ----
            bankNameLabel.setText("\u4ea4 \u901a \u94f6 \u884c");
            bankNameLabel.setForeground(Color.black);
            bankNameLabel.setFont(new Font("\u6977\u4f53", Font.BOLD, 28));
            bankCardImagePanel.add(bankNameLabel);
            bankNameLabel.setBounds(90, 35, 460, 38);

            //---- bankCardNoLabel ----
            bankCardNoLabel.setText("6223 483 0123 01277");
            bankCardNoLabel.setForeground(Color.black);
            bankCardNoLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 24));
            bankCardImagePanel.add(bankCardNoLabel);
            bankCardNoLabel.setBounds(75, 210, 480, 38);

            //---- cardBinLabel ----
            cardBinLabel.setForeground(Color.black);
            cardBinLabel.setText("123456");
            bankCardImagePanel.add(cardBinLabel);
            cardBinLabel.setBounds(95, 330, 100, cardBinLabel.getPreferredSize().height);

            //---- bankCardImageLabel ----
            bankCardImageLabel.setIcon(new ImageIcon(getClass().getResource("/images/bank_card_front_600x400.png")));
            bankCardImagePanel.add(bankCardImageLabel);
            bankCardImageLabel.setBounds(0, 0, bankCardImageLabel.getPreferredSize().width, 377);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < bankCardImagePanel.getComponentCount(); i++) {
                    Rectangle bounds = bankCardImagePanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = bankCardImagePanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                bankCardImagePanel.setMinimumSize(preferredSize);
                bankCardImagePanel.setPreferredSize(preferredSize);
            }
        }
        add(bankCardImagePanel, BorderLayout.CENTER);

        //======== bankCardPopupMenu ========
        {

            //---- menuItem2 ----
            menuItem2.setText("\u590d\u5236\u94f6\u884c\u5361\u53f7\u7801");
            bankCardPopupMenu.add(menuItem2);

            //---- menuItem3 ----
            menuItem3.setText("\u590d\u5236\u94f6\u884c\u5361\u4fe1\u606f");
            bankCardPopupMenu.add(menuItem3);
            bankCardPopupMenu.addSeparator();

            //---- menuItem1 ----
            menuItem1.setText("\u94f6\u884c\u5361\u53e6\u5b58\u4e3a");
            bankCardPopupMenu.add(menuItem1);
            bankCardPopupMenu.addSeparator();

            //---- menuItem4 ----
            menuItem4.setText("\u6279\u91cf\u751f\u6210\u94f6\u884c\u5361");
            bankCardPopupMenu.add(menuItem4);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel bankCardGenConfigPanel;
    private JComboBox<String> bankCardTypeComboBox;
    private JComboBox<String> bankNameComboBox;
    private JPanel bankCardImagePanel;
    private JLabel bankCardNameLabel;
    private JLabel bankNameLabel;
    private JLabel bankCardNoLabel;
    private JLabel cardBinLabel;
    private JLabel bankCardImageLabel;
    private JPopupMenu bankCardPopupMenu;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenuItem menuItem1;
    private JMenuItem menuItem4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
