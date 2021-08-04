package com.github.kancyframework.springx.utils;

import com.ice.jni.registry.RegStringValue;
import com.ice.jni.registry.Registry;
import com.ice.jni.registry.RegistryKey;

public class RegistryUtils {
    private static final String DEFAULT_PATH = String.format("Springx-boot\\%s",
            System.getProperty("spring.application.name" , "common"));

    public static String getValue(String key) {
        return getValue(getCurrentUserKey(), DEFAULT_PATH, key);
    }

    public static void setValue(String key, String value) {
        setValue(getCurrentUserKey(), DEFAULT_PATH, key, value);
    }

    public static void delValue(String key) {
        delValue(getCurrentUserKey(), DEFAULT_PATH, key);
    }

    public static String getValue(String path,
                                      String key) {
        return getValue(getCurrentUserKey(), path, key);
    }

    public static void setValue(String path,
                                    String key, String value) {
        setValue(getCurrentUserKey(), path, key, value);
    }

    public static void delValue(String path, String key) {
        delValue(getCurrentUserKey(), path, key);
    }

    public static void delKey(String path, String node) {
        delKey(getCurrentUserKey(), path, node);
    }

    public static String getValue(RegistryKey rootRegistryKey, String path, String key) {
        RegistryKey subKey = null;
        try {
            subKey = rootRegistryKey.createSubKey(path, RegistryKey.ACCESS_READ);
            return subKey.getStringValue(key);
        } finally {
            if (subKey != null) {
                subKey.closeKey();
            }
        }
    }

    public static void setValue(RegistryKey rootRegistryKey, String path,
                                String key, String value) {
        RegistryKey subKey = rootRegistryKey.createSubKey(path, RegistryKey.ACCESS_ALL);
        subKey.setValue(new RegStringValue(subKey, key, value));
        subKey.closeKey();
    }

    public static void delValue(RegistryKey rootRegistryKey, String path, String key) {
        RegistryKey subKey = rootRegistryKey.createSubKey(path, RegistryKey.ACCESS_ALL);
        subKey.deleteValue(key);
        subKey.closeKey();
    }

    public static void delKey(RegistryKey rootRegistryKey, String path, String node) {
        RegistryKey software = rootRegistryKey.createSubKey(path, RegistryKey.ACCESS_ALL);
        software.deleteSubKey(node);
        software.closeKey();
    }

    public static RegistryKey getCurrentUserKey() {
        return Registry.HKEY_CURRENT_USER;
    }

}
