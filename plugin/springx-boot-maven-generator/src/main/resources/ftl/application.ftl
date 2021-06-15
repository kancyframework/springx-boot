package ${packageName};

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
 * @since ${datetime}
 * @description 启动类
 **/
<#noparse>
@SpringBootApplication
public class Application implements SwingSpringApplication<JFrame> {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
</#noparse>