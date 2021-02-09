package com.kancy;

import com.github.kancyframework.springx.classloader.NetClassLoader;
import com.github.kancyframework.springx.utils.ReflectionUtils;

public class ApplicationStarter {
    public static void main(String[] args) {

        CommandLineArgument argument = new CommandLineArgument(args);
        String url = argument.getArgument("url");
        String mainClass = argument.getArgument("mainClass");
        System.out.println(url);
        System.out.println(mainClass);
        NetClassLoader.load("https://gitee.com/kancy666/public/raw/master/jars/demo-spring-boot-starter-gui-0.0.1-SNAPSHOT.jar");
        startApplication("com.github.kancyframework.springx.demo.Application", args);
    }

    private static void startApplication(String mainClassName, String[] args) {
        ReflectionUtils.invokeMainMethod(mainClassName, args);
    }
}
