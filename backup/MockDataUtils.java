package com.github.kancyframework.springx.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public abstract class MockDataUtils {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(baiDuUrl());
        }
    }

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
     * 银行卡
     * @return
     */
    public static String bankCard(){
        return null;
    }

    /**
     * 银行卡
     * @return
     */
    public static String bankCard19(){
        return null;
    }

    /**
     * 银行卡
     * @return
     */
    public static String bankCard15(){
        return null;
    }

    /**
     * 身份证号码
     * @return
     */
    public static String idCard(){
        return IDCardUtils.create();
    }

    /**
     * 地址
     * @return
     */
    public static String address(){
        return null;
    }

    /**
     * 城市
     * @return
     */
    public static String city(){
        return null;
    }

    /**
     * 姓式
     * @return
     */
    public static String familyName() {
        return RandomUtils.nextDouble() > 0.3d ? singleFamilyName() : doubleFamilyName();
    }
    /**
     * 姓式
     * @return
     */
    public static String singleFamilyName() {
        return MetaData.SINGLE_FAMILY_NAME_LIST.get(RandomUtils.nextInt(MetaData.SINGLE_FAMILY_NAME_LIST.size()));
    }
    /**
     * 姓式
     * @return
     */
    public static String doubleFamilyName() {
        return MetaData.DOUBLE_FAMILY_NAME_LIST.get(RandomUtils.nextInt(MetaData.DOUBLE_FAMILY_NAME_LIST.size()));
    }

    /**
     * 汉字
     * @return
     */
    public static String chinese() {
        return chinese(1);
    }

    /**
     * 汉字
     * @return
     */
    public static String chinese(int size) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder.append(MetaData.CHINESE_STRING
                    .charAt(RandomUtils.nextInt(MetaData.CHINESE_STRING.length())));
        }
        return stringBuilder.toString();
    }

    /**
     * 姓名
     * @return
     */
    public static String customerName(){
        return String.format("%s%s", familyName(), chinese(RandomUtils.nextDouble() > 0.3d ? 2 : 1));
    }

    /**
     * 雪花ID
     * @return
     */
    public static long snowflakeId(){
        return IDUtils.getSnowflakeId();
    }

    /**
     * 雪花ID
     * @return
     */
    public static String dateSnowflakeNo(){
        return IDUtils.getDateSnowflakeNo();
    }

    /**
     * 雪花ID
     * @return
     */
    public static String dateSnowflakeNo(String name){
        return IDUtils.getDateSnowflakeNo(name);
    }

    /**
     * UUID
     * @return
     */
    public static String uuid(){
        return IDUtils.getUUIDString();
    }

    /**
     * UUID
     * @return
     */
    public static String uuid32(){
        return IDUtils.get32UUIDString();
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

    /**
     * 数字
     * @return
     */
    public static int integer(){
        return RandomUtils.nextInt(Integer.MAX_VALUE);
    }
    /**
     * 概率
     * @return
     */
    public static double probability(){
        return probability(2);
    }
    /**
     * 概率
     * @param scale
     * @return
     */
    public static double probability(int scale){
        return RandomUtils.nextDouble(scale);
    }

    /**
     * 百分比
     * @return
     */
    public static String percentString(){
        return percentString(2);
    }
    /**
     * 百分比
     * @return
     */
    public static String percentString(int scale){
        return String.format("%s%%", BigDecimal.valueOf(RandomUtils.nextDouble(scale+2))
                .multiply(BigDecimal.valueOf(100)).setScale(scale));
    }

    /**
     * 钱
     * @return
     */
    public static BigDecimal amount(){
        return amount(0,100000, 2);
    }

    /**
     * 钱
     * @return
     */
    public static BigDecimal amount(double start,double end, int scale){
        return BigDecimal.valueOf(RandomUtils.nextDouble(start,end, scale));
    }

    /**
     * 百度url
     * @return
     */
    public static String baiDuUrl(){
        return String.format("http://www.baidu.com/s?wd=%s", string());
    }

    public static class MetaData {
        public static final char[] CHAR_ARRAY = "_123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();

        private static final String CHINESE_STRING = "\u7684\u4e00\u4e86\u662f\u6211\u4e0d\u5728\u4eba\u4eec\u6709\u6765\u4ed6\u8fd9\u4e0a\u7740\u4e2a\u5730\u5230\u5927\u91cc\u8bf4\u5c31\u53bb\u5b50\u5f97\u4e5f\u548c\u90a3\u8981\u4e0b\u770b\u5929\u65f6\u8fc7\u51fa\u5c0f\u4e48\u8d77\u4f60\u90fd\u628a\u597d\u8fd8\u591a\u6ca1\u4e3a\u53c8\u53ef\u5bb6\u5b66\u53ea\u4ee5\u4e3b\u4f1a\u6837\u5e74\u60f3\u751f\u540c\u8001\u4e2d\u5341\u4ece\u81ea\u9762\u524d\u5934\u9053\u5b83\u540e\u7136\u8d70\u5f88\u50cf\u89c1\u4e24\u7528\u5979\u56fd\u52a8\u8fdb\u6210\u56de\u4ec0\u8fb9\u4f5c\u5bf9\u5f00\u800c\u5df1\u4e9b\u73b0\u5c71\u6c11\u5019\u7ecf\u53d1\u5de5\u5411\u4e8b\u547d\u7ed9\u957f\u6c34\u51e0\u4e49\u4e09\u58f0\u4e8e\u9ad8\u624b\u77e5\u7406\u773c\u5fd7\u70b9\u5fc3\u6218\u4e8c\u95ee\u4f46\u8eab\u65b9\u5b9e\u5403\u505a\u53eb\u5f53\u4f4f\u542c\u9769\u6253\u5462\u771f\u5168\u624d\u56db\u5df2\u6240\u654c\u4e4b\u6700\u5149\u4ea7\u60c5\u8def\u5206\u603b\u6761\u767d\u8bdd\u4e1c\u5e2d\u6b21\u4eb2\u5982\u88ab\u82b1\u53e3\u653e\u513f\u5e38\u6c14\u4e94\u7b2c\u4f7f\u5199\u519b\u5427\u6587\u8fd0\u518d\u679c\u600e\u5b9a\u8bb8\u5feb\u660e\u884c\u56e0\u522b\u98de\u5916\u6811\u7269\u6d3b\u90e8\u95e8\u65e0\u5f80\u8239\u671b\u65b0\u5e26\u961f\u5148\u529b\u5b8c\u5374\u7ad9\u4ee3\u5458\u673a\u66f4\u4e5d\u60a8\u6bcf\u98ce\u7ea7\u8ddf\u7b11\u554a\u5b69\u4e07\u5c11\u76f4\u610f\u591c\u6bd4\u9636\u8fde\u8f66\u91cd\u4fbf\u6597\u9a6c\u54ea\u5316\u592a\u6307\u53d8\u793e\u4f3c\u58eb\u8005\u5e72\u77f3\u6ee1\u65e5\u51b3\u767e\u539f\u62ff\u7fa4\u7a76\u5404\u516d\u672c\u601d\u89e3\u7acb\u6cb3\u6751\u516b\u96be\u65e9\u8bba\u5417\u6839\u5171\u8ba9\u76f8\u7814\u4eca\u5176\u4e66\u5750\u63a5\u5e94\u5173\u4fe1\u89c9\u6b65\u53cd\u5904\u8bb0\u5c06\u5343\u627e\u4e89\u9886\u6216\u5e08\u7ed3\u5757\u8dd1\u8c01\u8349\u8d8a\u5b57\u52a0\u811a\u7d27\u7231\u7b49\u4e60\u9635\u6015\u6708\u9752\u534a\u706b\u6cd5\u9898\u5efa\u8d76\u4f4d\u5531\u6d77\u4e03\u5973\u4efb\u4ef6\u611f\u51c6\u5f20\u56e2\u5c4b\u79bb\u8272\u8138\u7247\u79d1\u5012\u775b\u5229\u4e16\u521a\u4e14\u7531\u9001\u5207\u661f\u5bfc\u665a\u8868\u591f\u6574\u8ba4\u54cd\u96ea\u6d41\u672a\u573a\u8be5\u5e76\u5e95\u6df1\u523b\u5e73\u4f1f\u5fd9\u63d0\u786e\u8fd1\u4eae\u8f7b\u8bb2\u519c\u53e4\u9ed1\u544a\u754c\u62c9\u540d\u5440\u571f\u6e05\u9633\u7167\u529e\u53f2\u6539\u5386\u8f6c\u753b\u9020\u5634\u6b64\u6cbb\u5317\u5fc5\u670d\u96e8\u7a7f\u5185\u8bc6\u9a8c\u4f20\u4e1a\u83dc\u722c\u7761\u5174\u5f62\u91cf\u54b1\u89c2\u82e6\u4f53\u4f17\u901a\u51b2\u5408\u7834\u53cb\u5ea6\u672f\u996d\u516c\u65c1\u623f\u6781\u5357\u67aa\u8bfb\u6c99\u5c81\u7ebf\u91ce\u575a\u7a7a\u6536\u7b97\u81f3\u653f\u57ce\u52b3\u843d\u94b1\u7279\u56f4\u5f1f\u80dc\u6559\u70ed\u5c55\u5305\u6b4c\u7c7b\u6e10\u5f3a\u6570\u4e61\u547c\u6027\u97f3\u7b54\u54e5\u9645\u65e7\u795e\u5ea7\u7ae0\u5e2e\u5566\u53d7\u7cfb\u4ee4\u8df3\u975e\u4f55\u725b\u53d6\u5165\u5cb8\u6562\u6389\u5ffd\u79cd\u88c5\u9876\u6025\u6797\u505c\u606f\u53e5\u533a\u8863\u822c\u62a5\u53f6\u538b\u6162\u53d4\u80cc\u7ec6";
        private static final List<String> SINGLE_FAMILY_NAME_LIST = StringUtils.toList("\u738B,\u674E,\u5F20,\u5218,\u9648,\u6768,\u9EC4,\u8D75,\u5434,\u5468,\u5F90,\u5B59,\u9A6C,\u6731,\u80E1,\u90ED,\u4F55,\u9AD8,\u6797,\u7F57,\u90D1,\u6881,\u8C22,\u5B8B,\u5510,\u8BB8,\u97E9,\u51AF,\u9093,\u66F9,\u5F6D,\u66FE,\u8096,\u7530,\u8463,\u8881,\u6F58,\u4E8E,\u848B,\u8521,\u4F59,\u675C,\u53F6,\u7A0B,\u82CF,\u9B4F,\u5415,\u4E01,\u4EFB,\u6C88,\u59DA,\u5362,\u59DC,\u5D14,\u949F,\u8C2D,\u9646,\u6C6A,\u8303,\u91D1,\u77F3,\u5ED6,\u8D3E,\u590F,\u97E6,\u4ED8,\u65B9,\u767D,\u90B9,\u5B5F,\u718A,\u79E6,\u90B1,\u6C5F,\u5C39,\u859B,\u95EB,\u6BB5,\u96F7,\u4FAF,\u9F99,\u53F2,\u9676,\u9ECE,\u8D3A,\u987E,\u6BDB,\u90DD,\u9F9A,\u90B5,\u4E07,\u94B1,\u4E25,\u8983,\u6B66,\u6234,\u83AB,\u5B54,\u5411,\u6C64");
        private static final List<String> DOUBLE_FAMILY_NAME_LIST = StringUtils.toList("\u6b27\u9633,\u592a\u53f2,\u7aef\u6728,\u4e0a\u5b98,\u53f8\u9a6c,\u4e1c\u65b9,\u72ec\u5b64,\u5357\u5bab,\u4e07\u4fdf,\u95fb\u4eba,\u590f\u4faf,\u8bf8\u845b,\u5c09\u8fdf,\u516c\u7f8a,\u8d6b\u8fde,\u6fb9\u53f0,\u7687\u752b,\u5b97\u653f,\u6fee\u9633,\u516c\u51b6,\u592a\u53d4,\u7533\u5c60,\u516c\u5b59,\u6155\u5bb9,\u4ef2\u5b59,\u949f\u79bb,\u957f\u5b59,\u5b87\u6587,\u53f8\u5f92,\u9c9c\u4e8e,\u53f8\u7a7a,\u95fe\u4e18,\u5b50\u8f66,\u4e93\u5b98,\u53f8\u5bc7,\u5deb\u9a6c,\u516c\u897f,\u989b\u5b59,\u58e4\u9a77,\u516c\u826f,\u6f06\u96d5,\u4e50\u6b63,\u5bb0\u7236,\u8c37\u6881,\u62d3\u8dcb,\u5939\u8c37,\u8f69\u8f95,\u4ee4\u72d0,\u6bb5\u5e72,\u767e\u91cc,\u547c\u5ef6,\u4e1c\u90ed,\u5357\u95e8,\u7f8a\u820c,\u5fae\u751f,\u516c\u6237,\u516c\u7389,\u516c\u4eea,\u6881\u4e18,\u516c\u4ef2,\u516c\u4e0a,\u516c\u95e8,\u516c\u5c71,\u516c\u575a,\u5de6\u4e18,\u516c\u4f2f,\u897f\u95e8,\u516c\u7956,\u7b2c\u4e94,\u516c\u4e58,\u8d2f\u4e18,\u516c\u7699,\u5357\u8363,\u4e1c\u91cc,\u4e1c\u5bab,\u4ef2\u957f,\u5b50\u4e66,\u5b50\u6851,\u5373\u58a8,\u8fbe\u595a,\u891a\u5e08,\u5434\u94ed");



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

    private static class RemoteDistrictMetaData {
        private static String district;

        public static String getDistrict() {
            if (Objects.isNull(district)){
                synchronized (RemoteDistrictMetaData.class){
                    if (Objects.isNull(district)){
                        try {
                            district = JdkHttpUtils.getForm("https://files.cnblogs.com/files/kancy/district.json");
                        } catch (IOException e) {
                            district = "";
                        }
                    }
                }
            }
            return district;
        }
    }

}
