package com.github.kancyframework.springx.context;

/**
 * ImportSelecter
 *
 * @author kancy
 * @date 2021/1/9 12:05
 */
public interface ImportSelector {

    /**
     * 导入的类
     * @param context 上下文
     * @return
     */
    default String[] selectImports(ApplicationContext context){
        return new String[0];
    }

    /**
     * 导入的包
     * @param context 上下文
     * @return
     */
    default String[] selectImportPackages(ApplicationContext context){
        return new String[0];
    }
}
