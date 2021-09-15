package com.github.kancyframework.springx.swing.themes;

import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.utils.ClassUtils;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * LookAndFeels
 *
 * @author kancy
 * @date 2020/6/13 17:05
 */
public class LookAndFeels {
    private static final Set<UIManager.LookAndFeelInfo> installedLookAndFeelSet = new HashSet<>();

    private static final Map<String, UIManager.LookAndFeelInfo> installedLookAndFeelMap = new HashMap<>();

    static {
        // 安装主题
        installLookAndFeel("SeaGlass", "com.seaglasslookandfeel.SeaGlassLookAndFeel");
        installLookAndFeel("pgs", "com.pagosoft.plaf.PgsLookAndFeel");

        // 不好看的主题
        List<String> excludeLookAndFeels = Arrays.asList(new String[]{"CDE/Motif","Windows Classic","Windows"});
        UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo installedLookAndFeel : installedLookAndFeels) {
            String name = installedLookAndFeel.getName();
            if (!excludeLookAndFeels.contains(name)){
                installedLookAndFeelSet.add(installedLookAndFeel);
                installedLookAndFeelMap.put(name, installedLookAndFeel);
                installedLookAndFeelMap.put(name.toLowerCase(), installedLookAndFeel);
                installedLookAndFeelMap.put(installedLookAndFeel.getClassName(), installedLookAndFeel);
                Log.debug("Install Theme : %s", installedLookAndFeel.getName());
            }
        }
    }

    public static void useRandom(){
        Object[] keys = installedLookAndFeelMap.keySet().toArray();
        int index = ThreadLocalRandom.current().nextInt(0, keys.length);
        UIManager.LookAndFeelInfo lookAndFeelInfo = installedLookAndFeelMap.get(keys[index].toString());
        if (Objects.nonNull(lookAndFeelInfo)){
            setLookAndFeel(lookAndFeelInfo.getClassName());
        }
    }

    public static void useNimbus(){
        setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
    }

    public static void useMetal(){
        setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    }

    public static void useSeaGlass(){
        setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
    }

    public static void useDefault(){
    }


    private static void installLookAndFeel(String name, String className) {
        try {
            Class<?> lookAndFeelClass = Class.forName(className);
            if (ClassUtils.isAssignableFrom(LookAndFeel.class, lookAndFeelClass)){
                UIManager.installLookAndFeel(name, className);
            }
        } catch (Exception e) {
        }
    }

    public static Set<UIManager.LookAndFeelInfo> getInstalledLookAndFeelSet(){
        return installedLookAndFeelSet;
}

    private static void setLookAndFeel(String lookAndFeelClassName) {
        try {
            UIManager.setLookAndFeel(lookAndFeelClassName);
            Log.info("LookAndFeel set : %s", lookAndFeelClassName);
        } catch (Exception e) {
            Log.error("LookAndFeel set fail : %s", e.getMessage());
            useDefault();
        }
    }
}
