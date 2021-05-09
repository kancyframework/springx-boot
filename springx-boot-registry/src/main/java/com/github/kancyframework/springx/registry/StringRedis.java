package com.github.kancyframework.springx.registry;

import com.ice.jni.registry.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringRedis implements Redis<String> {

    private final String database;

    private final String tag = "::";

    public StringRedis(String database) {
        this.database = database;
    }

    @Override
    public synchronized void set(String key, String value) {
        if (Objects.isNull(key) || Objects.isNull(value)){
            return;
        }
        RegistryKey redisKey = getDatabaseWriteKey();
        if (key.contains(tag)){
            List<String> keys = Arrays.stream(key.split(tag))
                    .filter(k -> Objects.nonNull(k) && !k.isEmpty())
                    .collect(Collectors.toList());
            if (keys.isEmpty()){
                return;
            }
            if (keys.size() > 1){
                int keyLen = keys.size();
                for (int i = 0; i < keyLen; i++) {
                    String newKey = keys.get(i);
                    if (i < keyLen-2){
                        redisKey = redisKey.createSubKey(newKey);
                    } else if (i < keyLen-1){
                        redisKey = redisKey.createSubKey(newKey, RegistryKey.ACCESS_ALL);
                    }else {
                        key = newKey;
                    }
                }
            }
        }
        redisKey.setValue(key, new RegStringValue(redisKey, key, value));
        redisKey.closeKey();
    }

    @Override
    public synchronized String get(String key) {
        if (Objects.isNull(key)){
            return null;
        }
        try {
            RegistryKey redisKey = getDatabaseReadKey();
            if (key.contains(tag)){
                List<String> keys = Arrays.stream(key.split(tag))
                        .filter(k -> Objects.nonNull(k) && !k.isEmpty())
                        .collect(Collectors.toList());
                if (keys.isEmpty()){
                    return null;
                }
                if (keys.size() > 1){
                    int keyLen = keys.size();
                    for (int i = 0; i < keyLen; i++) {
                        String newKey = keys.get(i);
                        if (i < keyLen-1){
                            redisKey = redisKey.createSubKey(newKey, RegistryKey.ACCESS_READ);
                        }else {
                            key = newKey;
                        }
                    }
                }
            }

            String value = redisKey.getStringValue(key);
            redisKey.closeKey();
            return value;
        } catch (NoSuchValueException e) {
            return null;
        }
    }

    @Override
    public synchronized void delete(String key) {
        if (Objects.isNull(key)){
            return;
        }
        RegistryKey redisKey = getDatabaseWriteKey();
        if (key.contains(tag)){
            List<String> keys = Arrays.stream(key.split(tag))
                    .filter(k -> Objects.nonNull(k) && !k.isEmpty())
                    .collect(Collectors.toList());
            if (keys.isEmpty()){
                return;
            }
            if (keys.size() > 1){
                int keyLen = keys.size();
                for (int i = 0; i < keyLen; i++) {
                    String newKey = keys.get(i);
                    if (i < keyLen-2){
                        redisKey = redisKey.createSubKey(newKey);
                    } else if (i < keyLen-1){
                        redisKey = redisKey.createSubKey(newKey, RegistryKey.ACCESS_ALL);
                    }else {
                        key = newKey;
                    }
                }
            }
        }
        redisKey.deleteValue(key);
        redisKey.closeKey();
    }

    @Override
    public synchronized void clear() {
        try {
            RegistryKey redisKey = getRedisKey();
            redisKey.deleteSubKey(database);
            redisKey.flushKey();
            redisKey.closeKey();
        } catch (RegistryException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDatabase() {
        return this.database;
    }

    @Override
    public synchronized long getSize() {
        try {
            RegistryKey readKey = getDatabaseReadKey();
            int size = readKey.getNumberValues();
            readKey.closeKey();
            return size;
        } catch (RegistryException e) {
            throw new RuntimeException(e);
        }
    }

    protected RegistryKey getDatabaseReadKey() {
        return getDatabaseKey(RegistryKey.ACCESS_READ);
    }

    protected RegistryKey getDatabaseWriteKey() {
        return getDatabaseKey(RegistryKey.ACCESS_ALL);
    }

    protected RegistryKey getDatabaseKey(int access) {
        return getRedisKey().createSubKey(database, access);
    }

    protected RegistryKey getRedisKey() {
        return Registry.HKEY_CURRENT_USER
                .openSubKey("Software\\ODBC\\ODBC.INI")
                .createSubKey("Redis");
    }
}
