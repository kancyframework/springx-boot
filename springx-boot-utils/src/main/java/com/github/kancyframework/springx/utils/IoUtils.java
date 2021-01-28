package com.github.kancyframework.springx.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * IOUtils
 *
 * @author kancy
 * @date 2020/2/18 19:19
 */
public abstract class IoUtils {

    public static List<String> readLines(InputStream input, String encoding) throws IOException {
        return readLines(input, Charset.forName(encoding));
    }

    public static List<String> readLines(InputStream input, Charset encoding) throws IOException {
        InputStreamReader reader = new InputStreamReader(input, encoding);
        return readLines(reader);
    }

    public static List<String> readLines(Reader input) throws IOException {
        BufferedReader reader = new BufferedReader(input);
        List<String> list = new ArrayList();
        for(String line = reader.readLine(); line != null; line = reader.readLine()) {
            list.add(line);
        }
        return list;
    }

    public static String toString(InputStream input, String encoding) throws IOException {
        return new String(toByteArray(input), encoding);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(input, out);
        return out.toByteArray();
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            byte[] buf = new byte[1024];
            int read = 0;
            while ((read = inputStream.read(buf)) != -1){
                outputStream.write(buf, 0, read);
            }
        } finally {
            closeResource(inputStream);
            closeResource(outputStream);
        }
    }

    /**
     * 关闭资源
     * @param object
     * @throws IOException
     */
    public static void closeResource(Object object) throws IOException {
        if (Objects.nonNull(object)){
            if (InputStream.class.isInstance(object)){
                ((InputStream)object).close();
            }else if(OutputStream.class.isInstance(object)){
                ((OutputStream)object).close();
            }else if(Writer.class.isInstance(object)){
                ((Writer)object).close();
            }else if(Reader.class.isInstance(object)){
                ((Reader)object).close();
            }
        }
    }
}
