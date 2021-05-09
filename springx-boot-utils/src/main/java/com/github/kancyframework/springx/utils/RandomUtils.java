package com.github.kancyframework.springx.utils;


import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 获取随机数
 * @author kancy
 * @version 1.0
 * @date 2019/5/27 13:31
 */
public class RandomUtils {

    public static int nextInt(){
        return ThreadLocalRandom.current().nextInt();
    }
    public static int nextInt(int bound){
        return ThreadLocalRandom.current().nextInt(bound);
    }
    public static int nextInt(int start,int end){
        return ThreadLocalRandom.current().nextInt(start, end);
    }
    public static int[] ints(int size, int start, int end){
        int[] ints = new int[size];
        for (int i = 0; i < size; i++) {
            ints[i] = nextInt(start, end);
        }
        return ints;
    }

    public static long nextLong(){
        return ThreadLocalRandom.current().nextLong();
    }
    public static long nextLong(long bound){
        return ThreadLocalRandom.current().nextLong(bound);
    }
    public static long nextLong(long start,long end){
        return ThreadLocalRandom.current().nextLong(start, end);
    }
    public static long[] longs(int size, long start, long end){
        long[] longs = new long[size];
        for (int i = 0; i < size; i++) {
            longs[i] = nextLong(start, end);
        }
        return longs;
    }

    public static double nextDouble(){
        return ThreadLocalRandom.current().nextDouble();
    }
    public static double nextDouble(int scale){
        return BigDecimal.valueOf(nextDouble())
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
    public static double nextDouble(double bound){
        return ThreadLocalRandom.current().nextDouble(bound);
    }
    public static double nextDouble(double bound, int scale){
        return BigDecimal.valueOf(nextDouble(bound))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
    public static double nextDouble(double start,double end){
        return ThreadLocalRandom.current().nextDouble(start, end);
    }
    public static double nextDouble(double start, double end, int scale){
        return BigDecimal.valueOf(nextDouble(start, end))
                .setScale(scale, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
    public static double[] doubles(int size, double start, double end){
        return doubles(size, start, end, 2);
    }

    public static double[] doubles(int size, double start, double end, int scale){
        double[] doubles = new double[size];
        for (int i = 0; i < size; i++) {
            doubles[i] = nextDouble(start, end, scale);
        }
        return doubles;
    }

    public static boolean nextBoolean(){
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static boolean nextTrue(double probability){
        return nextDouble() < probability;
    }

    public static boolean nextFalse(double probability){
        return nextDouble() > probability;
    }

    public static String nextString(String str){
        List<String> rangeStr = StringUtils.toList(str);
        return rangeStr.get(nextInt(rangeStr.size()));
    }

    public static String nextString(String str, String splitChar){
        List<String> rangeStr = StringUtils.toList(str, splitChar);
        return rangeStr.get(nextInt(rangeStr.size()));
    }

    public static String nextString(String ... strArrays){
        List<String> rangeStr = CollectionUtils.toList(strArrays);
        return rangeStr.get(nextInt(rangeStr.size()));
    }

    /**
     * 得到一组符合高斯/正态分布的随机数，是否可以利用nextGaussian方法
     * @return
     */
    public static double nextGaussian(){
        return ThreadLocalRandom.current().nextGaussian();
    }

}
