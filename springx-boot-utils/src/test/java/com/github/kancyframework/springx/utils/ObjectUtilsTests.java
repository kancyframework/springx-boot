package com.github.kancyframework.springx.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class ObjectUtilsTests {

    @Test
    public void test01(){
        assertTrue(ObjectUtils.cast(true, Boolean.class));
        assertTrue(ObjectUtils.cast("true", Boolean.class));
        assertFalse(ObjectUtils.cast("false", Boolean.class));
        assertFalse(ObjectUtils.cast(0, Boolean.class));
        assertFalse(ObjectUtils.cast(1, Boolean.class));
    }
}
