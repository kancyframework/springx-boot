package com.kancy.jevel.classloader;

/**
 * MemoryClassLoader
 *
 * @author huangchengkang
 * @date 2021/9/18 1:15
 */

import com.github.kancyframework.springx.utils.Md5Utils;
import com.github.kancyframework.springx.utils.StringUtils;

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 把一段Java字符串变成类
 */
public class MemoryClassLoader extends URLClassLoader {
    private static Logger logger = Logger.getLogger(MemoryClassLoader.class.getSimpleName());

    private Map<String, byte[]> classBytes = new HashMap<>();
    private Map<String, String> srcMd5ClassBytes = new HashMap<>();
    private Map<String, Class<?>> classes = new HashMap<>();
    /**
     * 单利默认的
     */
    private static final MemoryClassLoader defaultLoader = new MemoryClassLoader();

    private MemoryClassLoader() {
        super(new URL[0], MemoryClassLoader.class.getClassLoader());
    }

    /**
     * 获取默认的类加载器
     *
     * @return 类加载器对象
     */
    public static MemoryClassLoader getInstance() {
        return defaultLoader;
    }
    /**
     * 注册Java 字符串到内存类加载器中
     *
     * @param javaStr   Java字符串
     */
    public synchronized Class<?> registerJava(String javaStr) throws ClassNotFoundException {
        if (StringUtils.isBlank(javaStr)){
            return null;
        }
        String className = getClassNameByJavaSrc(javaStr);
        registerJava(className, javaStr);
        Class<?> aClass = findClass(className);
        this.classes.put(className, aClass);
        return aClass;
    }

    /**
     * 注册Java 字符串到内存类加载器中
     *
     * @param className 类名字
     * @param javaStr   Java字符串
     */
    public synchronized void registerJava(String className, String javaStr) {
        if (StringUtils.isBlank(className) || StringUtils.isBlank(javaStr)){
            return;
        }

        javaStr = javaStr.trim();
        String srcMd5 = Md5Utils.md5(javaStr);
        if (Objects.equals(this.srcMd5ClassBytes.get(className), srcMd5)){
            return;
        }
        Map<String, byte[]> compile = compile(className, javaStr);
        if (compile!=null && !compile.isEmpty()){
            this.classBytes.putAll(compile);
            this.srcMd5ClassBytes.put(className, srcMd5);
            logger.info(String.format("重新加载class成功，类名 = %s ，源文件MD5 = %s", className, srcMd5));
        }

    }

    private static Map<String, byte[]> compile(String className, String javaStr) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(className, javaStr);
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
            if (task.call()) {
                return manager.getClassBytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] buf = classBytes.get(name);
        if (buf == null) {
            return super.findClass(name);
        }
        classBytes.remove(name);
        return defineClass(name, buf, 0, buf.length);
    }

    public String getClassNameByJavaSrc(String javaStr) {
        String packageName = getPackageName(javaStr);
        if (packageName.isEmpty()){
            return getPublicClass(javaStr);
        }
        return String.format("%s.%s", packageName, getPublicClass(javaStr));
    }

    private String getPackageName(String fileStr){
        Matcher m= Pattern.compile("(?im)^\\s*package\\s+([^;]+);").matcher(fileStr);
        if(m.find()){
            return m.group(1).trim();
        }
        return "";
    }
    private String getPublicClass(String fileStr){
        Matcher m = Pattern.compile("(?m)^\\s*public\\s+class\\s+(\\w+)\\b").matcher(fileStr);
        if(m.find()){
            return m.group(1).trim();
        }
        throw new IllegalArgumentException("public class name is empty.");
    }
}


/**
 * 内存Java文件管理器
 */
class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

    // compiled classes in bytes:
    final Map<String, byte[]> classBytes = new HashMap<>();

    final Map<String, List<JavaFileObject>> classObjectPackageMap = new HashMap<>();

    MemoryJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    public Map<String, byte[]> getClassBytes() {
        return new HashMap<>(this.classBytes);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        classBytes.clear();
    }

    @Override
    public Iterable<JavaFileObject> list(Location location,
                                         String packageName,
                                         Set<Kind> kinds,
                                         boolean recurse) throws IOException {
        Iterable<JavaFileObject> it = super.list(location, packageName, kinds, recurse);
        if (kinds.contains(Kind.CLASS)) {
            final List<JavaFileObject> javaFileObjectList = classObjectPackageMap.get(packageName);
            if (javaFileObjectList != null) {
                if (it != null) {
                    for (JavaFileObject javaFileObject : it) {
                        javaFileObjectList.add(javaFileObject);
                    }
                }
                return javaFileObjectList;
            } else {
                return it;
            }
        } else {
            return it;
        }
    }

    @Override
    public String inferBinaryName(Location location, JavaFileObject file) {
        if (file instanceof MemoryInputJavaClassObject) {
            return ((MemoryInputJavaClassObject) file).inferBinaryName();
        }
        return super.inferBinaryName(location, file);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, Kind kind,
                                               FileObject sibling) throws IOException {
        if (kind == Kind.CLASS) {
            return new MemoryOutputJavaClassObject(className);
        } else {
            return super.getJavaFileForOutput(location, className, kind, sibling);
        }
    }

    JavaFileObject makeStringSource(String className, final String code) {
        String classPath = className.replace('.', '/') + Kind.SOURCE.extension;

        return new SimpleJavaFileObject(URI.create("string:///" + classPath), Kind.SOURCE) {
            @Override
            public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
                return CharBuffer.wrap(code);
            }
        };
    }

    void makeBinaryClass(String className, final byte[] bs) {
        JavaFileObject javaFileObject = new MemoryInputJavaClassObject(className, bs);

        String packageName = "";
        int pos = className.lastIndexOf('.');
        if (pos > 0) {
            packageName = className.substring(0, pos);
        }
        List<JavaFileObject> javaFileObjectList = classObjectPackageMap.get(packageName);
        if (javaFileObjectList == null) {
            javaFileObjectList = new LinkedList<>();
            javaFileObjectList.add(javaFileObject);

            classObjectPackageMap.put(packageName, javaFileObjectList);
        } else {
            javaFileObjectList.add(javaFileObject);
        }
    }

    static class MemoryInputJavaClassObject extends SimpleJavaFileObject {
        final String className;
        final byte[] bs;

        MemoryInputJavaClassObject(String className, byte[] bs) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
            this.className = className;
            this.bs = bs;
        }

        @Override
        public InputStream openInputStream() {
            return new ByteArrayInputStream(bs);
        }

        public String inferBinaryName() {
            return className;
        }
    }

    class MemoryOutputJavaClassObject extends SimpleJavaFileObject {
        final String className;

        MemoryOutputJavaClassObject(String className) {
            super(URI.create("string:///" + className.replace('.', '/') + Kind.CLASS.extension), Kind.CLASS);
            this.className = className;
        }

        @Override
        public OutputStream openOutputStream() {
            return new FilterOutputStream(new ByteArrayOutputStream()) {
                @Override
                public void close() throws IOException {
                    out.close();
                    ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                    byte[] bs = bos.toByteArray();
                    classBytes.put(className, bs);
                    makeBinaryClass(className, bs);
                }
            };
        }
    }
}