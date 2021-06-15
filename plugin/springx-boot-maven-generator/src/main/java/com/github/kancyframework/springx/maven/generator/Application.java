package com.github.kancyframework.springx.maven.generator;

import com.github.kancyframework.springx.boot.SpringApplication;
import com.github.kancyframework.springx.boot.SpringBootApplication;
import com.github.kancyframework.springx.swing.SwingSpringApplication;

import javax.swing.*;
import java.awt.*;

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
        frame.setSize(new Dimension(650, 110));
    }
}
