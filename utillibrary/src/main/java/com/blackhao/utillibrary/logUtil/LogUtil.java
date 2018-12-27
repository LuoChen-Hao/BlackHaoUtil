package com.blackhao.utillibrary.logUtil;

import android.util.Log;

/**
 * Author ： BlackHao
 * Time : 2017/8/1 14:05
 * Description : Log 工具类 ,支持跳转对应的代码位置
 */
public class LogUtil {
    //单例
    private static LogUtil logUtil;
    //打印调试开关
    private static boolean IS_DEBUG = true;
    //Log 单词打印的最大长度
    private static final int MAX_LENGTH = 3 * 1024;

    //单例模式初始化
    public static LogUtil getInstance() {
        if (logUtil == null) {
            logUtil = new LogUtil();
        }
        return logUtil;
    }

    /**
     * 获取 TAG 信息：文件名以及行数
     *
     * @return TAG 信息
     */
    private synchronized String getTAG() {
        StringBuilder tag = new StringBuilder();
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return "";
        }
        for (StackTraceElement st : sts) {
            //筛选获取需要打印的TAG
            if (!st.isNativeMethod() && !st.getClassName().equals(Thread.class.getName()) && !st.getClassName().equals(this.getClass().getName())) {
                //获取文件名以及打印的行数
                tag.append("(").append(st.getFileName()).append(":").append(st.getLineNumber()).append(")");
                return tag.toString();
            }
        }
        return "";
    }

    /**
     * Log.e 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void e(String text) {
        if (IS_DEBUG) {
            for (String str : splitStr(text)) {
                Log.e(getTAG(), str);
            }
        }
    }

    /**
     * Log.d 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void d(String text) {
        if (IS_DEBUG) {
            for (String str : splitStr(text)) {
                Log.d(getTAG(), str);
            }
        }
    }

    /**
     * Log.w 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void w(String text) {
        if (IS_DEBUG) {
            for (String str : splitStr(text)) {
                Log.w(getTAG(), str);
            }
        }
    }

    /**
     * Log.i 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void i(String text) {
        if (IS_DEBUG) {
            for (String str : splitStr(text)) {
                Log.i(getTAG(), str);
            }
        }
    }

    /**
     * Log.e 打印格式化后的JSON数据
     *
     * @param json 需要打印的内容
     */
    public synchronized void json(String json) {
        if (IS_DEBUG) {
            String tag = getTAG();
            try {
                //转化后的数据
                String logStr = formatJson(json);
                for (String str : splitStr(logStr)) {
                    Log.e(getTAG(), str);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(tag, e.toString());
            }
        }
    }

    /**
     * 数据分割成不超过 MAX_LENGTH的数据
     *
     * @param str 需要分割的数据
     * @return 分割后的数组
     */
    private String[] splitStr(String str) {
        //字符串长度
        int length = str.length();
        //返回的数组
        String[] strs = new String[length / MAX_LENGTH + 1];
        //
        int start = 0;
        for (int i = 0; i < strs.length; i++) {
            //判断是否达到最大长度
            if (start + MAX_LENGTH < length) {
                strs[i] = str.substring(start, start + MAX_LENGTH);
                start += MAX_LENGTH;
            } else {
                strs[i] = str.substring(start, length);
                start = length;
            }
        }
        return strs;
    }


    /**
     * 格式化
     *
     * @param jsonStr json数据
     * @return 格式化后的json数据
     * @author lizhgb
     * @link https://my.oschina.net/jasonli0102/blog/517052
     */
    private String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 在 StringBuilder指定位置添加 space
     *
     * @param sb     字符集
     * @param indent 添加位置
     * @author lizhgb
     * @link https://my.oschina.net/jasonli0102/blog/517052
     */
    private void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
