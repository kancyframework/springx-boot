package com.github.kancyframework.springx.swing.form;

/**
 * Value
 *
 * @author kancy
 * @date 2021/1/9 17:35
 */
public interface Value<T> {
    T getValue();
    void setDefaultValue(T defaultValue);
}
