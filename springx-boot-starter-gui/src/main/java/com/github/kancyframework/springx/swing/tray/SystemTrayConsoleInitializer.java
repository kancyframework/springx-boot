package com.github.kancyframework.springx.swing.tray;

import com.github.kancyframework.springx.annotation.Order;
import com.github.kancyframework.springx.boot.ApplicationInitializer;
import com.github.kancyframework.springx.boot.CommandLineArgument;
import com.github.kancyframework.springx.swing.console.ConsoleDialog;

@Order
public class SystemTrayConsoleInitializer implements ApplicationInitializer {
    /**
     * run
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(CommandLineArgument args) throws Exception {
        if (args.getArgument("tray", true)
                && args.getArgument("tray.console", false)
                && args.getArgument("console.init", true)){
            ConsoleDialog.install();
        }
    }
}
