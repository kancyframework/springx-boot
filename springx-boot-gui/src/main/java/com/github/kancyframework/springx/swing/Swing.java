package com.github.kancyframework.springx.swing;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.swing.dialog.*;
import com.github.kancyframework.springx.swing.exception.AlertException;
import com.github.kancyframework.springx.swing.themes.Themes;
import com.github.kancyframework.springx.utils.Assert;
import com.github.kancyframework.springx.utils.ClassUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;

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
        if (frame.getWidth() < 200 || frame.getHeight() < 150){
            frame.setSize(new Dimension(600, 400));
        }
        if (StringUtils.isBlank(frame.getTitle())){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            String title = String.format("%s v1.0 by kancy at %s", frame.getClass().getSimpleName(), df.format(new Date()));
            frame.setTitle(title);
        }
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
     * 设置可见
     * @param window
     */
    public static void visible(Window window){
        if (!window.isVisible()){
            window.setVisible(true);
        }else {
            window.requestFocusInWindow();
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

    /**
     * 确定后消费
     * @param consumer
     */
    public static void confirm(Consumer<Boolean> consumer){
        confirm(null, null, consumer);
    }

    /**
     * 确定后消费
     * @param message
     * @param consumer
     */
    public static void confirm(String message, Consumer<Boolean> consumer){
        confirm(null, message, consumer);
    }

    /**
     * 确定后消费
     * @param window
     * @param message
     * @param consumer
     */
    public static void confirm(Window window, String message, Consumer<Boolean> consumer){
        confirm(window, message, consumer, true);
    }

    /**
     * 确定后消费
     * @param window
     * @param message
     * @param consumer
     * @param isOk 为true时才执行？
     */
    public static void confirm(Window window, String message, Consumer<Boolean> consumer, boolean isOk){
        ConfirmDialog dialog = new ConfirmDialog(window);
        if (Objects.nonNull(message)){
            dialog.confirm(message);
        }
        if (isOk && dialog.isOk()){
            consumer.accept(true);
        } else {
            consumer.accept(dialog.isOk() ? Boolean.TRUE : (dialog.isReject() ? Boolean.FALSE : null));
        }
    }

    /**
     * 是否确定
     * @param msgFormat
     * @param args
     * @return
     */
    public static boolean confirm(String msgFormat, Object ... args){
        return confirm(null, msgFormat, args);
    }

    /**
     * 是否确定
     * @param window
     * @param msgFormat
     * @param args
     * @return
     */
    public static boolean confirm(Window window, String msgFormat, Object ... args){
        ConfirmDialog dialog = new ConfirmDialog(window);
        if (Objects.nonNull(msgFormat)){
            msgFormat = msgFormat.replace("{}", "%s");
            msgFormat = String.format(msgFormat, args);
            dialog.confirm(msgFormat);
        } else {
            dialog.confirm();
        }
        return dialog.isOk();
    }

    public static <T> T getInputEnum(String message, Class<? extends Enum> enumClass){
        return getInputEnum(null, message, enumClass);
    }

    public static <T> T getInputEnum(Component component, String message, Class<? extends Enum> enumClass){
        JComboBoxInputDialog inputDialog = new JComboBoxInputDialog(component, message);
        inputDialog.setDataModel(enumClass);
        inputDialog.show();
        return (T) inputDialog.getInputValue();
    }

    public static <T> T getInputPassword(Component component, String message){
        return getInput(JPasswordFieldInputDialog.class, component, message);
    }

    public static String getInputPassword(String message){
        return getInput(JPasswordFieldInputDialog.class, message);
    }

    public static String getInput(String message){
        return getInput(JTextAreaInputDialog.class, message);
    }

    public static String getInput(Component component, String message){
        return getInput(JTextAreaInputDialog.class, component, message);
    }

    public static <T> T getInput(Class<? extends InputDialog> cls, String message){
        return getInput(cls, null, message);
    }

    public static <T> T getInput(Class<? extends InputDialog> cls, Component component, String message){
        return getInput(cls, component, message, null);
    }

    public static <T> T getInput(Class<? extends InputDialog> cls, Component component, String message, T def){
        InputDialog inputDialog = Objects.isNull(component)
                ? ClassUtils.newObject(cls, message) : ClassUtils.newObject(cls, component, message);
        inputDialog.setDefaultValue(def);
        inputDialog.show();
        return (T) inputDialog.getInputValue();
    }

    /**
     * 断言不能不为空
     * @param object 支持的类型：字符串，数组，集合，Map
     * @param message
     */
    public static void assertNotBlank(Object object, String message){
        try {
            Assert.isNotBlank(object, message);
        } catch (IllegalArgumentException e) {
            throw new AlertException(message, e);
        }
    }

    /**
     * 断言不能结果为真
     * @param expression
     * @param message
     */
    public static void assertTrue(boolean expression, String message){
        try {
            Assert.isTrue(expression, message);
        } catch (IllegalArgumentException e) {
            throw new AlertException(message, e);
        }
    }

    /**
     * 添加双击事件
     * @param component
     * @param consumer
     */
    public static void addDoubleClickListener(Component component, Consumer<MouseEvent> consumer){
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1){
                    consumer.accept(e);
                }
            }
        });
    }
}
