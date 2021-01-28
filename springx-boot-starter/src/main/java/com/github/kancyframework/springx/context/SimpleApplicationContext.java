package com.github.kancyframework.springx.context;

import com.github.kancyframework.springx.context.annotation.*;
import com.github.kancyframework.springx.context.env.EnvironmentAware;
import com.github.kancyframework.springx.context.event.ApplicationEvent;
import com.github.kancyframework.springx.context.event.ApplicationEventMulticaster;
import com.github.kancyframework.springx.context.factory.BeanDefinition;
import com.github.kancyframework.springx.boot.SpringBootApplication;
import com.github.kancyframework.springx.utils.*;
import com.github.kancyframework.springx.context.env.Environment;
import com.github.kancyframework.springx.log.Logger;
import com.github.kancyframework.springx.log.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SimpleApplicationContext
 *
 * @author kancy
 * @date 2020/2/18 12:12
 */
public class SimpleApplicationContext implements ApplicationContext {

    private long startupDate;

    private ThreadPoolExecutor taskExecutor;

    private Environment environment = new Environment();

    private Set<String> scannedPackages = new ConcurrentHashSet<>();

    private Map<String, BeanDefinition> registeredBeans = new ConcurrentHashMap<>();

    private Map<String, BeanDefinition> earlyBeans = new ConcurrentHashMap<>();

    private final Logger log = LoggerFactory.getLogger(SimpleApplicationContext.class);

    private final ApplicationEventMulticaster applicationEventMulticaster = new ApplicationEventMulticaster(this);

    AtomicLong totalBeanCount = new AtomicLong(0L);

    AtomicLong nameConflictCount = new AtomicLong(0L);

    @Override
    public Object getBean(String name) {
        return registeredBeans.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> type) {
        BeanDefinition beanDefinition = (BeanDefinition)getBean(name);
        return beanDefinition == null ? null : (type.isAssignableFrom(beanDefinition.getClazz()) ? type.cast(beanDefinition.getObject()) : null);
    }

    @Override
    public <T> T getBean(Class<T> beanClass) {
        Map<String, T> map = getBeansOfType(beanClass);
        return map.isEmpty() ? null : beanClass.cast(map.values().toArray()[0]);
    }

    @Override
    public boolean containsBean(String name) {
        return getBean(name) != null;
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> beanType) {
        Map<String, T> res = new HashMap<>();
        registeredBeans.entrySet().stream()
                .filter(entry -> beanType.isAssignableFrom(entry.getValue().getClazz()))
                .forEach(entry -> res.put(entry.getKey(), beanType.cast(entry.getValue().getObject())));
        return res;
    }

    @Override
    public void setStartupDate(long startupDate) {
        this.startupDate = startupDate;
    }

    @Override
    public long getStartupDate() {
        return startupDate;
    }

    /**
     * try to autowire those beans in earlyBeans
     * if succeed, remove it from earlyBeans and put it into registeredBeans
     * otherwise ,throw a RuntimeException(in autowireFields)
     */
    private synchronized void processEarlyBeans() {
        for (Map.Entry<String, BeanDefinition> entry : earlyBeans.entrySet()) {
            BeanDefinition myBeanDefinition = entry.getValue();
            try {
                if (autowireFields(myBeanDefinition.getObject(), myBeanDefinition.getClazz(), true)) {
                    registeredBeans.put(entry.getKey(), myBeanDefinition);
                    earlyBeans.remove(entry.getKey());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 初始化
     */
    public void initialize() {
        // set log level
        LoggerFactory.setLogLevel(environment.getStringProperty("log.level", "INFO"));
        // set banner
        try {
            String bannerString = IoUtils.toString(getClass().getClassLoader().getResourceAsStream("banner.txt"), "utf-8");
            System.out.println(bannerString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * scan base packages and create beans
     * @param basePackages
     * @param recursively
     * @throws ClassNotFoundException
     */
    public void scan(Set<String> basePackages, boolean recursively) throws ClassNotFoundException, IOException {
        log.info("start scanning......");

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // get all classes who haven't been registered
        Set<Class<?>> classes = new LinkedHashSet<>();
        for (String packageName : basePackages) {
            if (scannedPackages.add(packageName)) {
                classes.addAll(ClassUtils.getClassesByPackageName(classLoader, packageName, recursively));
            }
        }

        // autowire or create bean for each class
        classes.forEach(this::processSingleClass);

        // spi添加需要导入的类
        Set<Class<?>> importClasses = new LinkedHashSet<>();
        List<ImportSelector> services = SpiUtils.findServices(ImportSelector.class);
        for (ImportSelector importSelector : services) {
            String[] selectImportPackages = importSelector.selectImportPackages(this);
            for (String packageName : selectImportPackages) {
                importClasses.addAll(ClassUtils.getClassesByPackageName(classLoader, packageName, recursively));
            }

            String[] selectImports = importSelector.selectImports(this);
            for (String selectImportClassName : selectImports) {
                try {
                    importClasses.add(Class.forName(selectImportClassName));
                } catch (ClassNotFoundException e) {
                    log.info("Not Found Import Class : {}", selectImportClassName);
                }
            }
        }
        // autowire or create bean for each import class
        importClasses.forEach(this::processSingleClass);

        // 动态bean注册
        List<DynamicBeanRegistry> beanDefinitionRegistries = SpiUtils.findServices(DynamicBeanRegistry.class);
        for (DynamicBeanRegistry beanRegistry : beanDefinitionRegistries) {
            Map<String, BeanDefinition> definitionMap = beanRegistry.registerBeans(this);
            for (Map.Entry<String, BeanDefinition> entry : definitionMap.entrySet()) {
                String beanName = entry.getKey();
                Object instance = entry.getValue().getObject();
                Class<?> clazz = entry.getValue().getClazz();

                Assert.notNull(instance, "dynamic registry bean instance is null.");
                Assert.notNull(clazz, "dynamic registry bean class is null.");

                // 环境不匹配
                if (!profileCheck(clazz)){
                    continue;
                }

                if (registeredBeans.containsKey(beanName) || earlyBeans.containsKey(beanName)){
                    log.warn("Spring bean already registered : {}", beanName);
                } else {
                    try {
                        log.info("dynamic registry bean [{}] , class : {}", beanName, clazz.getName());

                        if (autowireFields(instance, clazz, false)) {
                            registeredBeans.put(beanName, entry.getValue());
                        } else {
                            earlyBeans.put(beanName, entry.getValue());
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        processEarlyBeans();

        log.info("scan over!");
    }

    /**
     * try to create a bean for certain class, put it into registeredBeans if success, otherwise put it into earlyBeans
     * @param clazz
     */
    private void processSingleClass(Class<?> clazz) {
        if (profileCheck(clazz)){

            if ((clazz.isAnnotationPresent(Configuration.class) || clazz.isAnnotationPresent(SpringBootApplication.class))
                    && clazz.isAnnotationPresent(Import.class)){
                Import anImport = clazz.getAnnotation(Import.class);
                Set<Class<?>> classes = new LinkedHashSet<>();
                classes.addAll(CollectionUtils.toList(anImport.classes()));
                classes.addAll(CollectionUtils.toList(ClassUtils.loadClasses(anImport.value())));
                for (Class<?> aClass : classes) {
                    String beanName = null;
                    if (aClass.isAnnotationPresent(Configuration.class)){
                        beanName = aClass.getAnnotation(Configuration.class).value();
                    } else if (aClass.isAnnotationPresent(Component.class)){
                        beanName = aClass.getAnnotation(Component.class).value();
                    }
                    registerBean(aClass, beanName);
                }
            }

            if (!(clazz.isAnnotationPresent(Component.class)
                    ||clazz.isAnnotationPresent(Configuration.class)
                    ||clazz.isAnnotationPresent(SpringBootApplication.class))){
                return;
            }
            log.debug(String.format("processSingleClass [%s]", clazz.getName()));

            String beanName = null;
            if (clazz.isAnnotationPresent(Configuration.class)){
                beanName = clazz.getAnnotation(Configuration.class).value();
            } else if (clazz.isAnnotationPresent(Component.class)){
                beanName = clazz.getAnnotation(Component.class).value();
            }
            registerBean(clazz, beanName);
        }
    }

    private void registerBean(Class<?> clazz, String beanName) {
        if (!profileCheck(clazz)){
            return;
        }

        if (StringUtils.isEmpty(beanName)) {
            beanName = getBeanNameByClassName(clazz);
        }

        if (registeredBeans.containsKey(beanName) || earlyBeans.containsKey(beanName)){

        }

        BeanDefinition beanDefinition = registeredBeans.get(beanName);
        if (Objects.isNull(beanDefinition)){
            beanDefinition = earlyBeans.get(beanName);
        }

        if (Objects.nonNull(beanDefinition)){
            log.warn("beanName repeat : %s , class1[%s] , class2[%s]", beanName,
                    beanDefinition.getClazz().getName(), clazz.getName());
            return;
        }

        Object instance;
        try {
            instance = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // 如果这个Component是一个Aspect，那么给他拦截到的对象进行代理

        try {
            if (autowireFields(instance, clazz, false)) {
                registeredBeans.put(beanName, new BeanDefinition(instance, clazz));
            } else {
                earlyBeans.put(beanName, new BeanDefinition(instance, clazz));
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            createBeansByMethodsOfClass(instance, clazz);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean profileCheck(Class<?> clazz ){
        if (clazz.isAnnotationPresent(Profile.class)){
            Profile annotation = clazz.getAnnotation(Profile.class);
            String[] profiles = annotation.value();
            if (Objects.nonNull(profiles)){
                for (String profile : profiles) {
                    if (environment.getProfiles().contains(profile)){
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private boolean profileCheck(Method method ){
        if (method.isAnnotationPresent(Profile.class)){
            Profile annotation = method.getAnnotation(Profile.class);
            String[] profiles = annotation.value();
            if (Objects.nonNull(profiles)){
                for (String profile : profiles) {
                    if (environment.getProfiles().contains(profile)){
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private void createBeansByMethodsOfClass(Object instance, Class<?> clazz) throws InvocationTargetException, IllegalAccessException {
        if (!clazz.isAnnotationPresent(Configuration.class)){
            return;
        }

        List<Method> methods = getMethodsWithAnnotation(clazz, Bean.class);
        for (Method method : methods) {
            if (profileCheck(method)){
                method.setAccessible(true);
                Object methodBean = method.invoke(instance);
                long beanId = totalBeanCount.getAndIncrement();
                Class<?> methodBeanClass = methodBean.getClass();

                //bean name
                Bean simpleBean = method.getAnnotation(Bean.class);
                String beanName = simpleBean.value();
                if (beanName.isEmpty()) {
                    beanName = getBeanNameByMethodName(method, beanId);
                }

                // register bean
                registeredBeans.put(beanName, new BeanDefinition(methodBean, methodBeanClass));
            }
        }
    }

    private List<Method> getMethodsWithAnnotation(Class<?> clazz, Class<?> annotationClass) {
        List<Method> res = new LinkedList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationClass) {
                    res.add(method);
                    break;
                }
            }
        }
        return res;
    }


    /**
     * try autowire all fields of a certain instance
     * @param instance
     * @param clazz
     * @param lastChance
     * @return true if success, otherwise return false or throw a exception if this is the lastChance
     * @throws IllegalAccessException
     */
    private boolean autowireFields(Object instance, Class<?> clazz, boolean lastChance) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Autowired) {
                    Autowired autowired = (Autowired) annotation;
                    String beanName = autowired.value();
                    BeanDefinition beanDefinition = getSimpleBeanByNameOrType(beanName, field.getType(), true);
                    if (beanDefinition == null) {
                        if (lastChance) {
                            if (!autowired.required()) {
                                break;
                            }
                            throw new RuntimeException(String.format("Failed in autowireFields : [%s].[%s]", clazz.getName(), field.getName()));
                        } else {
                            return false;
                        }
                    }
                    field.setAccessible(true);
                    field.set(instance, beanDefinition.getObject());
                }

                if (annotation instanceof Value) {
                    Value value = (Value) annotation;
                    String valueString = value.value();

                    if (StringUtils.isNotBlank(valueString)){
                        valueString = valueString.trim();
                        if (valueString.startsWith("${") && valueString.endsWith("}")){
                            valueString = valueString.substring(2, valueString.length() - 1);
                        }
                        Class<?> fieldType = field.getType();
                        Object setValue = null;
                        if (Objects.equals(fieldType, String.class)){
                            setValue = environment.getStringProperty(valueString);
                        } else if (Objects.equals(fieldType, BigDecimal.class)){
                            String property = environment.getStringProperty(valueString);
                            if (StringUtils.isNotBlank(property)){
                                setValue = new BigDecimal(property);
                            }
                        } else if (Objects.equals(fieldType, Duration.class)){
                            String property = environment.getStringProperty(valueString);
                            if (StringUtils.isNotBlank(property)){
                                setValue = Duration.parse(property);
                            }
                        } else if (Objects.equals(fieldType, Boolean.class) || Objects.equals(fieldType, boolean.class)){
                            setValue = environment.getBooleanProperty(valueString);
                        } else if (Objects.equals(fieldType, Integer.class) || Objects.equals(fieldType, int.class)){
                            setValue = environment.getIntegerProperty(valueString);
                        } else if (Objects.equals(fieldType, Double.class) || Objects.equals(fieldType, double.class)){
                            setValue = environment.getDoubleProperty(valueString);
                        } else if (Objects.equals(fieldType, Long.class) || Objects.equals(fieldType, long.class)){
                            setValue = environment.getLongProperty(valueString);
                        } else if (Set.class.isAssignableFrom(fieldType)){
                            setValue = environment.getSetProperty(valueString);
                        } else if (List.class.isAssignableFrom(fieldType)){
                            setValue = environment.getListProperty(valueString);
                        }
                        if (Objects.nonNull(setValue)){
                            ReflectionUtils.makeAccessible(field);
                            field.set(instance, setValue);
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * only used in autowireFields
     * @param beanName
     * @param type
     * @param allowEarlyBean
     * @return
     */
    private BeanDefinition getSimpleBeanByNameOrType(String beanName, Class<?> type, boolean allowEarlyBean) {
        // 1. by name
        BeanDefinition res = registeredBeans.get(beanName);
        if (res == null && allowEarlyBean) {
            res = earlyBeans.get(beanName);
        }

        // 2. by type
        if (type != null) {
            if (res == null) {
                res = getSimpleBeanByType(type, registeredBeans);
            }
            if (res == null && allowEarlyBean) {
                res = getSimpleBeanByType(type, earlyBeans);
            }
        }

        return res;
    }

    /**
     * search bean by type in certain beans map
     * @param type
     * @param beansMap
     * @return
     */
    private BeanDefinition getSimpleBeanByType(Class<?> type, Map<String, BeanDefinition> beansMap) {
        List<BeanDefinition> beanDefinitions = new LinkedList<>();
        beansMap.entrySet().stream().filter(entry -> type.isAssignableFrom(entry.getValue().getClazz())).forEach(entry -> beanDefinitions.add(entry.getValue()));
        if (beanDefinitions.size() > 1) {
            throw new RuntimeException(String.format("Autowire by type, but more than one instance of type [%s] is founded!", beanDefinitions.get(0).getClazz().getName()));
        }
        return beanDefinitions.isEmpty() ? null : beanDefinitions.get(0);
    }

    private String getUniqueBeanNameByClassAndBeanId(Class<?> clazz, long beanId) {
        String beanName = clazz.getName() + "_" + beanId;
        while (registeredBeans.containsKey(beanName) || earlyBeans.containsKey(beanName)) {
            beanName = clazz.getName() + "_" + beanId + "_" + nameConflictCount.getAndIncrement();
        }
        return beanName;
    }

    private String getBeanNameByMethodName(Method method, long beanId) {
        String beanName = StringUtils.lowerFirst(method.getName());
        if (registeredBeans.containsKey(beanName) || earlyBeans.containsKey(beanName)) {
            throw new RuntimeException(String.format("beanName repeat：%s", beanName));
        }
        return beanName;
    }

    private String getBeanNameByClassName(Class<?> clazz) {
        String beanName = StringUtils.lowerFirst(clazz.getSimpleName());
        if (registeredBeans.containsKey(beanName) || earlyBeans.containsKey(beanName)) {
            throw new RuntimeException(String.format("beanName repeat：%s", beanName));
        }
        return beanName;
    }

    @Override
    public void publishEvent(ApplicationEvent applicationEvent) {
        applicationEventMulticaster.multicastEvent(applicationEvent);
    }

    /**
     * 获取TaskExecutor
     * @return
     */
    public ThreadPoolExecutor getTaskExecutor() {
        if (Objects.isNull(taskExecutor)){
            synchronized (this){
                taskExecutor =  new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                        Runtime.getRuntime().availableProcessors()*2,
                        0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    taskExecutor.shutdown();
                    log.info("taskExecutor shutdown Successfully.");
                }));
                log.info("taskExecutor initialized Successfully.");
            }
        }
        return taskExecutor;
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * set Aware
     */
    public void setAware() {
        // set ApplicationContextAware
        Map<String, ApplicationContextAware> beanMaps = this.getBeansOfType(ApplicationContextAware.class);
        beanMaps.values().stream()
                .forEach(aware -> aware.onApplicationContext(this));

        // set EnvironmentAware
        Map<String, EnvironmentAware> environmentAwareBeanMaps = this.getBeansOfType(EnvironmentAware.class);
        environmentAwareBeanMaps.values().stream()
                .forEach(aware -> aware.onEnvironment(this.getEnvironment()));
    }

    /**
     * ConcurrentHashSet
     *
     * @author kancy
     * @date 2020/2/18 12:12
     */
    public static class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, Serializable {

        private static final long serialVersionUID = -3416162471678171568L;

        private static final Object DEFAULT_VALUE = new Object();

        private final ConcurrentHashMap<E, Object> map;

        public ConcurrentHashSet() {
            map = new ConcurrentHashMap<>();
        }

        public ConcurrentHashSet(int initialCapacity) {
            map = new ConcurrentHashMap<>(initialCapacity);
        }

        public ConcurrentHashSet(int initialCapacity, float loadFactor) {
            map = new ConcurrentHashMap<>(initialCapacity, loadFactor);
        }

        @Override
        public Iterator<E> iterator() {
            return map.keySet().iterator();
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return map.containsKey(o);
        }

        @Override
        public boolean add(E e) {
            return map.put(e, DEFAULT_VALUE) == null;
        }

        @Override
        public boolean remove(Object o) {
            return map.remove(o) == DEFAULT_VALUE;
        }

        @Override
        public void clear() {
            map.clear();
        }
    }
}