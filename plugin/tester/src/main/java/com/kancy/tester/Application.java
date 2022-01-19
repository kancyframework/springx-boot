package com.kancy.tester;

import com.github.kancyframework.dingtalk.DingTalkClient;
import com.github.kancyframework.dingtalk.DingTalkClientImpl;
import com.github.kancyframework.springx.boot.SpringApplication;
import com.github.kancyframework.springx.boot.SpringBootApplication;
import com.github.kancyframework.springx.context.annotation.Bean;
import com.github.kancyframework.springx.context.annotation.Configuration;
import com.github.kancyframework.springx.swing.SwingSpringApplication;

import javax.swing.*;
import java.awt.*;

/**
 * <p>
 * Application
 * </p>
 *
 * @author kancy
 * @since 2021-06-14 08:13:22
 * @description 启动类
 **/
@Configuration
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
        frame.setSize(new Dimension(625, 505));
        frame.setTitle("影像生成器 1.2 by kancy at 20211005");
        frame.setResizable(false);
    }

    @Bean
    public DingTalkClient dingTalkClient(){
        String secretKey = "SEC83a8ae9b90c938d41073aed3db3b00dd7124481f23d45fe10d980d6e4ea5ca21";
        String accessToken="e42bd0ae95d2913edfdbdfbd2edd73817487d06fa7c6350bce828192f1a86bab";
        return new DingTalkClientImpl(accessToken, secretKey);
    }

}
