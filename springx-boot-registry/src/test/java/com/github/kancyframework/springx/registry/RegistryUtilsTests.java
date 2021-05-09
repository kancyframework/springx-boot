package com.github.kancyframework.springx.registry;

import com.github.kancyframework.springx.utils.RegistryUtils;
import org.junit.Test;

public class RegistryUtilsTests {
    @Test
    public void test(){
        RegistryUtils.setValue("name", "emma");
        String name = RegistryUtils.getValue("name");
        System.out.println(name);
    }

    @Test
    public void delTest(){
        RegistryUtils.delValue("name");
    }
}
