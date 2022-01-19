package com.github.kancyframework.springx.swing.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.net.URI;

/**
 * SystemUtils
 *
 * @author kancy
 * @date 2020/6/14 10:22
 */
public class SystemUtils {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    public static String getUserName() {
        return System.getenv("USERNAME");
    }

    /**
     * 获取用户主目录
     *
     * @return
     */
    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    /**
     * 用户的当前工作目录
     *
     * @return
     */
    public static String getCurrentWorkDir() {
        return System.getProperty("user.dir");
    }

    /**
     * 获取临时目录
     *
     * @return
     */
    public static String getTmpdir() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * 获取当前系统文件分隔符
     *
     * @return
     */
    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * 获取当前系统行分隔符
     *
     * @return
     */
    public static String getLineSeparator() {
        return System.getProperty("line.separator");
    }

    public static String getAdminUserName() {
        return "Administrator";
    }

    public static boolean isLinux() {
        return OS.indexOf("linux") >= 0;
    }

    public static boolean isMacOS() {
        return OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0;
    }

    public static boolean isWindows() {
        return OS.indexOf("windows") >= 0;
    }

    /**
     * 将字符串复制到剪切板。
     */
    public static void setClipboardText(String writeMe) {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(writeMe);
        clip.setContents(tText, null);
    }

    /**
     * 从剪切板获得文字。
     */
    public static String getClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf == null) {
            return ret;
        }

        // 检查内容是否是文本类型
        if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                ret = (String) clipTf
                        .getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }


    /**
     * 从剪切板获得图片。
     */
    public static Image getImageFromClipboard() throws Exception {
        Clipboard sysc = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cc = sysc.getContents(null);
        if (cc == null)
            return null;
        else if (cc.isDataFlavorSupported(DataFlavor.imageFlavor))
            return (Image) cc.getTransferData(DataFlavor.imageFlavor);
        return null;

    }

    /**
     * 从剪切板获得图片。
     */
    public static byte[] getImageBytesFromClipboard() throws Exception {
        return ImageUtils.getImageBytes(getImageFromClipboard());
    }

    /**
     * 复制图片到剪切板。
     */
    public static void setClipboardImage(final byte[] imageBytes) {
        setClipboardImage(new ImageIcon(imageBytes).getImage());
    }

    /**
     * 复制图片到剪切板。
     */
    public static void setClipboardImage(final Image image) {
        Transferable trans = new Transferable() {
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }

            public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                if (isDataFlavorSupported(flavor))
                    return image;
                throw new UnsupportedFlavorException(flavor);
            }

        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }

    /**
     * 打开默认浏览器
     *
     * @param sURL
     */
    public static void openBrowser(String sURL) {
        try {
            URI uri = new URI(sURL);
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null) {
                desktop.browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
