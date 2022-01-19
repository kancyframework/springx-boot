package com.kancy.imagebeder;

import com.github.kancyframework.springx.boot.SpringApplication;
import com.github.kancyframework.springx.boot.SpringBootApplication;
import com.github.kancyframework.springx.swing.SwingSpringApplication;
import com.kancy.imagebeder.ui.Imagebeder;

/**
 * <p>
 * Application
 * </p>
 *
 * @author kancy
 * @since 2022-01-18 22:47:39
 * @description 启动
 **/
@SpringBootApplication
public class Application implements SwingSpringApplication<Imagebeder> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 自定义设置
     *
     * @param frame frame
     */
    @Override
    public void customSettings(Imagebeder frame) {
        frame.setSize(900, 600);
        frame.setResizable(false);
    }
}
