package com.ice.jni.registry;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 从jar包中加载dll
 */
public class DllLoader {
    private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");
    private static final String DDL_CLASS_PATH = "/META-INF/dll/";

    public static synchronized void loadFromJar(String libName) throws IOException {
        if (!(libName.endsWith(".dll") || libName.endsWith(".DLL"))){
            libName += ".dll";
        }
        File extractedLibFile = new File(TEMP_PATH + File.separator + libName);
        if(!extractedLibFile.exists()){
            writeDll2TempFile(DDL_CLASS_PATH + libName, extractedLibFile);
            System.out.println("Write " + libName);
        }
        System.load(extractedLibFile.toString());
    }

    private static void writeDll2TempFile(String classPath, File extractedLibFile) throws IOException {
        BufferedInputStream reader = null;
        FileOutputStream writer = null;
        try {
            reader = new BufferedInputStream(DllLoader.class.getResourceAsStream(classPath));
            writer = new FileOutputStream(extractedLibFile);
            byte[] buffer = new byte[1024];
            while (reader.read(buffer) > 0){
                writer.write(buffer);
            }
        } finally {
            if(writer!=null){
                writer.close();
            }
            if(reader!=null){
                reader.close();
            }
        }
    }
}


