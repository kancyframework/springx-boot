package com.github.kancyframework.springx.context;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * SimpleApplicationContextInitializer
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public class SimpleApplicationContextInitializer implements ApplicationContextInitializer<SimpleApplicationContext> {

    private Set<String> basePackages = new LinkedHashSet<>();

    public SimpleApplicationContextInitializer(List<String> basePackages) {
        this.basePackages.addAll(basePackages);
    }

    @Override
    public void initialize(SimpleApplicationContext applicationContext) {
        try {
            applicationContext.initialize();
            applicationContext.scan(basePackages, true);
            applicationContext.setAware();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        applicationContext.setStartupDate(System.currentTimeMillis());
    }
}
