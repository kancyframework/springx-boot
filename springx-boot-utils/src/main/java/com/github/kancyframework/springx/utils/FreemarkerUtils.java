package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.log.Log;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * FreemarkerUtils
 *
 * @author kancy
 * @date 2020/2/21 0:11
 */
public class FreemarkerUtils {

    private static String baseTemplatePath = "/ftl";

    private static Configuration configuration;

    /**
     * 渲染模板文件
     * @param templatePath
     * @param templateData
     * @return
     * @return
     */
    public static Optional<String> render(String templatePath, Map<String, Object> templateData) {
        StringWriter result = null;
        try {
            Template template = getConfiguration().getTemplate(templatePath);
            result = new StringWriter();
            template.process(templateData, result);
        } catch (IOException e){
            throw new RuntimeException("模板引擎执行错误！", e);
        } catch (TemplateException e) {
            throw new RuntimeException("Freemarker模板可能存在错误，请检查！", e);
        }
        return Optional.ofNullable(result.getBuffer().toString());
    }

    /**
     * 渲染模板数据
     *
     * @param templateData
     * @param modelData
     * @return
     */
    public static Optional<String> execute(String templateData, Map<String, Object> modelData) {
        StringWriter result = null;
        try {
            Template template = new Template(null, templateData, getConfiguration());
            result = new StringWriter();
            template.process(modelData, result);
        } catch (IOException e){
            throw new RuntimeException("模板引擎执行错误！", e);
        } catch (TemplateException e) {
            throw new RuntimeException("Freemarker模板可能存在错误，请检查！", e);
        }
        return Optional.ofNullable(result.getBuffer().toString());
    }

    /**
     * 初始化并且线程安全
     * @return
     */
    private static Configuration getConfiguration(){
        if (Objects.isNull(configuration)){
            synchronized (FreemarkerUtils.class){
                if (Objects.isNull(configuration)){
                    configuration = initConfiguration();
                }
            }
        }
        return configuration;
    }

    /**
     * 初始化配置
     * @return
     */
    private static Configuration initConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setClassForTemplateLoading(FreemarkerUtils.class, baseTemplatePath);
        setDefaultConfiguration(configuration);
        // 加载配置文件的属性
        ArrayList<String> confList = new ArrayList<>();
        confList.add("/META-INF/freemarker.properties");
        confList.add("freemarker.properties");
        confList.add("/ftl/freemarker.properties");
        confList.forEach(p ->{
            try {
                configuration.setSettings(PropertiesUtils.loadClasspathProperties(p));
                Log.info("load freemarker config file : {}", p);
            } catch (Exception e) {
                // ignore
            }
        });
        return configuration;
    }


    /**
     * 设置属性
     * @param configuration
     */
    private static Configuration setDefaultConfiguration(Configuration configuration) {
        configuration.setLocale(Locale.CHINA);
        configuration.setDateFormat("yyyy-MM-dd");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        configuration.setNumberFormat("#.######");
        configuration.setBooleanFormat("true,false");
        return configuration;
    }

}
