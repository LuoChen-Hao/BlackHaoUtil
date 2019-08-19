package com.blackhao.utillibrary.log;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.blackhao.utillibrary.BuildConfig;
import com.blackhao.utillibrary.time.TimeUtil;

import java.io.File;
import java.io.FileWriter;

/**
 * Author ： BlackHao
 * Time : 2017/8/1 14:05
 * Description : Log 工具类 ,支持跳转对应的代码位置
 */
public class LogHelper {

    //单例
    private static LogHelper logUtil;
    //打印等级(这里用于release版本打印)
    private static int LOG_LEVEL = Log.ERROR;
    //打印调试开关
    private static boolean IS_DEBUG = BuildConfig.DEBUG;
    //Log 单次打印的最大长度
    private static final int MAX_LENGTH = 3 * 1024;
    //是否需要写文件
    private static final boolean IS_WRITE_FILE = false;
    //文件路径
    private static String LOG_FILE_PATH;

    //单例模式初始化
    public static LogHelper getInstance() {
        if (logUtil == null) {
            logUtil = new LogHelper();
        }
        return logUtil;
    }

    /**
     * 初始化 LOG_FILE
     */
    public void initLogFile(Context context) {
        if (context != null) {
            LOG_FILE_PATH = context.getCacheDir().getAbsolutePath() + File.separator + "QChatLog";
        }
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
            if (!st.isNativeMethod()
                    && !st.getClassName().equals(Thread.class.getName())
                    && !st.getClassName().equals(this.getClass().getName())) {
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
        if (IS_DEBUG || Log.ERROR >= LOG_LEVEL) {
            if (TextUtils.isEmpty(text)) {
                Log.e(getTAG(), "Log Error text is null");
                return;
            }
            for (String str : splitStr(text)) {
                Log.e(getTAG(), str);
                writeLog("ERROR : " + getTAG() + " : " + str);
            }
        }
    }

    /**
     * Log.d 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void d(String text) {
        if (IS_DEBUG || Log.DEBUG >= LOG_LEVEL) {
            for (String str : splitStr(text)) {
                Log.d(getTAG(), str);
                writeLog("DEBUG : " + getTAG() + " : " + str);
            }
        }
    }

    /**
     * Log.w 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void w(String text) {
        if (IS_DEBUG || Log.WARN >= LOG_LEVEL) {
            for (String str : splitStr(text)) {
                Log.w(getTAG(), str);
                writeLog("WARN : " + getTAG() + " : " + str);
            }
        }
    }

    /**
     * Log.i 打印
     *
     * @param text 需要打印的内容
     */
    public synchronized void i(String text) {
        if (IS_DEBUG || Log.INFO >= LOG_LEVEL) {
            for (String str : splitStr(text)) {
                Log.i(getTAG(), str);
                writeLog("INFO : " + getTAG() + " : " + str);
            }
        }
    }

    /**
     * Log.e 打印格式化后的JSON数据
     *
     * @param json 需要打印的内容
     */
    public synchronized void json(String json) {
        if (IS_DEBUG || Log.ERROR >= LOG_LEVEL) {
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
        char last;
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

    /**
     * 将 log 写入文件
     *
     * @param log 需要写入文件的 log
     */
    private void writeLog(String log) {
        try {
            if (!IS_WRITE_FILE) {
                return;
            }
            if (LOG_FILE_PATH != null && LOG_FILE_PATH.length() > 0) {
                File folder = new File(LOG_FILE_PATH);
                if (folder.exists() || folder.mkdirs()) {
                    //文件夹存在或者创建文件夹成功
                    String file = LOG_FILE_PATH + File.separator + TimeUtil.getCurrentTime("yyyy-MM-dd") + ".txt";
                    File logFile = new File(file);
                    if (logFile.exists() || logFile.createNewFile()) {
                        FileWriter writer = new FileWriter(logFile, true);
                        writer.write(log + "\n");
                        writer.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}