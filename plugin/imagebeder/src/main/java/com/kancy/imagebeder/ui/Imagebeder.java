/*
 * Created by JFormDesigner on Tue Jan 18 22:52:03 CST 2022
 */

package com.kancy.imagebeder.ui;

import com.github.kancyframework.springx.context.InitializingBean;
import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileDialog;
import com.github.kancyframework.springx.swing.utils.DropTargetUtils;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.kancy.imagebeder.config.ImagebedConfig;
import com.kancy.imagebeder.config.Settings;
import lombok.Data;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * @author kancy
 */
@Data
@Component
public class Imagebeder extends JFrame implements InitializingBean {

    @Autowired
    private Settings settings;

    private static ImageIcon imageIcon;
    static {
        Image img = ImageUtils.createImage("/images/error_view.jpg");
        Image newImg = img.getScaledInstance(855, 450, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newImg);
    }

    public Imagebeder() {
        initComponents();
        label_img.setSize(864,491);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        comboBox_config = new JComboBox();
        button_config = new JButton();
        button_upload = new JButton();
        label_img = new JLabel();
        progressBar = new JProgressBar();
        label_log = new JLabel();
        popupMenu = new JPopupMenu();
        menuItem2 = new JMenuItem();
        menu1 = new JMenu();
        checkBox_link_enabled = new JCheckBox();
        menuItem1 = new JMenuItem();

        //======== this ========
        setTitle("\u7801\u4e91\u56fe\u5e8a v0.1 by kancy");
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[474,fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[175]" +
            "[]"));

        //---- comboBox_config ----
        comboBox_config.setActionCommand("EMPTY");
        contentPane.add(comboBox_config, "cell 0 0");

        //---- button_config ----
        button_config.setText("\u914d\u7f6e\u6e90");
        contentPane.add(button_config, "cell 1 0");

        //---- button_upload ----
        button_upload.setText("\u4e0a\u4f20");
        contentPane.add(button_upload, "cell 2 0");

        //---- label_img ----
        label_img.setBorder(new TitledBorder("\u8bf7\u9009\u62e9\u4e0a\u4f20\u56fe\u7247"));
        label_img.setComponentPopupMenu(popupMenu);
        label_img.setText("<html><p align='center' color='gray'>\u53cc\u51fb\u56fe\u7247\u533a\u57df\u9009\u62e9\u56fe\u7247\u6216\u8005\u62d6\u5230\u56fe\u7247\u5230\u56fe\u7247\u533a\u57df</p></html>");
        label_img.setHorizontalAlignment(SwingConstants.CENTER);
        label_img.setHorizontalTextPosition(SwingConstants.CENTER);
        contentPane.add(label_img, "cell 0 1 4 1,dock center");

        //---- progressBar ----
        progressBar.setPreferredSize(new Dimension(1463, 4));
        progressBar.setBorderPainted(false);
        progressBar.setVisible(false);
        contentPane.add(progressBar, "cell 0 2 4 1");
        pack();
        setLocationRelativeTo(getOwner());

        //---- label_log ----
        label_log.setText("\u63d0\u793a\uff1a\u53cc\u51fb\u56fe\u7247\u533a\u57df\u9009\u62e9\u56fe\u7247\u6216\u8005\u62d6\u5230\u56fe\u7247\u5230\u56fe\u7247\u533a\u57df\u3002");
        label_log.setForeground(new Color(204, 204, 204));

        //======== popupMenu ========
        {

            //---- menuItem2 ----
            menuItem2.setText("\u4e0a\u4f20\u6587\u4ef6");
            menuItem2.setActionCommand("\u4e0a\u4f20");
            popupMenu.add(menuItem2);
            popupMenu.addSeparator();

            //======== menu1 ========
            {
                menu1.setText("\u4e0a\u4f20\u914d\u7f6e");

                //---- checkBox_link_enabled ----
                checkBox_link_enabled.setText("\u5f00\u542f\u8d85\u94fe\u63a5");
                menu1.add(checkBox_link_enabled);
            }
            popupMenu.add(menu1);
            popupMenu.addSeparator();

            //---- menuItem1 ----
            menuItem1.setText("\u4ece\u526a\u5207\u677f\u5bfc\u5165");
            popupMenu.add(menuItem1);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    /**
     * Invoked by the containing {@code BeanFactory} after it has set all bean properties
     * and satisfied, {@code ApplicationContextAware} etc.
     */
    @Override
    public void afterPropertiesSet() {
        init();
        // 初始化comboBox_config
        refreshConfigComboBox();
        // 配置
        checkBox_link_enabled.setSelected(settings.isLinkEnabled());

        if (comboBox_config.getItemCount() == 0){
            Map<String, ImagebedConfig> configs = settings.getConfigs();
            ImagebedConfig imagebedConfig = new ImagebedConfig();
            imagebedConfig.setUsername("kancy666");
            imagebedConfig.setRepoName("images");
            imagebedConfig.setBasePath("upload");
            imagebedConfig.setAccessToken("2d6c78c1e13ff10b0f6a001f0b4a2cdf");
            imagebedConfig.setRemark("默认公开图床");
            imagebedConfig.setBranchName("default");
            configs.put(imagebedConfig.toString(), imagebedConfig);
            refreshConfigComboBox();
        }
    }

    public void refreshConfigComboBox() {
        comboBox_config.removeAllItems();
        Map<String, ImagebedConfig> configs = settings.getConfigs();
        if (!configs.isEmpty()){
            Collection<ImagebedConfig> values = configs.values();
            for (ImagebedConfig value : values) {
                comboBox_config.addItem(new ConfigSourceItem(value));
            }
        }
    }

    private void init() {
        DropTargetUtils.addJavaFileDropTarget(files -> {
            File selectedFile = files.get(0);
            return setLabelImage(selectedFile);
        }, label_img);

        label_img.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == 1){
                    SimpleFileDialog dialog = new SimpleFileDialog();
                    dialog.showOpenDialog(fileDialog -> {
                        setLabelImage(fileDialog.getSelectedFile());
                    });
                }
            }
        });
    }

    public Boolean setLabelImage(File selectedFile) {
        try {
            String path = selectedFile.getAbsolutePath().toLowerCase();
            if (path.matches(".*\\.(png|jpg|jpeg)$")){
                Image img = ImageUtils.createImage(selectedFile);
                Image newImg = img.getScaledInstance(855, 450, Image.SCALE_SMOOTH);
                label_img.setIcon(new ImageIcon(newImg));
                label_img.putClientProperty("path", selectedFile.getAbsolutePath());
                label_img.setText("");
                return true;
            }
            if (path.matches(".*\\.(bmp|gif|svg|webp|psd|ico)$")){
                label_img.setIcon(imageIcon);
                label_img.putClientProperty("path", selectedFile.getAbsolutePath());
                label_img.setText("");
                return true;
            }
            Swing.msg("该文件格式不支持！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JComboBox comboBox_config;
    private JButton button_config;
    private JButton button_upload;
    private JLabel label_img;
    private JProgressBar progressBar;
    private JLabel label_log;
    private JPopupMenu popupMenu;
    private JMenuItem menuItem2;
    private JMenu menu1;
    private JCheckBox checkBox_link_enabled;
    private JMenuItem menuItem1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
