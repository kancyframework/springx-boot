package ${packageName}.ui;

import com.github.kancyframework.springx.context.annotation.Component;

import java.awt.*;
import javax.swing.*;

/**
 * <p>
 * FrameStarter
 * </p>
 *
 * @author kancy
 * @since ${datetime}
 * @description 一个Demo JFrame
 **/
<#noparse>
@Component
public class FrameStarter extends JFrame {
    private JLabel label;

    public FrameStarter() {
        initComponents();
    }

    private void initComponents() {
        label = new JLabel();
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        label.setText("\u8fd9\u53ea\u662f\u4e00\u4e2a\u6848\u4f8b\uff01");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(new Color(204, 204, 204));
        label.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 30));
        contentPane.add(label, BorderLayout.CENTER);
    }
}
</#noparse>