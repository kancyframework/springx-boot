package com.github.kancyframework.springx.swing;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.swing.dialog.MessageDialog;
import com.github.kancyframework.springx.swing.themes.Themes;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

/**
 * Swing
 *
 * @author kancy
 * @date 2020/2/16 5:07
 */
public class Swing {

    /**
     * 启动
     * @param jFrameClass
     * @param <T>
     */
    public static <T extends JFrame> void startUp(Class<T> jFrameClass){
        SwingUtilities.invokeLater(() -> {
            try {
                JFrame frame = jFrameClass.newInstance();
                startUp(frame);
            } catch (Exception e) {
                Log.error("启动{}失败：{}", jFrameClass.getSimpleName(), e);
                Swing.msg("启动失败！");
            }
        });
    }

    /**
     * Frame默认设置
     * @param frame
     */
    public static void startUp(JFrame frame){
        // frame.setAlwaysOnTop(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        setUIManager();
        if (!frame.isVisible()){
            frame.setVisible(true);
        }
    }

    /**
     * 设置随机的LookAndFeel
     */
    public static void setRandomLookAndFeel() {
        Themes.useRandom();
    }

    /**
     * 设置 LookAndFeel
     * @param lookAndFeelClassName
     */
    public static void setLookAndFeel(String lookAndFeelClassName) {
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
        } catch (Exception e) {
            Log.error(e.getMessage(), e);
        }
    }

    /**
     * 设置默认Ui字体
     */
    public static void setUIFont() {
        Font font = new Font("宋体", Font.PLAIN, 12);
        setUIFont(font);
    }

    /**
     * 设置Ui字体
     * @param font
     */
    public static void setUIFont(Font font) {
        FontUIResource f = new FontUIResource(font);
        Enumeration<?> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource){
                UIManager.put(key, f);
            }
        }
    }

    public static void setUIManager(){
        UIManager.put("OptionPane.titleText", "确认对话框");
        UIManager.put("OptionPane.yesButtonText", "好了");
        UIManager.put("OptionPane.noButtonText", "不行");
        UIManager.put("OptionPane.cancelButtonText", "关闭");
    }

    public static Window findWindow(Component c) {
        if (c == null) {
            return JOptionPane.getRootFrame();
        } else if (c instanceof Window) {
            return (Window) c;
        } else {
            return findWindow(c.getParent());
        }
    }

    public static List<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        List<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container) {
                compList.addAll(getAllComponents((Container) comp));
            }
        }
        return compList;
    }

    /**
     * 提示信息
     * @param msgFormat
     * @param args
     */
    public static void msg(String msgFormat, Object ... args){
        msg(JOptionPane.getRootFrame(), msgFormat, args);
    }

    /**
     * 提示信息
     * @param component
     * @param msgFormat
     * @param args
     */
    public static void msg(Component component, String msgFormat, Object ... args){
        if (Objects.nonNull(msgFormat)){
            msgFormat = msgFormat.replace("{}", "%s");
            Log.warn(msgFormat, args);
            MessageDialog messageDialog = new MessageDialog(component, String.format(msgFormat, args));
            messageDialog.show();
        }
    }
}
