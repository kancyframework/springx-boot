package com.kancy.jevel.utils;

import com.kancy.jevel.classloader.MemoryClassLoader;

import java.lang.reflect.Method;

/**
 * JavaDynamicUtils
 *
 * @author huangchengkang
 * @date 2021/9/18 1:34
 */
public class JavaDynamicUtils {
    public static void main(String[] args) throws Exception {
        String src = "package com.kancy.jevel;\n" +
                "\n" +
                "public class A1 {\n" +
                "    public String main() {\n" +
                "        System.out.println(\"Hello22!\");\n" +
                "return \"10\";" +
                "    }\n" +
                "}\n";
        Class<?> aClass = forClass(src);
        Object instance = aClass.newInstance();
        Method main = aClass.getDeclaredMethod("main");
        Object invoke = main.invoke(instance);
        System.out.println(invoke);
    }


    public static Class<?> forClass(String javaSrc) throws ClassNotFoundException {
        MemoryClassLoader loader = MemoryClassLoader.getInstance();
        String className = loader.getClassNameByJavaSrc(javaSrc);
        loader.registerJava(className, javaSrc);
        loader.registerJava(className, javaSrc);
        loader.registerJava(className, javaSrc+"   ");
        return loader.findClass(className);
    }
}
