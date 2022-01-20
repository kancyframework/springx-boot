package com.github.kancyframework.springx.boot;

import com.github.kancyframework.springx.context.*;
import com.github.kancyframework.springx.context.annotation.Async;
import com.github.kancyframework.springx.context.event.InitializedApplicationEvent;
import com.github.kancyframework.springx.context.event.StartedApplicationEvent;
import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;
import com.github.kancyframework.springx.utils.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * SpringBootApplication
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public class SpringApplication {
    private Class<?> applicationEntryClass;

    private ApplicationContext applicationContext;

    private final Logger log = LoggerFactory.getLogger(SpringApplication.class);

    public SpringApplication(Class<?> applicationEntryClass) {
        this.applicationEntryClass = applicationEntryClass;
    }

    public static void run(Class<?> applicationEntryClass, String[] args) {
        new SpringApplication(applicationEntryClass).run(args);
    }

    public void run(String[] args) {
        // 初始化命令行参数
        CommandLineArgument commandLineArgument = new CommandLineArgument(args);
        SpringUtils.setCommandLineArgument(commandLineArgument);

        // 应用数据初始化
        List<ApplicationInitializer> services = SpiUtils.findServices(ApplicationInitializer.class);
        for (ApplicationInitializer applicationInitializer : services) {
            try {
                applicationInitializer.run(commandLineArgument);
            } catch (Exception e) {
                throw new RuntimeException("Application failed to initialize！", e);
            }
        }
        log.info("start running......");

        // create application context and application initializer
        applicationContext = createSimpleApplicationContext();

        SpringUtils.setApplicationContext(applicationContext);
        ApplicationContextInitializer initializer = createSimpleApplicationContextInitializer(applicationEntryClass);

        // initialize the application context (this is where we create beans)
        // here maybe exist a hidden cast
        initializer.initialize(applicationContext);

        // 发送初始化完成事件
        applicationContext.publishEvent(new InitializedApplicationEvent(applicationContext));

        // InitializingBean
        Map<String, InitializingBean> beanMaps = applicationContext.getBeansOfType(InitializingBean.class);
        beanMaps.values().forEach(InitializingBean::afterPropertiesSet);

        // process those special beans
        processSpecialBeans(args);

        // addShutdownHook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (ShutdownHook shutdownHook : applicationContext.getBeansOfType(ShutdownHook.class).values()) {
                shutdownHook.run(commandLineArgument);
            }
        }));

        log.info("Started {} success in {} seconds!",
                applicationContext.getEnvironment().getApplicationName(),
                (System.currentTimeMillis() *1.0 - applicationContext.getStartupDate())/1000);

        // 发送启动事件
        applicationContext.publishEvent(new StartedApplicationEvent(applicationContext));

    }

    private SimpleApplicationContextInitializer createSimpleApplicationContextInitializer(Class<?> entryClass) {
        // get base packages
        SpringBootApplication annotation = entryClass.getDeclaredAnnotation(SpringBootApplication.class);
        String[] basePackages = annotation.basePackages();
        if (basePackages.length == 0) {
            basePackages = new String[]{entryClass.getPackage().getName()};
        }

        // create context initializer with base packages
        return new SimpleApplicationContextInitializer(Arrays.asList(basePackages));
    }

    private SimpleApplicationContext createSimpleApplicationContext() {
        return new SimpleApplicationContext();
    }

    private void processSpecialBeans(String[] args) {
        callRegisteredRunners(args);
    }

    private void callRegisteredRunners(String[] args) {
        CommandLineArgument commandLineArgument = new CommandLineArgument(args);
        Map<String, ApplicationRunner> applicationRunners = applicationContext.getBeansOfType(ApplicationRunner.class);
        List<ApplicationRunner> applicationRunnerList = sortByOrder(applicationRunners.values());
        try {
            for (ApplicationRunner applicationRunner : applicationRunnerList) {

                Class<? extends ApplicationRunner> applicationRunnerClass = applicationRunner.getClass();

                Executor executor = null;
                if (ClassUtils.isAnnotationPresentOnClass(applicationRunnerClass, Async.class)){
                    Async annotation = applicationRunnerClass.getAnnotation(Async.class);
                    executor = getTaskExecutor(annotation.value());
                }else {
                    Method listenerMethod = ReflectionUtils.findMethod(applicationRunnerClass,"run", CommandLineArgument.class);
                    if (listenerMethod.isAnnotationPresent(Async.class)){
                        executor = getTaskExecutor(listenerMethod.getAnnotation(Async.class).value());
                    }
                }
                if (Objects.nonNull(executor)){
                    executor.execute(() -> {
                        try {
                            applicationRunner.run(commandLineArgument);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }else {
                    applicationRunner.run(commandLineArgument);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Executor getTaskExecutor(String beanName) {
        return applicationContext.getBean(beanName, Executor.class);
    }

    /**
     * 通过Order注解排序
     * @param collection
     */
    private <T> List<T> sortByOrder(Collection<T> collection) {
        return OrderUtils.sort(collection);
    }
}