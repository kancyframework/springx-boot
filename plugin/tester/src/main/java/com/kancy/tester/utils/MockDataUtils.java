package com.kancy.tester.utils;

import com.github.kancyframework.springx.utils.RandomUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import java.util.List;

public abstract class MockDataUtils {

    /**
     * 邮件
     * @return
     */
    public static String email(){
        return email(MetaData.EMAIL_SUFFIX_LIST.get(RandomUtils.nextInt(MetaData.EMAIL_SUFFIX_LIST.size())));
    }

    /**
     * QQ邮箱
     * @return
     */
    public static String qqEmail(){
        StringBuilder sb = new StringBuilder();
        int qqLen = RandomUtils.nextInt(8, 10);
        for (int i = 0; i < qqLen ; i++) {
            sb.append(RandomUtils.nextInt(10));
        }
        sb.append("@qq.com");
        return sb.toString();
    }

    /**
     * 手机邮箱
     * @return
     */
    public static String mobileEmail(){
        return String.format("%s%s", mobile(),
                MetaData.MOBILE_EMAIL_SUFFIX_LIST.get(RandomUtils.nextInt(MetaData.MOBILE_EMAIL_SUFFIX_LIST.size())));
    }

    /**
     * 邮件
     * @return
     */
    public static String email(String suffix){
        StringBuilder sb = new StringBuilder();
        int cl = RandomUtils.nextInt(6, 18);
        for (int i = 0; i < cl ; i++) {
            sb.append(MetaData.CHAR_ARRAY[RandomUtils.nextInt(MetaData.CHAR_ARRAY.length)]);
        }
        sb.append(suffix);
        return sb.toString();
    }

    /**
     * 电信手机号码
     * @return
     */
    public static String dxMobile(){
        return mobile(MetaData.DX_MOBILE_LIST.get(RandomUtils.nextInt(MetaData.DX_MOBILE_LIST.size())));
    }

    /**
     * 联通手机号码
     * @return
     */
    public static String ltMobile(){
        return mobile(MetaData.LT_MOBILE_LIST.get(RandomUtils.nextInt(MetaData.LT_MOBILE_LIST.size())));
    }

    /**
     * 移动手机号码
     * @return
     */
    public static String ydMobile(){
        return mobile(MetaData.YD_MOBILE_LIST.get(RandomUtils.nextInt(MetaData.YD_MOBILE_LIST.size())));
    }

    /**
     * 手机号码
     * @return
     */
    public static String mobile(){
        int index = RandomUtils.nextInt(100000) % 3;
        switch (index){
            case 0: return dxMobile();
            case 1: return ltMobile();
            default:
                return ydMobile();
        }
    }

    /**
     * 手机号码
     * @return
     */
    public static String mobile(String prefix){
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = prefix.length(); i <= 11; i++) {
            sb.append(RandomUtils.nextInt(0, 10));
        }
        return sb.toString();
    }

    /**
     * 身份证号码
     * @return
     */
    public static String idCard(){
        return IDCardUtils.create();
    }

    /**
     * IP地址
     * @return
     */
    public static String ip(){
        return ipv4();
    }

    /**
     * IP地址
     * @return
     */
    public static String ipv4(){
        return String.format("%s.%s.%s.%s",
                RandomUtils.nextInt(1,255),
                RandomUtils.nextInt(0,255),
                RandomUtils.nextInt(0,255),
                RandomUtils.nextInt(1,255));
    }

    /**
     * 字符串
     * @return
     */
    public static String string(){
        return string(10);
    }

    /**
     * 字符串
     * @return
     */
    public static String string(int len){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(MetaData.CHAR_ARRAY[RandomUtils.nextInt(MetaData.CHAR_ARRAY.length)]);
        }
        return sb.toString();
    }

    public static class MetaData {
        public static final char[] CHAR_ARRAY = "_123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();



        private static final List<String> EMAIL_SUFFIX_LIST = StringUtils.toList("@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@163.com,@163.net,@ask.com,@qq.com,@qq.com,@qq.com,@qq.com,@live.com,@qq.com,@163.com,@163.net,@263.net,@yeah.net,@googlemail.com,@googlemail.com,@126.com,@sina.com,@sohu.com,@vcredit.com,@cangoonline.com,@126.com,@sina.com,@sohu.com");
        private static final List<String> MOBILE_EMAIL_SUFFIX_LIST = StringUtils.toList("@163.com,@163.net,@263.net");


        /**
         * 电信
         */
        private static final List<String> DX_MOBILE_LIST = StringUtils.toList("133,149,153,173,177,180,181,189,199");
        /**
         * 联通
         */
        private static final List<String> LT_MOBILE_LIST = StringUtils.toList("130,131,132,145,155,156,166,171,175,176,185,186,166");
        /**
         * 移动
         */
        private static final List<String> YD_MOBILE_LIST = StringUtils.toList("135,136,137,138,139,147,150,151,152,157,158,159,172,178,182,183,184,187,188,198,1340,1341,1342,1343,1344,1345,1346,1347,1348");

    }

}
