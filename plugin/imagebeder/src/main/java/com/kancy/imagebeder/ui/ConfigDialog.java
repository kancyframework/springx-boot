/*
 * Created by JFormDesigner on Tue Jan 18 23:25:47 CST 2022
 */

package com.kancy.imagebeder.ui;

import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.exception.AlertException;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.github.kancyframework.springx.swing.utils.SystemUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.imagebeder.config.ImagebedConfig;
import com.kancy.imagebeder.service.Giteer;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

/**
 * @author kancy
 */
public class ConfigDialog extends JDialog {

    private ImagebedConfig imagebedConfig;

    private Imagebeder owner;

    public ConfigDialog(Imagebeder owner, ImagebedConfig imagebedConfig) {
        super(owner);
        this.owner = owner;
        this.imagebedConfig = imagebedConfig;
        initComponents();

        if (Objects.nonNull(imagebedConfig)){
            textField_username.setText(imagebedConfig.getUsername());
            passwordField_token.setText(imagebedConfig.getAccessToken());
            textField_repo.setText(imagebedConfig.getRepoName());
            textField_upload_path.setText(imagebedConfig.getBasePath());
            textField_remark.setText(imagebedConfig.getRemark());
            textField_namespace.setText(imagebedConfig.getBranchName());
        }

        label_tip_username.setToolTipText("去申请账号");
        label_tip_username.setIcon(ImageUtils.getQuestionMarkIcon());
        label_tip_token.setToolTipText("去申请私人令牌");
        label_tip_token.setIcon(ImageUtils.getQuestionMarkIcon());
        label_tip_repo.setToolTipText("访问图床仓库");
        label_tip_repo.setIcon(ImageUtils.getQuestionMarkIcon());
        label_tip_namespace.setToolTipText("访问命名空间");
        label_tip_namespace.setIcon(ImageUtils.getQuestionMarkIcon());
        label_tip_upload_path.setToolTipText("访问上传路径");
        label_tip_upload_path.setIcon(ImageUtils.getQuestionMarkIcon());
    }

    public ConfigDialog(Imagebeder owner) {
        this(owner, null);
    }

    public ImagebedConfig getImagebedConfig() {
        return imagebedConfig;
    }

    private void button1ActionPerformed(ActionEvent e) {
        String configKey = getConfigKey();
       if (owner.getSettings().getConfigs().containsKey(configKey)){
           ImagebedConfig remove = owner.getSettings().getConfigs().remove(configKey);
           owner.getSettings().save();
           // 刷新
           owner.refreshConfigComboBox();
           this.dispose();
           Swing.msg(owner, "{} 移除成功！", remove.toString());
       }else {
           Swing.msg(this, "当前配置源不存在，无法移除！");
       }

    }

    private void button_okActionPerformed(ActionEvent e) {
        String username = textField_username.getText().trim();
        String accessToken = passwordField_token.getText().trim();
        String repoName = textField_repo.getText().trim();
        String uploadPath = textField_upload_path.getText().trim();
        String remark = textField_remark.getText().trim();
        String branchName = textField_namespace.getText().trim();

        if (StringUtils.isAnyBlank(username,accessToken,repoName,uploadPath,remark,branchName)){
            Swing.msg(this, "请输入完整的配置！");
            return;
        }

        // 创建git项目
        try {
            Giteer.createProject(accessToken, repoName);
        } catch (IOException ioException) {
            if (!ioException.getMessage().contains("Server returned HTTP response code: 422")){
                throw new AlertException("创建图床仓库失败！");
            }
        }
        try {
            Giteer.createBranch(username, repoName,branchName, accessToken);
        } catch (IOException ioException) {
            if (!ioException.getMessage().contains("Server returned HTTP response code: 400")){
                throw new AlertException("创建图床仓库失败！");
            }
        }

        this.imagebedConfig = new ImagebedConfig();
        this.imagebedConfig.setUsername(username);
        this.imagebedConfig.setAccessToken(accessToken);
        this.imagebedConfig.setRepoName(repoName);
        this.imagebedConfig.setBasePath(uploadPath);
        this.imagebedConfig.setRemark(remark);
        this.imagebedConfig.setBranchName(branchName);

        owner.getSettings().getConfigs().put(imagebedConfig.toString(), imagebedConfig);
        owner.getSettings().save();
        // 刷新
        owner.refreshConfigComboBox();
        this.dispose();
        Swing.msg(owner, "{} 新增/修改成功！", imagebedConfig.toString());
    }

    public String getConfigKey() {
        String username = textField_username.getText().trim();
        String repoName = textField_repo.getText().trim();
        String uploadPath = textField_upload_path.getText().trim();
        String branchName = textField_namespace.getText().trim();

        StringBuffer sb = new StringBuffer();
        sb.append(username).append("@");
        if (uploadPath.startsWith("/")){
            sb.append(repoName).append("@").append(branchName).append(":/").append(uploadPath);
        }else {
            sb.append(repoName).append("@").append(branchName).append("://").append(uploadPath);
        }
        return sb.toString();
    }

    private void label_tip_repoMouseClicked(MouseEvent e) {
        if (StringUtils.isNotBlank(textField_username.getText())
                && StringUtils.isNotBlank(textField_repo.getText())){
            SystemUtils.openBrowser(String.format("https://gitee.com/%s/%s",
                    textField_username.getText().trim(), textField_repo.getText().trim()));
        }else {
            Swing.msg(this, "请输入用户名称和图床仓库");
        }

    }

    private void label_tip_tokenMouseClicked(MouseEvent e) {
        SystemUtils.openBrowser("https://gitee.com/profile/personal_access_tokens");
    }

    private void label_tip_usernameMouseClicked(MouseEvent e) {
        SystemUtils.openBrowser("https://gitee.com/");
    }

    private void label_tip_upload_pathMouseClicked(MouseEvent e) {
        if (StringUtils.isNotBlank(textField_username.getText())
                && StringUtils.isNotBlank(textField_repo.getText())
                && StringUtils.isNotBlank(textField_namespace.getText())
                && StringUtils.isNotBlank(textField_upload_path.getText())){
            SystemUtils.openBrowser(String.format("https://gitee.com/%s/%s/tree/%s/%s",
                    textField_username.getText().trim(),
                    textField_repo.getText().trim(),
                    textField_namespace.getText().trim(),
                    textField_upload_path.getText().trim()));
        } else {
            Swing.msg(this, "请输入用户名称、图床仓库、命名空间和上传路径");
        }
    }

    private void label_tip_namespaceMouseClicked(MouseEvent e) {
        if (StringUtils.isNotBlank(textField_username.getText())
                && StringUtils.isNotBlank(textField_repo.getText())
                && StringUtils.isNotBlank(textField_namespace.getText())){
            SystemUtils.openBrowser(String.format("https://gitee.com/%s/%s/tree/%s",
                    textField_username.getText().trim(),
                    textField_repo.getText().trim(),
                    textField_namespace.getText().trim()));
        } else {
            Swing.msg(this, "请输入用户名称、图床仓库和命名空间");
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        label1 = new JLabel();
        textField_username = new JTextField();
        label_tip_username = new JLabel();
        label2 = new JLabel();
        passwordField_token = new JPasswordField();
        label_tip_token = new JLabel();
        label3 = new JLabel();
        textField_repo = new JTextField();
        label_tip_repo = new JLabel();
        label5 = new JLabel();
        textField_namespace = new JTextField();
        label_tip_namespace = new JLabel();
        label4 = new JLabel();
        textField_upload_path = new JTextField();
        label_tip_upload_path = new JLabel();
        label6 = new JLabel();
        textField_remark = new JTextField();
        button1 = new JButton();
        button_ok = new JButton();

        //======== this ========
        setTitle("\u914d\u7f6e\u56fe\u5e8a\u6e90");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[351,fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- label1 ----
        label1.setText("\u7528\u6237\u540d\u79f0\uff1a");
        contentPane.add(label1, "cell 1 1,alignx right,growx 0");

        //---- textField_username ----
        textField_username.setPreferredSize(new Dimension(50, 35));
        textField_username.setToolTipText("<html>\n<p>1.gitee\u7684\u7528\u6237\u540d</p>\n<p>2.\u5982\u679c\u6ca1\u6709\u53ef\u4ee5\u53bbgitee\u5b98\u7f51\u514d\u8d39\u6ce8\u518c</p>\n</html>");
        contentPane.add(textField_username, "cell 2 1");

        //---- label_tip_username ----
        label_tip_username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_tip_usernameMouseClicked(e);
            }
        });
        contentPane.add(label_tip_username, "cell 3 1");

        //---- label2 ----
        label2.setText("\u8bbf\u95ee\u4ee4\u724c\uff1a");
        contentPane.add(label2, "cell 1 2,alignx right,growx 0");

        //---- passwordField_token ----
        passwordField_token.setPreferredSize(new Dimension(50, 35));
        passwordField_token.setToolTipText("<html>\n<p>\u53ef\u4ee5\u5728\u201c\u4e2a\u4eba\u4e2d\u5fc3/\u5b89\u5168\u8bbe\u7f6e/\u79c1\u4eba\u4ee4\u724c\u201d\u521b\u5efa</p>\n</html>");
        contentPane.add(passwordField_token, "cell 2 2");

        //---- label_tip_token ----
        label_tip_token.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_tip_tokenMouseClicked(e);
            }
        });
        contentPane.add(label_tip_token, "cell 3 2");

        //---- label3 ----
        label3.setText("\u56fe\u5e8a\u4ed3\u5e93\uff1a");
        contentPane.add(label3, "cell 1 3,alignx right,growx 0");

        //---- textField_repo ----
        textField_repo.setPreferredSize(new Dimension(50, 35));
        textField_repo.setText("images");
        textField_repo.setToolTipText("<html>\n<p>1.\u56fe\u5e8a\u4ed3\u5e93\u9879\u76ee\u540d\u79f0\u5efa\u8bae\u4f7f\u7528\u82f1\u6587</p>\n<p>2.\u56fe\u5e8a\u4ed3\u5e93\u9879\u76ee\u4f1a\u81ea\u52a8\u521b\u5efa</p>\n</html>");
        contentPane.add(textField_repo, "cell 2 3");

        //---- label_tip_repo ----
        label_tip_repo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_tip_repoMouseClicked(e);
            }
        });
        contentPane.add(label_tip_repo, "cell 3 3");

        //---- label5 ----
        label5.setText("\u547d\u540d\u7a7a\u95f4\uff1a");
        contentPane.add(label5, "cell 1 4");

        //---- textField_namespace ----
        textField_namespace.setPreferredSize(new Dimension(49, 35));
        textField_namespace.setText("default");
        textField_namespace.setToolTipText("<html>\n<p>1.\u547d\u540d\u7a7a\u95f4\u540d\u79f0\u5efa\u8bae\u4f7f\u7528\u82f1\u6587</p>\n<p>2.\u547d\u540d\u7a7a\u95f4\u4f1a\u81ea\u52a8\u521b\u5efa</p>\n</html>");
        contentPane.add(textField_namespace, "cell 2 4");

        //---- label_tip_namespace ----
        label_tip_namespace.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_tip_namespaceMouseClicked(e);
            }
        });
        contentPane.add(label_tip_namespace, "cell 3 4");

        //---- label4 ----
        label4.setText("\u4e0a\u4f20\u8def\u5f84\uff1a");
        contentPane.add(label4, "cell 1 5,alignx right,growx 0");

        //---- textField_upload_path ----
        textField_upload_path.setPreferredSize(new Dimension(50, 35));
        textField_upload_path.setText("upload");
        textField_upload_path.setToolTipText("\u4e0a\u4f20\u8def\u5f84\u4f5c\u4e3a\u6700\u5c0f\u7684\u5206\u7c7b\u5355\u5143");
        contentPane.add(textField_upload_path, "cell 2 5");

        //---- label_tip_upload_path ----
        label_tip_upload_path.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                label_tip_upload_pathMouseClicked(e);
            }
        });
        contentPane.add(label_tip_upload_path, "cell 3 5");

        //---- label6 ----
        label6.setText("\u56fe\u5e8a\u5907\u6ce8\uff1a");
        contentPane.add(label6, "cell 1 6");

        //---- textField_remark ----
        textField_remark.setPreferredSize(new Dimension(49, 35));
        contentPane.add(textField_remark, "cell 2 6");

        //---- button1 ----
        button1.setText("\u5220\u9664");
        button1.setMaximumSize(new Dimension(78, 35));
        button1.setMinimumSize(new Dimension(78, 35));
        button1.setPreferredSize(new Dimension(78, 35));
        button1.addActionListener(e -> button1ActionPerformed(e));
        contentPane.add(button1, "cell 2 8,alignx right,growx 0");

        //---- button_ok ----
        button_ok.setText("\u4fdd\u5b58");
        button_ok.setPreferredSize(new Dimension(78, 35));
        button_ok.setToolTipText("<html>\n<p>\u65b0\u589e\u6216\u8005\u4fee\u6539</p>\n</html>");
        button_ok.addActionListener(e -> button_okActionPerformed(e));
        contentPane.add(button_ok, "cell 2 8,alignx right,growx 0");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JLabel label1;
    private JTextField textField_username;
    private JLabel label_tip_username;
    private JLabel label2;
    private JPasswordField passwordField_token;
    private JLabel label_tip_token;
    private JLabel label3;
    private JTextField textField_repo;
    private JLabel label_tip_repo;
    private JLabel label5;
    private JTextField textField_namespace;
    private JLabel label_tip_namespace;
    private JLabel label4;
    private JTextField textField_upload_path;
    private JLabel label_tip_upload_path;
    private JLabel label6;
    private JTextField textField_remark;
    private JButton button1;
    private JButton button_ok;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
