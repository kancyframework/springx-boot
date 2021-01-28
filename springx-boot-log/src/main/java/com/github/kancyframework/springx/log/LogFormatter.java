package com.github.kancyframework.springx.log;

import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LogFormatter
 *
 * @author kancy
 * @date 2020/2/18 6:03
 */
class LogFormatter {

    private static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    private String logName;

    public LogFormatter(String logName) {
        this.logName = logName;
    }

    /**
     * 日志格式化
     * @param msg
     * @param logLevel
     * @return
     */
    public String format(String msg, LogLevel logLevel) {
        StringBuffer log = new StringBuffer();
        log.append(LocalDateTime.now().format(DATETIME_FORMATTER));
        log.append(" ").append(LogColorPrinter.getColorString(toRight(logLevel.name(), 5), LogColorPrinter.GREEN)).append(" ");
        log.append(LogColorPrinter.getColorString(getPid(), LogColorPrinter.MAGENTA));
        log.append(" --- ");
        log.append("[").append(getThreadName()).append("] ");
        log.append(LogColorPrinter.getColorString(String.format("%-40s", getSimpleLogName()), LogColorPrinter.CYAN));
        log.append(" : ");
        log.append(msg);
        return log.toString();
    }

    /**
     * ThreadName
     * @return
     */
    private String getThreadName() {
        String tmp = Thread.currentThread().getName();
        if (tmp.length() > 10){
            tmp = tmp.substring(tmp.length() -10);
        }
        return String.format("%-10s", tmp);
    }

    /**
     * pid
     * @return
     */
    private static String getPid(){
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
    }

    /**
     * 获取日志名称
     * @return
     */
    private String getSimpleLogName() {
        int maxLen = 40;
        if (logName.length() <= maxLen){
            return logName;
        }
        String[] stringArray = logName.split("[.]");

        StringBuffer sb = new StringBuffer();
        for (int i = stringArray.length -1; i >= 0 ; i--) {
            String content = stringArray[i];
            if (sb.length() == 0 && content.length() >= maxLen){
                return content.substring(0, maxLen);
            }
            if (sb.length() + content.length() < maxLen){
                sb.insert(0, content);
                sb.insert(0, ".");
            }else{
                // 获取每个文件夹的首字母
                sb.insert(0, content.substring(0,1));
                sb.insert(0, ".");
            }
        }
        // 只保留最右边的maxLen长度字符
        if (sb.length() > maxLen){
            return sb.substring(sb.length() - maxLen);
        }
        return sb.toString();
    }

    /**
     * toRight
     * @param str
     * @return
     */
    private String toRight(String str, int len){
        if (str.length() >= len){
            return str.substring(str.length() - len);
        }
        StringBuffer buffer = new StringBuffer(str);
        for (int i = 0; i <len ; i++) {
            buffer.insert(0, " ");
            if (buffer.length() == len){
                break;
            }
        }
        return buffer.toString();
    }
}
