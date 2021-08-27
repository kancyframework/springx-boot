package com.github.kancyframework.springx.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ExceptionUtils
 *
 * @author huangchengkang
 * @date 2021/8/27 13:03
 */
public class ExceptionUtils {
    /**
     * 获取异常的堆栈信息
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

    public static <T extends Throwable> void throwRuntimeException(T t) {
        if (t instanceof RuntimeException){
            throw RuntimeException.class.cast(t);
        }
        throw new RuntimeException(t);
    }
}
