package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * ApplicationContext
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public abstract class ClassUtils {

    private static final Logger log = LoggerFactory.getLogger(ClassUtils.class);

    public static Set<Class<?>> getClassesByPackageName(String packageName, boolean recursively) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return getClassesByPackageName(classLoader, packageName, recursively);
    }

    /**
     * 获取指定包名下的所有类
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassesByPackageName(ClassLoader classLoader, String packageName, boolean recursively) throws IOException {
        Set<Class<?>> classes = new HashSet<>();
        try {
            Enumeration<URL> urls = classLoader.getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if ("file".equals(protocol)) {
                        String packagePath = url.getPath().replaceAll(" ", "");
                        getClassesInPackageUsingFileProtocol(classes, classLoader, packagePath, packageName, recursively);
                    } else if ("jar".equals(protocol)) {
                        getClassesInPackageUsingJarProtocol(classes, classLoader, url, packageName, recursively);
                    } else {
                        log.warn(String.format("protocol[%s] not supported!", protocol));
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    private static void getClassesInPackageUsingJarProtocol(Set<Class<?>> classes, ClassLoader classLoader, URL url, String packageName, boolean recursively) throws IOException {
        String packagePath = packageName.replace(".", "/");
        log.info("---------getClassesInPackageUsingJarProtocol----------");
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        if (jarURLConnection != null) {
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration<JarEntry> jarEntries = jarFile.entries();
            while (jarEntries.hasMoreElements()) {
                JarEntry jarEntry = jarEntries.nextElement();
                String jarEntryName = jarEntry.getName();
                if (jarEntryName.startsWith(packagePath) && jarEntryName.endsWith(".class")) {
                    if (!recursively && jarEntryName.substring(packagePath.length() + 1).contains("/")) {
                        continue;
                    }
                    log.info(jarEntryName);
                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                    classes.add(loadClass(className, false, classLoader));
                }
            }
        }
        log.info("---------getClassesInPackageUsingJarProtocol----------");
    }

    private static void getClassesInPackageUsingFileProtocol(Set<Class<?>> classes, ClassLoader classLoader, String packagePath, String packageName, boolean recursively) {
        final File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class") || file.isDirectory()));
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (!StringUtils.isEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                classes.add(loadClass(className, false, classLoader));
            } else if (recursively) {
                String subPackagePath = fileName;
                if (!StringUtils.isEmpty(subPackagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (!StringUtils.isEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                getClassesInPackageUsingFileProtocol(classes, classLoader, subPackagePath, subPackageName, recursively);
            }
        }
    }

    public static Class<?>[] loadClasses(String ... classNames) {
        List<? extends Class<?>> classList = Arrays.stream(classNames)
                .map(c -> loadClass(c, true, ClassUtils.class.getClassLoader()))
                .collect(Collectors.toList());
        return classList.toArray(new Class[0]);
    }

    /**
     *
     * @param className
     * @param isInitialized
     * @param classLoader
     * @return
     */
    public static Class<?> loadClass(String className, Boolean isInitialized, ClassLoader classLoader) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, isInitialized, classLoader);
        } catch (ClassNotFoundException e) {
            ReflectionUtils.handleReflectionException(e);
        }
        return clazz;
    }

    public static <T> T newObject(Class<T> cls, Object ... args) {
        Class<?>[] paramClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramClasses[i] = args[i].getClass();
        }
        try {
            Constructor constructor = getDeclaredConstructor(cls, paramClasses);
            ReflectionUtils.makeAccessible(constructor);
            return (T) constructor.newInstance(args);
        } catch (Exception e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        throw new IllegalStateException("Should never get here");
    }

    public static Object newObject(String className, Object ... args) {
        Class<?>[] classes = loadClasses(className);
        if (classes.length > 0){
            return newObject(classes[0], args);
        }
        return null;
    }

    public static <T> Constructor getDeclaredConstructor(Class<T> cls, Class<?>[] paramClasses)
            throws NoSuchMethodException {
        Constructor constructor = null;
        try {
            constructor = ReflectionUtils.accessibleConstructor(cls, paramClasses);
        } catch (NoSuchMethodException e) {
            Constructor<?>[] constructors = cls.getDeclaredConstructors();
            for (Constructor<?> c : constructors) {
                if (c.getParameterCount() == paramClasses.length){
                    constructor = c;
                    Class<?>[] parameterTypes = c.getParameterTypes();
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!ClassUtils.isAssignableFrom(parameterTypes[i], paramClasses[i])){
                            constructor = null;
                            break;
                        }
                    }
                }
            }
        }
        if (Objects.isNull(constructor)){
            throw new NoSuchMethodException("Not found available constructor");
        }
        return constructor;
    }


    /**
     * 在类上是否有注解
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static boolean isAnnotationPresentOnClass(Class<?> clazz , Class<? extends Annotation> annotationClass){
        return clazz.isAnnotationPresent(annotationClass);
    }

    /**
     * 在方法上是否有注解
     * @param clazz
     * @param annotationClass
     * @param methodName
     * @param paramTypes
     * @return
     */
    public static boolean isAnnotationPresentOnMethod(Class<?> clazz ,  Class<? extends Annotation> annotationClass, String methodName, Class ... paramTypes){
        Method method = ReflectionUtils.findMethod(clazz, methodName, paramTypes);
        return Objects.nonNull(method) && method.isAnnotationPresent(annotationClass);
    }

    /**
     * 获取父类第一个泛型类型
     * @param srcClass
     * @return
     */
    public static <T> Class<T> getGenericType(Class<?> srcClass){
        return getGenericType(srcClass, 0);
    }

    /**
     * 获取父类第 {genericIndex} 个泛型类型
     * @param srcClass
     * @param genericIndex
     * @return
     */
    public static <T> Class<T> getGenericType(Class<?> srcClass, int genericIndex){
        return (Class<T>) ((ParameterizedType) srcClass.getGenericSuperclass()).getActualTypeArguments()[genericIndex];
    }

    /**
     * 获取第一个接口，第一个泛型类型
     * @param srcClass
     * @return
     */
    public static <T> Class<T> getInterfaceGenericType(Class<?> srcClass){
        return (Class<T>) ((ParameterizedType) srcClass.getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    /**
     * 获取第一个接口，第 {genericIndex}  个泛型类型
     * @param srcClass
     * @return
     */
    public static <T> Class<T> getInterfaceGenericType(Class<?> srcClass, int genericIndex){
        return (Class<T>) ((ParameterizedType) srcClass.getGenericInterfaces()[0]).getActualTypeArguments()[genericIndex];
    }

    /**
     * 获取第 {interfaceIndex} 个接口，第 {genericIndex}  个泛型类型
     * @param srcClass
     * @return
     */
    public static <T> Class<T> getInterfaceGenericType(Class<?> srcClass, int interfaceIndex, int genericIndex){
        return (Class<T>) ((ParameterizedType) srcClass.getGenericInterfaces()[interfaceIndex]).getActualTypeArguments()[genericIndex];
    }

    /**
     * 是否满足继承关系
     * @param supperClass 父类类型
     * @param subClass 子类类型
     * @return
     */
    public static boolean isAssignableFrom(Class supperClass, Class subClass){
        return supperClass.isAssignableFrom(subClass);
    }
}
