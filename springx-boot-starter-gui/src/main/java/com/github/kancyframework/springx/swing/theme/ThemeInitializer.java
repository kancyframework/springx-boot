package com.github.kancyframework.springx.swing.theme;

import com.github.kancyframework.springx.boot.ApplicationInitializer;
import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.swing.themes.Themes;

/**
 * ThemeInitializer
 *
 * @author huangchengkang
 * @date 2021/9/6 13:50
 */
public class ThemeInitializer implements ApplicationInitializer {
    /**
     * run
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(CommandLineArgument args) throws Exception {
        // 安装主题
        Themes.useTheme();
    }
}
