package com.github.kancyframework.springx.demo;

import com.github.kancyframework.springx.boot.SpringApplication;
import com.github.kancyframework.springx.boot.SpringBootApplication;
import com.github.kancyframework.springx.demo.ui.MainUI;
import com.github.kancyframework.springx.swing.SwingSpringApplication;

@SpringBootApplication
public class Application implements SwingSpringApplication<MainUI> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
