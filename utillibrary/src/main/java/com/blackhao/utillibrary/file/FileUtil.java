package com.blackhao.utillibrary.file;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;

import com.blackhao.utillibrary.log.LogHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author ： BlackHao
 * Time : 2018/4/9 10:10
 * Description : 文件工具类
 */
public class FileUtil {

    /**
     * 获取缓存文件
     *
     * @param context  上下文对象
     * @param fileName 文件名
     * @return 文件路径
     */
    public static File getTempFile(Context context, String fileName) {
        File file = null;
        try {
            String[] fix = fileName.split("\\.");
            String preFix = fix[0];
            String sufFix = null;
            if (fix.length > 1) {
                sufFix = "." + fix[1];
            }
            String path = context.getCacheDir().getAbsolutePath() + File.separator + "temp";
            //判断文件夹是否存在,不存在直接创建
            File folder = new File(path);
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    //创建失败
                    LogHelper.getInstance().e("create folder " + path + " fail");
                    return null;
                }
            }
            file = File.createTempFile(preFix, sufFix, folder);
            //程序结束自动删除文件（这里好像并没有什么效果,所以需要手动去调用delTempFileBeforeTime）
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 删除临时文件夹下，指定时间以前的所有临时文件
     *
     * @param context 上下文对象
     * @param msec    指定时间（毫秒）
     */
    public void delTempFileBeforeTime(Context context, long msec) {
        String path = context.getCacheDir().getAbsolutePath() + File.separator + "temp";
        delFileBeforeTime(path, msec);
    }

    /**
     * 删除文件夹下指定时间以前的所有文件
     *
     * @param folderPath 文件夹路径
     * @param msec       指定时间（毫秒）,传 <=0 表示删除所有的文件
     */
    public static void delFileBeforeTime(String folderPath, long msec) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.isFile() && (msec <= 0 || file.lastModified() < msec)) {
                        file.delete();
//                        LogHelper.getInstance().e("delete file :" + file.getAbsolutePath());
                    } else if (file.isDirectory()) {
                        delFileBeforeTime(file.getAbsolutePath(), msec);
                        //这里如果文件夹已经为空，直接删除文件夹
                        File[] folderFiles = file.listFiles();
                        if (folderFiles == null || folderFiles.length == 0) {
                            file.delete();
//                            LogHelper.getInstance().e("delete folder :" + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 检测文件是否存在
     *
     * @param path 文件夹路径
     */
    public static boolean checkFileExists(String path) {
        File file = new File(path);
        //判断文件夹是否存在
        return file.exists();
    }

    /**
     * 拷贝文件到指定文件夹
     *
     * @param fromFilePath 文件的绝对路径
     * @param toFolder     目标文件夹
     */
    public static boolean copyFile(String fromFilePath, String toFolder) {
        boolean result = false;
        File from = new File(fromFilePath);
        File to = new File(toFolder);
        if (from.exists() && to.exists() && to.isDirectory()) {
            File dst = new File(toFolder + File.separator + from.getName());
            if (!dst.exists()) {
                try {
                    dst.createNewFile();
                    FileInputStream fis = new FileInputStream(from);
                    FileOutputStream fos = new FileOutputStream(dst);
                    byte[] data = new byte[10 * 1024];
                    int len;
                    while ((len = fis.read(data)) > 0) {
                        fos.write(data, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    fis.close();
                    result = true;
                } catch (IOException e) {
                    LogHelper.getInstance().e("copyFile: " + e.toString());
                    result = false;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * 拷贝整个文件夹内容到另一个文件夹（只拷贝文件夹内的所有文件）
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp;
            for (String aFile : file) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + aFile);
                } else {
                    temp = new File(oldPath + File.separator + aFile);
                }

                if (temp.isFile()) {
                    //如果是文件，直接拷贝
                    copyFile(temp.getAbsolutePath(), newPath);
                } else if (temp.isDirectory()) {
                    //如果是子文件夹
                    copyFolder(oldPath + "/" + aFile, newPath + "/" + aFile);
                }
            }
        } catch (Exception e) {
            LogHelper.getInstance().e(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 删除单个文件
     **/
    public static boolean delSingleFile(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * 修改文件/文件夹名
     *
     * @param path    文件路径
     * @param newName 文件新的名称
     */
    public static boolean modifyFileName(String path, String newName) {
        File file = new File(path);
        if (file.exists()) {
            String newPath = file.getParentFile().getAbsolutePath() + File.separator + newName;
            return file.renameTo(new File(newPath));
        }
        return false;
    }

    /**
     * 删除文件夹及其目录下所有文件
     *
     * @param folderPath 文件夹路径
     */
    public static void delFolder(String folderPath) {
        try {
            delFileBeforeTime(folderPath, 0); //删除完里面所有内容
            File myFilePath = new File(folderPath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将URI转成绝对路径
     *
     * @param context 上下文对象
     * @param uri     URI
     * @return 绝对路径
     */
    public static String convertUriToPath(Context context, Uri uri) {
        if (null == uri) return null;
        String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 是否是文本文件
     *
     * @param s 文件名
     */
    public static boolean isTextFile(String s) {
        s = s.toLowerCase();      //转成小写的
        return s.endsWith(".txt");
    }

    /**
     * 判断是否是图片文件
     *
     * @return fileName 文件名（文件路径）
     */
    public static boolean isImageFile(String fileName) {
        fileName = fileName.toLowerCase();      //转成小写的
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                || fileName.endsWith(".png");
    }

    /**
     * 判断是否是 Office 文件
     *
     * @return fileName 文件名（文件路径）
     */
    public static boolean isOfficeFile(String fileName) {
        fileName = fileName.toLowerCase();      //转成小写的
        return fileName.endsWith(".doc") || fileName.endsWith(".ppt") || fileName.endsWith(".xls")
                || fileName.endsWith(".docx") || fileName.endsWith(".pptx") || fileName.endsWith(".xlsx");
    }

    /**
     * 判断是否是视频文件
     *
     * @return fileName 文件名（文件路径）
     */
    public static boolean isVideoFile(String fileName) {
        fileName = fileName.toLowerCase();      //转成小写的
        return fileName.endsWith(".mp4") || fileName.endsWith(".flv");
    }

    /**
     * 获取指定文件夹的文件列表
     *
     * @param path 文件夹列表
     */
    public static List<File> getFolderFile(String path) {
        List<File> list = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) {
            //不存在则创建文件夹
            file.mkdirs();
        }
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null && fileList.length > 0) {
                Collections.addAll(list, fileList);
            }
        }
        return list;
    }

    /**
     * 获取 sdcard 可用空间的大小
     *
     * @return 可用空间
     */
    public static long getUseableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        long blockSize;
        long freeBlocks;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = sf.getBlockSizeLong();
            freeBlocks = sf.getAvailableBlocksLong();
            // return freeBlocks * blockSize; //单位Byte
            // return (freeBlocks * blockSize)/1024; //单位KB
            return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
        } else {
            return -1;
        }

    }

}
