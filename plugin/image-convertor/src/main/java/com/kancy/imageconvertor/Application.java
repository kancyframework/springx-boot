package com.kancy.imageconvertor;

import com.github.kancyframework.springx.boot.SpringApplication;
import com.github.kancyframework.springx.boot.SpringBootApplication;
import com.github.kancyframework.springx.swing.SwingSpringApplication;

import javax.swing.*;

/**
 * <p>
 * Application
 * </p>
 *
 * @author kancy
 * @since 2021-06-14 08:13:22
 * @description 启动类
 **/
@SpringBootApplication
public class Application implements SwingSpringApplication<JFrame> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 自定义设置
     *
     * @param frame frame
     */
    @Override
    public void customSettings(JFrame frame) {
    }
}
