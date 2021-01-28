package com.kancy.spring.minidb;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.BeanUtils;
import com.github.kancyframework.springx.utils.FileUtils;
import com.github.kancyframework.springx.utils.PathUtils;
import com.github.kancyframework.springx.utils.SpringUtils;
import com.kancy.spring.minidb.serialize.ObjectDataSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * FileManager
 *
 * @author kancy
 * @date 2021/1/8 22:14
 */
public class ObjectDataManager {

    private static final Logger log = LoggerFactory.getLogger(ObjectDataManager.class);

    private static final String DEFAULT_NS = "swing-boot/minidb";

    private static final String DEFAULT_APP_NAME = System.getProperty("spring.application.name", "application");

    private static final Map<String, ObjectData> OBJECT_CACHE = new ConcurrentHashMap<>();

    private static final ObjectDataSerializer OBJECT_DATA_SERIALIZER = ObjectDataSerializerHolder.get();

    private ObjectDataManager() {
    }

    public static void store(Class<? extends ObjectData> dataClass) {
        store(SpringUtils.getApplicationName(DEFAULT_APP_NAME), dataClass);
    }

    private static void store(String appName, Class<? extends ObjectData> dataClass) {
        String basePath = getUserHome();
        String dataFileName = getDataFileName(appName, dataClass);
        String path = PathUtils.path(basePath, DEFAULT_NS, appName, dataFileName);

        File dataFile = null;
        try {
            dataFile = FileUtils.createNewFile(path);
            ObjectData object = OBJECT_CACHE.get(dataFileName);
            synchronized (object){
                if (ObjectDataService.isProxy(object)){
                    Class<ObjectData> superclass = (Class<ObjectData>) object.getClass().getSuperclass();
                    ObjectData newObject = BeanUtils.copy(object, superclass);
                    OBJECT_DATA_SERIALIZER.write(newObject, new FileOutputStream(dataFile));
                } else {
                    OBJECT_DATA_SERIALIZER.write(object, new FileOutputStream(dataFile));
                }
            }
        } catch (Exception e) {
            FileUtils.deleteFile(dataFile);
            throw new RuntimeException(e);
        }
    }

    public static <T extends ObjectData> T load(Class<T> dataClass) {
        return load(SpringUtils.getApplicationName(DEFAULT_APP_NAME), dataClass);
    }

    private static <T extends ObjectData> T load(String appName, Class<T> dataClass) {
        String basePath = getUserHome();
        String dataFileName = getDataFileName(appName, dataClass);

        T data = null;
        if (OBJECT_CACHE.containsKey(dataFileName)){
            data = (T) OBJECT_CACHE.get(dataFileName);
            log.info("命中[ObjectData]对象数据缓存：{}@{}", dataClass.getName(), data.hashCode());
            return data;
        }

        File dataFile = null;
        try {
            dataFile = new File(PathUtils.path(basePath, DEFAULT_NS, appName, dataFileName));
            if (dataFile.exists() && dataFile.isFile()){
                data = OBJECT_DATA_SERIALIZER.read(new FileInputStream(dataFile), dataClass);
                // 如果使用代理模式
                if (ObjectDataService.isUseProxy(dataClass)){
                    T proxyData = ObjectDataService.initObjectData(dataClass);
                    BeanUtils.copy(data, proxyData);
                    data = proxyData;
                }
            } else {
                data = ObjectDataService.initObjectData(dataClass);
                data.setId(dataFileName);
            }
        } catch (Exception e) {
            FileUtils.deleteFile(dataFile);
            throw new RuntimeException(e);
        }
        // 放入缓存
        OBJECT_CACHE.put(dataFileName, data);
        return data;
    }

    private static String getDataFileName(String appName, Class<?> dataClass) {
        if (ObjectDataService.isProxy(dataClass)){
            dataClass = dataClass.getSuperclass();
        }
        String serializableType = OBJECT_DATA_SERIALIZER.getSerializableType();
        return md5(String.format("%s_%s_%s", appName, serializableType, dataClass.getName()));
    }

    private static String md5(String buffer) {
        String string = null;
        char hexDigist[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(buffer.getBytes());
            //16个字节的长整数
            byte[] datas = md.digest();

            char[] str = new char[2 * 16];
            int k = 0;

            for (int i = 0; i < 16; i++) {
                byte b = datas[i];
                //高4位
                str[k++] = hexDigist[b >>> 4 & 0xf];
                //低4位
                str[k++] = hexDigist[b & 0xf];
            }
            string = new String(str);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return string;
    }

    private static String getUserHome(){
        return System.getProperty("user.home");
    }

}
