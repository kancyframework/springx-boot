package com.github.kancyframework.springx.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * FileUtils
 *
 * @author kancy
 * @date 2020/2/18 19:16
 */
public abstract class FileUtils {

    public static String readFileToString(final File file, final Charset encoding) throws IOException {
        return new String(readFileToByteArray(file), encoding);
    }

    public static byte[] readFileToByteArray(final File file) throws IOException {
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IoUtils.copy(in, out);
        return out.toByteArray();
    }

    public static void writeByteArrayToFile(byte[] bytes, final File file) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        FileOutputStream out = new FileOutputStream(file);
        IoUtils.copy(in, out);
    }

    public static List<String> readLines(File file, String encoding) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            return IoUtils.readLines(fileInputStream, encoding);
        } finally {
            IoUtils.closeResource(fileInputStream);
        }
    }

    public static void writeObject(File file, Serializable object) throws IOException {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(object);
        } finally {
            IoUtils.closeResource(objectOutputStream);
        }
    }

    public static <T extends Serializable> T readObject(File file)
            throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(file));
            return (T) objectInputStream.readObject();
        } finally {
            IoUtils.closeResource(objectInputStream);
        }
    }

    public static File createNewFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!(file.exists() && file.isFile())){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    public static void deleteFile(File dataFile) {
        if (Objects.nonNull(dataFile)){
            boolean delete = dataFile.delete();
            if (!delete){
                dataFile.deleteOnExit();
            }
        }
    }

}
