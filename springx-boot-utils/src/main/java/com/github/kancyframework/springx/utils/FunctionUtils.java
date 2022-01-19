package com.github.kancyframework.springx.utils;


import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * FunctionUtils
 *
 * @author huangchengkang
 * @date 2021/12/7 11:20
 */
public class FunctionUtils {

    private static final Map<String, Field> fieldCache = new HashMap<>();

    @FunctionalInterface
    public interface SerializableFunction<T> extends Function<T, Object>, Serializable {
        Object apply(T t);

        default SerializedLambda getSerializedLambda() throws Exception {
            Method write = this.getClass().getDeclaredMethod("writeReplace");
            write.setAccessible(true);
            return (SerializedLambda) write.invoke(this);
        }

        default String getImplClass() {
            try {
                return getSerializedLambda().getImplClass();
            } catch (Exception e) {
                return null;
            }
        }

        default String getImplMethodName() {
            try {
                return getSerializedLambda().getImplMethodName();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static <T> String getPropertyName(SerializableFunction<T> function) {
        try {
            SerializedLambda lambda = getSerializedLambda(function);
            if (lambda == null) {
                return null;
            } else {
                String methodName = lambda.getImplMethodName();
                return PropertyNamer.methodToProperty(methodName);
            }
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> String getColumnName(SerializableFunction<T> function) {
        try {
            SerializedLambda serializedLambda = getSerializedLambda(function);
            if (serializedLambda == null) {
                return null;
            } else {
                String methodName = serializedLambda.getImplMethodName();
                String propertyName = PropertyNamer.methodToProperty(methodName);
                String className = serializedLambda.getImplClass().replace("/", ".");
                Field field = findFieldFromCache(className, propertyName);
                return getColumnName(field);
            }
        } catch (Exception e) {
            return null;
        }
    }

    private static Field findFieldFromCache(String className, String propertyName) throws ClassNotFoundException {
        String cacheKey = String.format("%s:%s", className, propertyName);
        if (!fieldCache.containsKey(cacheKey)){
            synchronized (fieldCache){
                if (!fieldCache.containsKey(cacheKey)){
                    Class<?> aClass = Class.forName(className);
                    Field field = getField(aClass, propertyName);
                    if (Objects.isNull(field)) {
                        field = getField(aClass, String.format("is%s", firstUpper(propertyName)));
                    }
                    fieldCache.put(cacheKey, field);
                }
            }
        }
        return fieldCache.get(cacheKey);
    }

    private static Field getField(Class<?> aClass, String fieldName) {
        try {
            return aClass.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                return aClass.getField(fieldName);
            } catch (NoSuchFieldException e2) {
                return null;
            }
        }
    }

    private static <T> SerializedLambda getSerializedLambda(SerializableFunction<T> function)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = function.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(true);
        Object obj = method.invoke(function);
        if (!(obj instanceof SerializedLambda)) {
            return null;
        } else {
            SerializedLambda lambda = (SerializedLambda) obj;
            return lambda;
        }
    }

    private static String getColumnName(Field field) {
        String fieldName = field.getName();
        return wrapColumnName(camelCaseToUnderscore(fieldName));
    }

    private static String wrapColumnName(String columnName) {
        return "`".concat(columnName).concat("`");
    }

    private static String camelCaseToUnderscore(String camelCase) {
        StringBuilder builder = new StringBuilder(camelCase.replace('.', '_'));
        for (int i = 1; i < builder.length() - 1; i++) {
            if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i),
                    builder.charAt(i + 1))) {
                builder.insert(i++, '_');
            }
        }

        return builder.toString().toLowerCase();
    }

    private static boolean isUnderscoreRequired(char before, char current, char after) {
        return Character.isLowerCase(before) && Character.isUpperCase(current)
                && Character.isLowerCase(after);
    }

    private static String firstUpper(String str) {
        if (Objects.isNull(str) || str.length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
