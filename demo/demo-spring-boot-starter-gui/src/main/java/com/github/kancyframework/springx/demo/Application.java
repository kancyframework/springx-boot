package com.github.kancyframework.springx.demo;

import com.github.kancyframework.springx.boot.SpringApplication;
import com.github.kancyframework.springx.boot.SpringBootApplication;
import com.github.kancyframework.springx.demo.ui.DemoFrame;
import com.github.kancyframework.springx.swing.SwingSpringApplication;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.github.kancyframework.springx.utils.StringRedisUtils;

@SpringBootApplication
public class Application implements SwingSpringApplication<DemoFrame> {

    public static void main(String[] args) {
        StringRedisUtils.set("random", RandomUtils.nextLong());
        StringRedisUtils.set("time", System.currentTimeMillis());
        SpringApplication.run(Application.class, args);
    }
}
