package com.github.kancyframework.springx.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SimpleJsonUtils
 *
 * @author huangchengkang
 * @date 2021/8/28 22:31
 */
public class SimpleJsonUtils {

    public static Map<String,Object> parseMap(String jsonString){
        if (StringUtils.isNotBlank(jsonString)
                && jsonString.trim().startsWith("{") && jsonString.trim().endsWith("}")){
            jsonString = jsonString.trim();
            if (jsonString.length() == 2){
                return new HashMap<>();
            }
            String kvStr = jsonString.substring(1,jsonString.length()-1);
            List<String> kvList = StringUtils.toList(kvStr);
            Map<String, Object> map = new HashMap<>();
            kvList.stream().forEach(itemStr->{
                String[] kv = StringUtils.toArray(itemStr, ":");
                map.put(kv[0].replace("\"",""), castValue(kv[1]));
            });
            return map;
        }else {
            return new HashMap<>();
        }
    }

    public static <T> T parseObject(String jsonString, Class<T> beanClass){
        if (ClassUtils.isAssignableFrom(Map.class, beanClass)){
            return (T) parseMap(jsonString);
        }
        Map<String,Object> map = parseMap(jsonString);
        return BeanUtils.mapToBean(map, beanClass);
    }

    /**
     * 转json str
     * @param bean
     * @return
     */
    public static String toJSONString(Object bean){
        if (bean instanceof Map){
            return toJSONString((Map<?, ?>) bean);
        }
        return toJSONString(BeanUtils.beanToMap(bean));
    }

    private static String toJSONString(Map<?, ?> map){
        if (CollectionUtils.isEmpty(map)){
            return "{}";
        }
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (Objects.isNull(value)){
                continue;
            }
            builder.append("\"").append(entry.getKey()).append("\"").append(":");

            if (ClassUtils.isAssignableFrom(Number.class, value.getClass())
                    || Objects.equals(Boolean.class, value.getClass())
                    || Objects.equals(boolean.class, value.getClass())){
                builder.append(value);
            }else {
                builder.append("\"").append(value).append("\"");
            }
            builder.append(",");
        }
        if (builder.length() > 0){
            builder.deleteCharAt(builder.length() -1);
        }
        return String.format("{%s}", builder);
    }


    private static Object castValue(String value) {
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)){
            return Boolean.parseBoolean(value);
        }
        try {
            if (value.contains(".")){
                return Double.parseDouble(value);
            } else {
                return Long.parseLong(value);
            }
        } catch (Exception ex) {
            return value;
        }
    }
}
