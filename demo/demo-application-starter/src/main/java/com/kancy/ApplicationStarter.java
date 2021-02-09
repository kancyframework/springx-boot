package com.kancy;

import com.github.kancyframework.springx.classloader.NetClassLoader;
import com.github.kancyframework.springx.utils.ReflectionUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import javax.swing.*;

/**
 * demo args
 * -Djar=demo-spring-boot-starter-gui-0.0.1-SNAPSHOT.jar
 * -Durl=https://gitee.com/kancy666/public/raw/master/jars/demo-spring-boot-starter-gui-0.0.1-SNAPSHOT.jar
 * -Dmain-class=com.github.kancyframework.springx.demo.Application
 */
public class ApplicationStarter {
    public static void main(String[] args) {

        CommandLineArgument argument = new CommandLineArgument(args);
        String url = argument.getArgument("url");
        if (StringUtils.isBlank(url)){
            String jarName = argument.getArgument("jar");
            if (StringUtils.isNotBlank(jarName)){
                url = String.format("https://gitee.com/kancy666/public/raw/master/jars/%s", jarName);
            }
        }
        String mainClass = argument.getArgument("mainClass", argument.getArgument("main-class"));
        if (StringUtils.isBlank(url)|| StringUtils.isBlank(mainClass)){
            JOptionPane.showMessageDialog(null, "启动参数错误，请联系管理员！");
            return;
        }
        NetClassLoader.load(url);
        ReflectionUtils.invokeMainMethod("com.github.kancyframework.springx.demo.Application", args);
    }
}
