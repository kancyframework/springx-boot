package com.github.kancyframework.springx.swing.action;

import java.lang.annotation.*;

/**
 * 配置Bean的注解
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface KeyStroke {

    /**
     * 快捷键
     * If typed, pressed or released is not specified, pressed is assumed. Here
     * are some examples:
     *
     * ctrl shift 1 , alt shift 2 , alt 1 , ctrl 3
     * ctrl DELETE , ctrl INSERT
     * ctrl PLUS , ctrl MINUS
     *
     * KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK|KeyEvent.SHIFT_MASK)
     *
     * @see javax.swing.KeyStroke#getKeyStroke(String)
     * @return
     */
    String value() default "";

    /**
     * 快捷键 - keycode
     * keyCode = KeyEvent.VK_1
     * @see java.awt.event.KeyEvent
     * @return
     */
    int keyCode() default -1;

    /**
     * 快捷键 - modifiers
     * modifiers = KeyEvent.CTRL_MASK|KeyEvent.ALT_MASK|KeyEvent.SHIFT_MASK
     * @see java.awt.event.KeyEvent
     * @return
     */
    int modifiers() default -1;
}
