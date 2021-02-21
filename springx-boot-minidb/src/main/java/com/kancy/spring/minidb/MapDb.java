package com.kancy.spring.minidb;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * MapDb
 *
 * @author kancy
 * @date 2021/1/8 22:27
 */
public final class MapDb extends ObjectData implements Serializable {

    private static final long serialVersionUID = 1L;

    private static MapDb mapDb;

    private final Map<String, Serializable> properties = new HashMap<>();

    private MapDb() {
    }

    public static MapDb get(){
        if (Objects.isNull(mapDb)){
            synchronized (MapDb.class){
                if (Objects.isNull(mapDb)){
                    mapDb = ObjectDataManager.load(MapDb.class);
                }
            }
        }
        return mapDb;
    }

    public static Serializable putData(String key, Serializable value) {
        return get().put(key, value);
    }

    public static void beginTransaction() {
        get().tx();
    }

    public static void commitTransaction() {
        get().commit();
    }

    public static <T extends Serializable> T getData(String key) {
        return get().get(key);
    }

    public static <T extends Serializable> T getData(String key, T defaultValue) {
        return get().getOrDefault(key, defaultValue);
    }

    public static Serializable removeData(String key) {
        return get().remove(key);
    }

    public static boolean hasData() {
        return !get().isEmpty();
    }

    public static int getSize() {
        return get().size();
    }

    public static void clearAll() {
        get().clear();
    }

    public Serializable put(String key, Serializable value) {
        Serializable object = properties.put(key, value);
        store();
        return object;
    }

    public int size() {
        return properties.size();
    }

    @JSONField(serialize = false)
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public <T extends Serializable> T get(String key) {
        return (T) properties.get(key);
    }

    public <T extends Serializable> T getOrDefault(String key, T defaultValue) {
        return (T) properties.getOrDefault(key, defaultValue);
    }

    public boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    @JSONField(serialize = false)
    public Set<String> getKeys() {
        return properties.keySet();
    }

    public Set<Map.Entry<String, Serializable>> entrySet() {
        return properties.entrySet();
    }

    public void putAll(Map<String, Serializable> m) {
        properties.putAll(m);
        store();
    }

    public Serializable remove(String key) {
        Serializable remove = properties.remove(key);
        store();
        return remove;
    }

    public void clear() {
        properties.clear();
        store();
    }

    public Serializable putIfAbsent(String key, Serializable value) {
        Serializable o = properties.putIfAbsent(key, value);
        store();
        return o;
    }

    public boolean remove(Object key, Serializable value) {
        boolean remove = properties.remove(key, value);
        if (remove){
            store();
        }
        return remove;
    }

    public boolean replace(String key, Serializable oldValue, Serializable newValue) {
        boolean replace = properties.replace(key, oldValue, newValue);
        if (replace && !Objects.equals(oldValue, newValue)){
            store();
        }
        return replace;
    }

    public Serializable replace(String key, Serializable value) {
        Serializable oldValue = properties.replace(key, value);
        if (!Objects.equals(oldValue, value)){
            store();
        }
        return oldValue;
    }

    public Map<String, Serializable> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return properties.toString();
    }

    private void store() {
        save();
    }
}
