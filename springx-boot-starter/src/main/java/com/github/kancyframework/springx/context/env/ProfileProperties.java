package com.github.kancyframework.springx.context.env;

import com.github.kancyframework.springx.utils.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ConfigProperties
 *
 * @author kancy
 * @date 2020/2/16 6:49
 */
public class ProfileProperties extends Properties {

    private static final Pattern PLACEHOLDER_REGEX = Pattern.compile("\\$\\{([^}]*)\\}");

    private String name;

    public ProfileProperties(String name) {
        this.name = name;
    }

    public String getStringProperty(String key){
        return getStringProperty(key,null);
    }
    public String getStringProperty(String key, String def){
        String property = System.getenv(key);
        if (Objects.isNull(property)){
            property = System.getProperty(key, getProperty(key, def));
        }
        if (Objects.nonNull(property)){
            Matcher matcher = PLACEHOLDER_REGEX.matcher(property);
            while (matcher.find()){
                String subPropertyName = matcher.group(1).trim();
                String subProperty = getStringProperty(subPropertyName);
                if (Objects.isNull(subProperty)){
                    subProperty = System.getProperty(subPropertyName, System.getenv(subPropertyName));
                }
                property = property.replace(String.format("${%s}", matcher.group(1)), subProperty);
            }
        }
        return property;
    }

    public Boolean getBooleanProperty(String key){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Boolean.parseBoolean(property.trim());
        }
        return null;
    }
    public Boolean getBooleanProperty(String key, Boolean def){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Boolean.parseBoolean(property.trim());
        }
        return def;
    }

    public Integer getIntegerProperty(String key){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Integer.parseInt(property.trim());
        }
        return null;
    }
    public Integer getIntegerProperty(String key, Integer def){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Integer.parseInt(property.trim());
        }
        return def;
    }

    public Long getLongProperty(String key){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Long.parseLong(property.trim());
        }
        return null;
    }
    public Long getLongProperty(String key, Long def){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Long.parseLong(property.trim());
        }
        return def;
    }

    public Double getDoubleProperty(String key){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Double.parseDouble(property.trim());
        }
        return null;
    }
    public Double getDoubleProperty(String key, Double def){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            return Double.parseDouble(property.trim());
        }
        return def;
    }

    public List<String> getListProperty(String key){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            String[] arrays = property.split(",");
            final List<String> list = new ArrayList<>();
            for (String value : arrays) {
                if (StringUtils.isNotBlank(value)){
                    list.add(value);
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    public Set<String> getSetProperty(String key){
        String property = getStringProperty(key);
        if (Objects.nonNull(property)){
            String[] arrays = property.split(",");
            final Set<String> sets = new HashSet<>();
            for (String value : arrays) {
                if (StringUtils.isNotBlank(value)){
                    sets.add(value);
                }
            }
            return sets;
        }
        return Collections.emptySet();
    }

    public void setObjectProperty(String key, Object value){
        setProperty(key, Objects.isNull(value) ? null : String.valueOf(value));
    }

    public void setCollectionProperty(String key, Collection collection){
        if (Objects.nonNull(collection)){
            StringBuffer stringBuffer= new StringBuffer();
            collection.stream().forEach(item ->{
                if (Objects.nonNull(item)){
                    stringBuffer.append(",").append(item);
                }
            });
            if (stringBuffer.length() > 0){
                stringBuffer.deleteCharAt(0);
            }
            setProperty(key, stringBuffer.toString());
        }
    }

    public void setArrayProperty(String key, Object[] array){
        if (Objects.nonNull(array)){
            setCollectionProperty(key, Arrays.asList(array));
        }
    }
}
