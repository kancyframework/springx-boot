package com.github.kancyframework.springx.utils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * FileUtils
 *
 * @author kancy
 * @date 2020/2/18 19:16
 */
public abstract class FileUtils {

    public static String readClasspathFileToString(final String classpath, final Charset encoding) throws IOException {
        return new String(readClasspathFileToByteArray(classpath), encoding);
    }

    public static String readClasspathFileToString(final String classpath) throws IOException {
        return readClasspathFileToString(classpath, Charset.defaultCharset());
    }

    public static String readFileToString(final File file, final Charset encoding) throws IOException {
        return new String(readFileToByteArray(file), encoding);
    }

    public static String readFileToString(final File file) throws IOException {
        return new String(readFileToByteArray(file), Charset.defaultCharset());
    }

    public static byte[] readFileToByteArray(final File file) throws IOException {
        InputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IoUtils.copy(in, out);
        return out.toByteArray();
    }

    public static byte[] readClasspathFileToByteArray(final String classpath) throws IOException {
        InputStream in = FileUtils.class.getResourceAsStream(getClasspath(classpath));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IoUtils.copy(in, out);
        return out.toByteArray();
    }

    public static void writeByteArrayToFile(byte[] bytes, final File file) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        FileOutputStream out = new FileOutputStream(file);
        IoUtils.copy(in, out);
    }

    public static List<String> readLines(File file) throws IOException {
        return readLines(file, Charset.defaultCharset().displayName());
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

    public static List<String> readClasspathFileLines(String classpath) throws IOException {
        return readClasspathFileLines(classpath, Charset.defaultCharset().displayName());
    }

    public static List<String> readClasspathFileLines(String classpath, String encoding) throws IOException {
        InputStream fileInputStream = null;
        try {
            fileInputStream = FileUtils.class.getResourceAsStream(getClasspath(classpath));
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

    public static boolean existsFile(String filePath) {
        File file = new File(PathUtils.getFileAbsolutePath(filePath));
        return Objects.nonNull(file) && file.isFile() && file.exists();
    }

    public static boolean existsClassPathFile(String filePath) {
        if (StringUtils.isBlank(filePath)){
            return false;
        }
        String resourcePath = getClasspath(filePath);
        URL resource = FileUtils.class.getResource(resourcePath);
        return Objects.nonNull(resource);
    }

    public static boolean existsFile(File file) {
        return Objects.nonNull(file) && file.isFile() && file.exists();
    }

    public static boolean existsDirectory(File file) {
        return Objects.nonNull(file) && file.isDirectory() && file.exists();
    }

    public static File createNewFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!(file.exists() && file.isFile())){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    public static File createNewDirectory(String filePath) {
        File file = new File(filePath);
        if (!(file.exists() && file.isDirectory())){
            file.mkdirs();
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

    private static String getClasspath(String filePath) {
        filePath = filePath.trim();
        String resourcePath = null;
        if (filePath.startsWith("classpath:/")){
            resourcePath = filePath.substring(10);
        } else if (filePath.startsWith("classpath:")){
            resourcePath = "/".concat(filePath.substring(10));
        }else {
            resourcePath = filePath;
        }
        if (!resourcePath.startsWith("/")){
            resourcePath = "/".concat(resourcePath);
        }
        return resourcePath;
    }

}
