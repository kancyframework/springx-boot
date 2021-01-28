package com.github.kancyframework.springx.utils;

import com.github.kancyframework.springx.log.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.WeakHashMap;

/**
 * @author kancy
 * @version 1.0
 * @date 2019/2/27 14:00
 */
public class DebugUtil {
    /**
     * 使用弱引用，当线程回收时，自动移除Key,Value
     */
    private static WeakHashMap<Thread, TimeDebug> weakHashMap = new WeakHashMap();

    /**
     * 记录当前线程当前时刻
     */
    public static void startPoint(){
        if(!weakHashMap.containsKey(Thread.currentThread())){
            weakHashMap.put(Thread.currentThread(), new TimeDebug());
        }
    }

    /**
     * 打印耗时
     */
    public static void printPoint(){
        printPoint("C");
    }
    /**
     * 打印耗时
     */
    public static void printPoint(String tag){
        if(weakHashMap.containsKey(Thread.currentThread())){
            TimeDebug timeDebug = weakHashMap.get(Thread.currentThread());
            timeDebug.setCurrentEndTime(System.currentTimeMillis());
            timeDebug.setTag(tag);
            timeDebug.setMode(TimeDebug.MODE_CURR);
            log(timeDebug.toString());
            // 日志打印完后重置开始时间
            timeDebug.setCurrentStartTime(System.currentTimeMillis());
        }else{
            // 没有设置开始时，先设置开始
            if(Thread.currentThread() != null){
                startPoint();
            }
        }
    }

    /**
     * 结束断点
     */
    public static void endPoint(){
        endPoint("F");
    }
    public static void endPoint(String tag){
        if(weakHashMap.containsKey(Thread.currentThread())){
            TimeDebug timeDebug = weakHashMap.get(Thread.currentThread());
            long now = System.currentTimeMillis();
            timeDebug.setCurrentEndTime(now);
            timeDebug.setFinalTime(now);
            timeDebug.setTag(tag);
            timeDebug.setMode(TimeDebug.MODE_FINAL);
            log(timeDebug.toString());
            clearPoint();
        }else{
            log("没有初始化Debug断点，请先调用：startPoint() !");
        }
    }

    /**
     * 清除Debug point
     */
    public static void clearPoint(){
        weakHashMap.remove(Thread.currentThread());
    }
    /**
     * 清除Debug point
     */
    public static void clearAll(){
        weakHashMap.clear();
    }

    @Deprecated
    private static void log(String msg) {
        Log.info(msg);
//        if (Log.isDebug()){
//            Log.debug(msg);
//        }
    }

    private static class TimeDebug{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private static final int MODE_CURR = 1;
        private static final int MODE_FINAL = 2;
        private long seq;
        private long currentStartTime;
        private long currentEndTime;
        private long originalTime;
        private long finalTime;
        private long sumCount;
        private String tag;

        private int mode;

        public TimeDebug() {
            setCurrentStartTime(System.currentTimeMillis());
            this.originalTime = currentStartTime;
        }
        public void setFinalTime(long finalTime) {
            this.finalTime = finalTime;
        }
        public void setCurrentStartTime(long currentStartTime) {
            this.currentStartTime = currentStartTime;
        }
        public void setCurrentEndTime(long currentEndTime) {
            this.currentEndTime = currentEndTime;
        }
        public void setTag(String tag) {
            this.tag = tag;
        }
        public void setMode(int mode) {
            this.mode = mode;
        }

        public String fromCurrentPoint() {
            long consumTime = currentEndTime - currentStartTime;
            return "===[Debug]===》 " +
                    "Point("+ seq +")，" +
                    "当前线程["+ Thread.currentThread().getName()+"]，" +
                    "TAG["+ tag +"]，" +
                    "耗时[from("+ df.format(new Date(currentStartTime))+")->to("+ df.format(new Date(currentEndTime)) +")]："+ formatConsumeTime(consumTime) + "【"+consumTime+"】！";
        }

        public String fromEndPoint() {
            if(seq == 0){
                ++seq;
                sumCount += (this.currentEndTime - this.currentStartTime);
            }
            long consumTime = finalTime - originalTime;
            return "===[Debug]===》 " +
                    "PointCount("+ seq +")，" +
                    "当前线程["+ Thread.currentThread().getName()+"]，" +
                    "TAG["+ tag +"]，" +
                    "任务总耗时[from("+ df.format(new Date(originalTime))+")->to("+ df.format(new Date(finalTime)) +")]："+ formatConsumeTime(consumTime) + "【"+consumTime+"】，" +
                    "任务执行总耗时："+ formatConsumeTime(sumCount) + "【"+ sumCount +"】！";
        }

        @Override
        public String toString() {
            switch (mode){
                case MODE_CURR:updateCount();return fromCurrentPoint();
                case MODE_FINAL:return fromEndPoint();
            }
            return null;
        }

        /**
         * 更新
         */
        private void updateCount() {
            if(mode != MODE_FINAL){
                ++seq;
                sumCount += (this.currentEndTime - this.currentStartTime);
            }
        }
    }

    /**
     * 格式化时间
     * @param time
     * @return
     */
    private static String formatConsumeTime(long time){
        int sec = 1000;
        if(time <= sec){
            return time + "毫秒";
        }
        int minute = 60 * sec;
        if(time <= minute){
            return time/sec + "秒" + time % sec + "毫秒";
        }
        int hour = 60 * minute;
        if(time <= hour){
            return time/minute + "分" + time/sec % 60 + "秒" + time % sec + "毫秒";
        }
        int day = 12 * hour;
        if(time <= 12 * 60 * 60 * 1000){
            return time/hour + "小时" + time/minute % 60 + "分" + time/sec  % 60 + "秒" + time % sec + "毫秒";
        }
        return time/day + "天" + time/hour % 12 + "小时" + time/minute % 60 + "分" + time/sec  % 60 + "秒" + time % sec + "毫秒";
    }

}
