package com.github.kancyframework.springx.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * ID生成工具
 * @author huangchengkang
 */
public class IDUtils {
    private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker();

    private IDUtils() {
    }

    public static String getUUIDString() {
        return UUID.randomUUID().toString();
    }

    public static String get32UUIDString() {
        return getUUIDString().replace("-", "");
    }

    public static long getSnowflakeId() {
        return idWorker.getSerialNo();
    }

    public static String getSnowflakeNo(String prefix) {
        return idWorker.getSerialNo(prefix);
    }

    public static String getDateSnowflakeNo(String prefix) {
        return idWorker.getDateSerialNo(prefix);
    }

    public static String getDateSnowflakeNo() {
        return idWorker.getDateSerialNo();
    }

    /**
     * Twitter_Snowflake
     * SnowFlake的结构如下(每部分用-分开):
     * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
     * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
     * 41位时间戳(毫秒级)，注意，41位时间戳不是存储当前时间的时间戳，而是存储时间戳的差值（当前时间戳 - 开始时间戳)
     * 得到的值），这里的的开始时间戳，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下面程序SnowflakeIdWorker类的startTime属性）。41位的时间戳，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
     * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId  (32*32台机器)
     * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间戳)产生4096个ID序号
     * 加起来刚好64位，为一个Long型。
     */
    private static class SnowflakeIdWorker {
        /** 开始时间戳 (2018-01-01) */
        private final long twepoch = 1514736000000L;

        /** 机器id所占的位数 */
        private final long workerIdBits = 5L;

        /** 数据标识id所占的位数 */
        private final long datacenterIdBits = 5L;

        /** 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数) */
        private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

        /** 支持的最大数据标识id，结果是31 */
        private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

        /** 序列在id中占的位数 */
        private final long sequenceBits = 12L;

        /** 机器ID向左移12位 */
        private final long workerIdShift = sequenceBits;

        /** 数据标识id向左移17位(12+5) */
        private final long datacenterIdShift = sequenceBits + workerIdBits;

        /** 时间戳向左移22位(5+5+12) */
        private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

        /** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
        private final long sequenceMask = -1L ^ (-1L << sequenceBits);

        /** 工作机器ID(0~31) */
        private long workerId;

        /** 数据中心ID(0~31) */
        private long datacenterId;

        /** 毫秒内序列(0~4095) */
        private long sequence = 0L;

        /** 上次生成ID的时间戳 */
        private long lastTimestamp = -1L;

        private static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");

        private static Integer pid = getPId();

        //==============================Constructors=====================================

        /**
         * 随机性
         */
        public SnowflakeIdWorker() {
            long workerId = getWorkId();
            long datacenterId = getDataCenterId();
            init(workerId, datacenterId);
        }

        /**
         * 构造函数
         * @param workerId 工作ID (0~31)
         * @param datacenterId 数据中心ID (0~31)
         */
        public SnowflakeIdWorker(long workerId, long datacenterId) {
            init(workerId, datacenterId);
        }

        /**
         * 初始化
         * @param workerId
         * @param datacenterId
         */
        private void init(long workerId, long datacenterId) {
            if (workerId > maxWorkerId || workerId < 0) {
                throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
            }
            if (datacenterId > maxDatacenterId || datacenterId < 0) {
                throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
            }
            this.workerId = workerId;
            this.datacenterId = datacenterId;
        }

        // ==============================Methods==========================================

        /**
         *  获取流水号 (该方法是线程安全的)
         * @return
         */
        public synchronized long getSerialNo() {
            return nextId();
        }

        /**
         *  获取带有前缀流水号 (该方法是线程安全的)
         * @return
         */
        public synchronized String getSerialNo(String prefix) {
            return prefix + nextId();
        }

        /**
         * yyyyMMdd（8） + pid（6） + nextId（18）
         * @return
         */
        public synchronized String getDateSerialNo() {
            return dateFormatter.format(new Date())+ pid + nextId();
        }

        /**
         * 获得下一个ID
         * @return SnowflakeId
         */
        private long nextId() {
            long timestamp = timeGen();

            //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
            if (timestamp < lastTimestamp) {
                throw new RuntimeException(
                        String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
            }

            //如果是同一时间生成的，则进行毫秒内序列
            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & sequenceMask;
                //毫秒内序列溢出
                if (sequence == 0) {
                    //阻塞到下一个毫秒,获得新的时间戳
                    timestamp = tilNextMillis(lastTimestamp);
                }
            }
            //时间戳改变，毫秒内序列重置
            else {
                sequence = 0L;
            }

            //上次生成ID的时间戳
            lastTimestamp = timestamp;

            //移位并通过或运算拼到一起组成64位的ID
            return ((timestamp - twepoch) << timestampLeftShift) //
                    | (datacenterId << datacenterIdShift) //
                    | (workerId << workerIdShift) //
                    | sequence;
        }

        /**
         * 保证获取的序列号长度为32
         * 效率会下降
         * prefix(n) + yyyyMMdd（8） + pid（6-n） + nextId（18）
         * @param prefix
         * @return
         */
        public synchronized String getDateSerialNo(String prefix) {
            if(prefix.length() >= 6){
                throw new IllegalArgumentException("prefix length is [0,5].");
            }
            int pidLen = 6 - prefix.length();
            String range = "";
            for (int i = pidLen; i > 0; i--) {
                range += "9";
            }
            return prefix +
                    dateFormatter.format(new Date())+
                    String.format("%0"+pidLen+"d", pid % Integer.parseInt(range)) +
                    nextId();
        }

        /**
         * 阻塞到下一个毫秒，直到获得新的时间戳
         * @param lastTimestamp 上次生成ID的时间戳
         * @return 当前时间戳
         */
        protected long tilNextMillis(long lastTimestamp) {
            long timestamp = timeGen();
            while (timestamp <= lastTimestamp) {
                timestamp = timeGen();
            }
            return timestamp;
        }

        /**
         * 返回以毫秒为单位的当前时间
         * @return 当前时间(毫秒)
         */
        protected long timeGen() {
            return System.currentTimeMillis();
        }

        private long numRandom(long bound){
            Random random = new Random();
            int anInt = random.nextInt((int) bound);
            return anInt;
        }

        private Long getWorkId() {
            try {
                String hostAddress = Inet4Address.getLocalHost().getHostAddress();
                int[] ints = toCodePoints(hostAddress);
                int sums = 0;
                for (int b : ints) {
                    sums += b;
                }
                return (long) (sums % 32);
            } catch (UnknownHostException e) {
                // 如果获取失败，则使用随机数备用
                return numRandom(maxWorkerId);
            }
        }

        private Long getDataCenterId() {
            try {
                int[] ints = toCodePoints(Inet4Address.getLocalHost().getHostName());
                int sums = 0;
                for (int i : ints) {
                    sums += i;
                }
                return (long) (sums % 32);
            } catch (UnknownHostException e) {
                // 如果获取失败，则使用随机数备用
                return numRandom(maxDatacenterId);
            }
        }

        /**
         * 字符串转int数组
         * @param str
         * @return
         */
        private static int[] toCodePoints(CharSequence str) {
            if (str == null) {
                return null;
            } else if (str.length() == 0) {
                return new int[0];
            } else {
                String s = str.toString();
                int[] result = new int[s.codePointCount(0, s.length())];
                int index = 0;
                for(int i = 0; i < result.length; ++i) {
                    result[i] = s.codePointAt(index);
                    index += Character.charCount(result[i]);
                }
                return result;
            }
        }

        /**
         * 获取系统的进程号
         * @return
         */
        private static Integer getPId() {
            RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
            String jvmName = bean.getName();
            String pid = jvmName.split("@")[0];
            return Integer.parseInt(pid);
        }

    }

}
